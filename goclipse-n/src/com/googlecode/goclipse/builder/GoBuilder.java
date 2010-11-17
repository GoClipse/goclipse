package com.googlecode.goclipse.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.SysUtils;
import com.googlecode.goclipse.dependency.CycleException;
import com.googlecode.goclipse.dependency.IDependencyVisitor;

/**
 * 
 * @author steel
 */
public class GoBuilder extends IncrementalProjectBuilder {
	public static final String BUILDER_ID = "com.googlecode.goclipse.goBuilder";
	private static final String MARKER_TYPE = "com.googlecode.goclipse.problem";
	Map<String, String> goEnv = new HashMap<String, String>();
	private GoDependencyManager dependencyManager;
	private GoCompiler compiler;
	private GoLinker linker;
	private GoPacker packer;
	private boolean onlyFullBuild = false;
	
	class CollectResourceDeltaVisitor implements IResourceDeltaVisitor {
		List<IResource> added = new ArrayList<IResource>();
		List<IResource> removed = new ArrayList<IResource>();
		List<IResource> changed = new ArrayList<IResource>();

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.
		 * core.resources.IResourceDelta)
		 */
		public boolean visit(IResourceDelta delta) throws CoreException {
			// SysUtils.debug("visit");

			IResource resource = delta.getResource();
			if (resource instanceof IFile && resource.getName().endsWith(GoConstants.GO_SOURCE_FILE_EXTENSION)) {
				switch (delta.getKind()) {
				case IResourceDelta.ADDED:
					// handle added resource
					added.add(resource);
					break;
				case IResourceDelta.REMOVED:
					// handle removed resource
					removed.add(resource);
					break;
				case IResourceDelta.CHANGED:
					// handle changed resource
					changed.add(resource);
					break;
				}
			}
			// return true to continue visiting children.
			return true;
		}

		public List<IResource> getAdded() {
			return added;
		}

		public List<IResource> getRemoved() {
			return removed;
		}

