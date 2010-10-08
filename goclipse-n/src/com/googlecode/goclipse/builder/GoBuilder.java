package com.googlecode.goclipse.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.SysUtils;
import com.googlecode.goclipse.preferences.GoPreferencePage;
import com.googlecode.goclipse.preferences.PreferenceConstants;
import com.googlecode.goclipse.preferences.PreferenceInitializer;

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
			if (resource instanceof IFile && resource.getName().endsWith(".go")) {
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
			if (kind == FULL_BUILD) {
				fullBuild(monitor);
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
				compiler = new GoCompiler(dependencyManager);
			}
		}
	}

	protected void fullBuild(final IProgressMonitor pmonitor)
			throws CoreException {
		SysUtils.debug("fullBuild");
		SubMonitor monitor = SubMonitor.convert(pmonitor, 250);

		CollectResourceVisitor crv = new CollectResourceVisitor();
		getProject().accept(crv);
		monitor.worked(20);

		List<IResource> toCompile = new ArrayList<IResource>();
		toCompile.addAll(crv.getCollectedResources()); // full build means
														// everything should be
														// compiled
		dependencyManager.clearState(monitor.newChild(10));
		compiler.remove(toCompile, getProject(), monitor.newChild(100));
		dependencyManager.buildDep(toCompile, monitor.newChild(40));
		List<String> allPaths = dependencyManager.getDirectDep(toCompile, null, true);
		compiler.compile(allPaths, getProject(), monitor.newChild(100));
		SysUtils.debug("fullBuild - done");
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
		List<String> tc = dependencyManager.getDirectDep(toRemove, null, false);
		compiler.remove(crdv.getRemoved(), getProject(), monitor.newChild(50));
		dependencyManager.removeDep(toRemove, monitor.newChild(10));

		// compile
		List<IResource> toCompile = new ArrayList<IResource>();
		toCompile.addAll(crdv.getAdded());
		toCompile.addAll(crdv.getChanged());
		dependencyManager.buildDep(toCompile, monitor.newChild(10));
		List<String> allPaths = dependencyManager.getDirectDep(toCompile, tc, true);
		compiler.compile(allPaths, getProject(), monitor.newChild(100));
		SysUtils.debug("incrementalBuild - done");
	}

	

	

	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		SysUtils.debug("cleaning project");
		getProject().deleteMarkers(IMarker.PROBLEM, false,
				IResource.DEPTH_INFINITE);
		String outPath = Environment.INSTANCE.getOutputFolder(getProject());
		File outFolder = new File(getProject().getLocation().append(outPath).toOSString());
		if (outFolder.exists()) {
			deleteFolder(outFolder, true);
		}
		getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
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

}