		public List<IResource> getChanged() {
			return changed;
		}

	}

	class CollectResourceVisitor implements IResourceVisitor {
		private List<IResource> collected = new ArrayList<IResource>();

		public boolean visit(IResource resource) {
			SysUtils.debug("SampleResourceVisitor.visit:" + resource);
			if (resource instanceof IFile && resource.getName().endsWith(".go")) {
				collected.add(resource);
			}
			// return true to continue visiting children.
			return true;
		}

		public List<IResource> getCollectedResources() {
			return collected;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int,
	 * java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
		checkBuild();
		try {
			goEnv = GoConstants.environment();
			dependencyManager.setEnvironment(goEnv);
			compiler.setEnvironment(goEnv);
			
			IProject project = getProject();
			if (kind == FULL_BUILD || onlyFullBuild) {
				fullBuild(monitor);
				onlyFullBuild = false;
			} else {
				IResourceDelta delta = getDelta(project);
				if (delta == null) {
					dependencyManager.prepare(project, true);
					fullBuild(monitor);
				} else {
					dependencyManager.prepare(project, false);
					incrementalBuild(delta, monitor);
				}
			}
			dependencyManager.save(project);
			
		}catch(Exception e) {
			SysUtils.debug(e);
		}
		// no project dependencies (yet)
		return null;
	}

	private void checkBuild() throws CoreException {
		if (!Environment.INSTANCE.isValid()){
			IMarker marker = getProject().createMarker(IMarker.PROBLEM);
			marker.setAttribute(IMarker.MESSAGE, GoConstants.INVALID_PREFERENCES_MESSAGE);
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
			throw new CoreException(new Status(Status.ERROR,Activator.PLUGIN_ID, GoConstants.INVALID_PREFERENCES_MESSAGE));
		} else {
			if (dependencyManager == null){
				dependencyManager = new GoDependencyManager();
			}
			if (compiler == null){
				compiler = new GoCompiler();
			}
			if (linker == null){
				linker = new GoLinker();
			}
			if (packer == null){
				packer = new GoPacker();
			}
		}
	}

	protected void fullBuild(final IProgressMonitor pmonitor)
			throws CoreException {
		SysUtils.debug("fullBuild");
		final SubMonitor monitor = SubMonitor.convert(pmonitor, 250);

		CollectResourceVisitor crv = new CollectResourceVisitor();
		getProject().accept(crv);
		monitor.worked(20);

		List<IResource> toCompile = new ArrayList<IResource>();
		toCompile.addAll(crv.getCollectedResources()); // full build means
														// everything should be
														// compiled
		dependencyManager.clearState(monitor.newChild(10));
		clean(monitor.newChild(10));
		dependencyManager.buildDep(crv.getCollectedResources(), monitor.newChild(40));
		doBuild(null, monitor);
		SysUtils.debug("fullBuild - done");
	}

	private void doBuild(Set<String> fileList, final SubMonitor monitor) throws CoreException {
		try {
			dependencyManager.accept(fileList, new IDependencyVisitor() {
				@Override
				public void visit(String aTarget, String... dependencies) {
					IProject project = getProject();
					if (isCompile(aTarget, dependencies)) {
						ensureFolderExists(project, aTarget);
						compiler.compile(project, monitor.newChild(100), aTarget, dependencies);
					} else if (isLibArchive(aTarget, dependencies)) {
						ensureFolderExists(project, aTarget);
						packer.createArchive(project, monitor.newChild(100), aTarget, dependencies);
					} else if (dependencies.length > 0) {
						// assume it's an executable compile
						linker.createExecutable(project, aTarget, dependencies);
					}
				}

			});
		} catch (CycleException e) {
			addCycleError(e.getNodes());
		}
	}

	private void addCycleError(Set<String> nodes) throws CoreException {
		onlyFullBuild = true;
		StringBuilder builder = new StringBuilder(" [");
		boolean first = true;
		for (String node : nodes) {
			if (!first) {
				builder.append(", ");
			}
			builder.append(node);
			first = false;
		}
		builder.append("]");

		
		IMarker marker = getProject().createMarker(IMarker.PROBLEM);
		marker.setAttribute(IMarker.MESSAGE, GoConstants.CYCLE_DETECTED_MESSAGE+builder.toString());
		marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
	}

	private boolean isCompile(String aTarget, String[] dependencies) {
		if (dependencies.length == 0){
			return false;
		}
		for (String dep : dependencies) {
			if (!(dep.endsWith(GoConstants.GO_SOURCE_FILE_EXTENSION) || dep.endsWith(GoConstants.GO_LIBRARY_FILE_EXTENSION))) {
				return false;
			}
		}
		String ext = Environment.INSTANCE.getArch().getExtension();
		return aTarget.endsWith(ext);
	}

	private boolean isLibArchive(String aTarget, String[] dependencies) {
		if (dependencies.length == 0){
			return false;
		}
		String ext = Environment.INSTANCE.getArch().getExtension();
		for (String dep : dependencies) {
			if (!dep.endsWith(ext)){
				return false;
			}
		}
		return aTarget.endsWith(GoConstants.GO_LIBRARY_FILE_EXTENSION);
	}

	private void ensureFolderExists(IProject project, String aTarget) {
		IPath file = project.getFile(aTarget).getRawLocation();
		IPath parentFolder = file.removeLastSegments(1);
		File parentFile = parentFolder.toFile();
		if (!parentFile.exists()){
			parentFile.mkdirs();
		}
	}

	
	protected void incrementalBuild(IResourceDelta delta,
			IProgressMonitor pmonitor) throws CoreException {
		SysUtils.debug("incrementalBuild");
		SubMonitor monitor = SubMonitor.convert(pmonitor, 170);
		// collect resources
		CollectResourceDeltaVisitor crdv = new CollectResourceDeltaVisitor();
		delta.accept(crdv);

		monitor.worked(20);
		// remove
		List<IResource> toRemove = crdv.getRemoved();
		//get a snapshot of dependencies for toRemove
		//dependencies will be sent to compile
		dependencyManager.removeDep(toRemove, pmonitor);
		
		// compile
		List<IResource> resourcesToCompile = new ArrayList<IResource>();
		resourcesToCompile.addAll(crdv.getAdded());
		resourcesToCompile.addAll(crdv.getChanged());
		dependencyManager.buildDep(resourcesToCompile, monitor.newChild(10));

		Set<String> toCompile = new HashSet<String>();
		for (IResource res : resourcesToCompile) {
			toCompile.add(res.getProjectRelativePath().toOSString());
		}
		
		doBuild(toCompile, monitor);
		SysUtils.debug("incrementalBuild - done");
	}

	

	

	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		SysUtils.debug("cleaning project");
		IProject project = getProject();
		project.deleteMarkers(IMarker.PROBLEM, false,
				IResource.DEPTH_INFINITE);
		IPath binPath = Environment.INSTANCE.getBinOutputFolder(project);
		File binFolder = new File(project.getLocation().append(binPath).toOSString());
		if (binFolder.exists()) {
			deleteFolder(binFolder, true);
		}
		IPath pkgPath = Environment.INSTANCE.getPkgOutputFolder(project);
		File pkgFolder = new File(project.getLocation().append(pkgPath).toOSString());
		if (pkgFolder.exists()) {
			deleteFolder(pkgFolder, true);
		}
		
		project.accept(new IResourceVisitor() {
			@Override
			public boolean visit(IResource resource) throws CoreException {
				IPath relativePath = resource.getProjectRelativePath();
				Environment instance = Environment.INSTANCE;
				String lastSegment = relativePath.lastSegment();
				IPath rawLocation = resource.getRawLocation();
				if (rawLocation != null) {
					File file = rawLocation.toFile();
					if (file.exists() && file.isDirectory() &&
						(instance.isCmdFile(relativePath) || instance.isPkgFile(relativePath)) 
						&& (lastSegment.equals(GoConstants.OBJ_FILE_DIRECTORY) || lastSegment.equals(GoConstants.TEST_FILE_DIRECTORY)))
					{
						deleteFolder(file, true);
					}
				}
				return resource instanceof IContainer;
			}
		}, IResource.DEPTH_INFINITE, false);
		
		project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
	}

	private boolean deleteFolder(File f, boolean justContents) {
		if (!f.exists()) {
			return false;
		}
		if (f.isDirectory()) {
			String[] children = f.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteFolder(new File(f, children[i]), false);
				if (!success) {
					return false;
				}
			}
			if (!justContents) {
				f.delete();
			}
		} else {
			f.delete();
		}
		return true;
	}

	
	public static boolean dependenciesExist(IProject project,
			String[] dependencies) {
		if (project != null){
			for (String dependency : dependencies) {
				IResource member = project.findMember(dependency);
				if (member==null || !member.getRawLocation().toFile().exists()){
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

}
