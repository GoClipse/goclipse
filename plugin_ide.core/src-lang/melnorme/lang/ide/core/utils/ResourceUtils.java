/*******************************************************************************
 * Copyright (c) 2014, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.utils;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Optional;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.tooling.LocationHandle;
import melnorme.lang.tooling.common.ops.CommonOperation;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.ThrowingRunnable;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.ownership.IOwner;

public class ResourceUtils {
	
	public static org.eclipse.core.runtime.Path epath(Location loc) {
		return new org.eclipse.core.runtime.Path(loc.path.toString());
	}
	
	public static org.eclipse.core.runtime.Path epath(Path path) {
		return new org.eclipse.core.runtime.Path(path.toString());
	}
	
	public static Location loc(IPath location) {
		return Location.create_fromValid(location.toFile().toPath());
	}
	
	public static URI toUri(IPath path) {
		if(path == null) {
			return null;
		}
		return path.toFile().toURI();
	}
	
	public static Location getResourceLocation(IResource resource) {
		IPath location = resource.getLocation();
		if(location == null) {
			return null;
		}
		return loc(location);
	}
	
	/* -----------------  ----------------- */ 
	
	/** Convenience method to get the workspace root. */
	public static IWorkspaceRoot getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}
	
	/** Convenience method to get the workspace. */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}
	
	public static Location getWorkspaceLocation() {
		IPath location_ = getWorkspaceRoot().getLocation();
		return Location.fromAbsolutePath(location_.toFile().toPath());
	}
	
	/* ----------------- getLocation ----------------- */
	
	public static Location getProjectLocation2(IProject project) throws CommonException {
		return getLocation(project);
	}
	
	public static Location getLocation(IResource resource) throws CommonException {
		IPath location = resource.getLocation();
		if(location == null) {
			throw new CommonException("Invalid resource location: " + resource.getLocationURI());
		}
		return Location.create(location.toFile().toPath());
	}
	
	public static ProjectLocation locationHandle(IProject project) throws CommonException {
		return new ProjectLocation(project);
	}
	
	public static class ProjectLocation extends LocationHandle {
		
		protected final IProject project;
		
		public ProjectLocation(IProject project) throws CommonException {
			super(ResourceUtils.getProjectLocation2(project));
			this.project = assertNotNull(project);
		}
		
		public IProject getProject() {
			return project;
		}
		
	}
	
	/* -----------------  ----------------- */
	
	public static IProject getProject(String name) {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(name);
	}
	
	public static IProject getProjectAt(Location location) {
		IContainer[] containers = getWorkspaceRoot().findContainersForLocationURI(location.toUri());
		for (IContainer container : containers) {
			if(container instanceof IProject) {
				return (IProject) container;
			}
		}
		return null;
	}
	
	public static IProject getProjectAt(LocationHandle location) {
		if(location instanceof ProjectLocation) {
			ProjectLocation projectLocation = (ProjectLocation) location;
			return projectLocation.getProject();
		}
		return getProjectAt(location.getLocation());
	}
	
	public static IProject getProjectFromMemberLocation(Location fileLocation) {
		IFile[] files = getWorkspaceRoot().findFilesForLocationURI(fileLocation.toUri());
		
		for (IFile file : files) {
			IProject project = file.getProject();
			if(project.exists() && project.isOpen()) {
				return project;
			}
		}
		
		return null;
	}
	
	public static IProject getProjectFromMemberLocation(Optional<Location> location) {
		if(location.isPresent()) {
			return getProjectFromMemberLocation(location.get()); 
		}
		return null;
	}
	
	/* ----------------- Operations ----------------- */
	
	public static void runCoreOperation2(ISchedulingRule rule, IProgressMonitor pm, CommonOperation operation)
			throws CoreException, CommonException, OperationCancellation {
		assertNotNull(rule);
		
		IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
			@Override
			public void run(IProgressMonitor monitor) throws CoreException {
				try {
					operation.execute(EclipseUtils.om(monitor));
				} catch(CommonException e) {
					throw new CommonException_CEWrapper(e);
				} catch(OperationCancellation e) {
					throw new CancellationException_CEWrapper(e);
				}
			}
		};
		
		try {
			ResourceUtils.getWorkspace().run(runnable, rule, IWorkspace.AVOID_UPDATE, pm);
		} catch(CommonException_CEWrapper cew) {
			throw cew.wrapped;
		} catch(CancellationException_CEWrapper cew) {
			throw cew.wrapped;
		}
	}
	
	public static void runOperation(ISchedulingRule rule, IOperationMonitor om, CommonOperation operation)
			throws CommonException, OperationCancellation {
		try {
			runCoreOperation2(rule, EclipseUtils.pm(om), operation);
		} catch(CoreException e) {
			throw LangCore.createCommonException(e);
		}
	}
	
	public static void runOperationUnderResource(IResource resource, IOperationMonitor om, CommonOperation operation)
			throws CommonException, OperationCancellation {
		runOperation(resource, om, operation);
		
		refresh(resource, om);
	}
	
	public static void runWorkspaceOperation(IProgressMonitor pm, CommonOperation operation) 
			throws OperationCancellation, CommonException {
		try {
			runCoreOperation2(getWorkspaceRoot(), pm, operation);
		} catch(CoreException e) {
			throw LangCore.createCommonException(e);
		}
	}
	
	public static void runProjectOperation(IOperationMonitor om, IProject project, CommonOperation operation) 
			throws OperationCancellation, CommonException {
		runOperation(project, om, operation);
	}
	
	public static interface CoreOperation extends CommonOperation {
		
		@Override
		default void execute(IOperationMonitor om) throws CommonException, OperationCancellation {
			try {
				execute_do(EclipseUtils.pm(om));
			} catch(CoreException e) {
				throw LangCore.createCommonException(e);
			}
		}
		
		public abstract void execute_do(IProgressMonitor pm) 
				throws CoreException, CommonException, OperationCancellation;
		
	}
	
	public static void runOperation(IRunnableContext context, CommonOperation op, boolean isCancellable) 
			throws OperationCancellation, CommonException {
		try {
			context.run(true, isCancellable, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					try {
						op.execute(EclipseUtils.om(monitor));
					} catch(CommonException e) {
						throw new InvocationTargetException(e);
					} catch(OperationCancellation | OperationCanceledException e) {
						throw new InterruptedException();
					}
				}
			});
		} catch (InterruptedException e) {
			throw new OperationCancellation();
		} catch (InvocationTargetException e) {
			Throwable targetException = e.getTargetException();
			if(targetException instanceof InterruptedException) {
				throw new OperationCancellation();
			}
			if(targetException instanceof CommonException) {
				throw (CommonException) targetException;
			}
			if(targetException instanceof RuntimeException) {
				throw (RuntimeException) targetException;
			}
			
			assertFail(); // Should not be possible
		}
	}
	
	public static void runOperationInWorkspace(IRunnableContext context, boolean isCancellable, CommonOperation op) 
			throws OperationCancellation, CommonException {
		
		runOperation(context, (pm) -> {
			ResourceUtils.runOperation(ResourceUtils.getWorkspaceRoot(), pm, op);
		}, isCancellable);
	}
	
	/* -----------------  Direct resource operations  ----------------- */
	
	public static void refresh(IResource resource, IOperationMonitor om) throws CommonException {
		try {
			resource.refreshLocal(IResource.DEPTH_INFINITE, EclipseUtils.pm(om));
		} catch(CoreException e) {
			throw LangCore.createCommonException(e);
		}
	}
	
	/* -----------------  ----------------- */
	
	@SuppressWarnings("serial")
	protected static class CommonException_CEWrapper extends CoreException {
		
		public final CommonException wrapped;
		
		public CommonException_CEWrapper(CommonException wrapped) {
			super(LangCore.createErrorStatus("Error: ", wrapped));
			this.wrapped = assertNotNull(wrapped);
		}
	}
	
	@SuppressWarnings("serial")
	protected static class CancellationException_CEWrapper extends CoreException {
		
		public final OperationCancellation wrapped;
		
		public CancellationException_CEWrapper(OperationCancellation wrapped) {
			super(LangCore.createErrorStatus("Error: ", wrapped));
			this.wrapped = assertNotNull(wrapped);
		}
	}
	
	/* ----------------- File read/write ----------------- */
	
	public static void writeToFile(IFile file, InputStream is, IProgressMonitor pm) throws CoreException {
		if(file.exists()) {
			file.setContents(is, false, false, pm);
		} else {
			file.create(is, false, null);
		}
	}
	
	public static void writeStringToFile(IFile file, String contents, IProgressMonitor pm) throws CoreException {
		writeStringToFile(file, contents, StringUtil.UTF8, pm);
	}
	
	public static void writeStringToFile(IFile file, String contents, Charset charset, IProgressMonitor pm) 
			throws CoreException {
		writeToFile(file, new ByteArrayInputStream(contents.getBytes(charset)), pm);
	}
	
	/* -----------------  ----------------- */
	
	public static void createFolder(IFolder folder, boolean force, IProgressMonitor monitor) 
			throws CoreException {
		createFolder(folder, force, true, monitor);
	}
	
	public static void createFolder(IFolder folder, boolean force, boolean local, IProgressMonitor monitor) 
			throws CoreException {
		if (folder.exists()) {
			return;
		}
		
		IContainer parent = folder.getParent();
		if (parent instanceof IFolder) {
			createFolder((IFolder) parent, force, local, monitor);
		}
		folder.create(force, local, monitor);
	}
	
	public static String printDelta(IResourceDelta delta) {
		StringBuilder sb = new StringBuilder();
		doPrintDelta(delta, "  ", sb);
		return sb.toString();
	}
	
	protected static void doPrintDelta(IResourceDelta delta, String indent, StringBuilder sb) {
		sb.append(indent);
		sb.append(delta);
		
		sb.append(" " + deltaKindToString(delta) + "\n");
		for (IResourceDelta childDelta : delta.getAffectedChildren()) {
			doPrintDelta(childDelta, indent + "  ", sb);
		}
	}
	
	protected static String deltaKindToString(IResourceDelta delta) {
		switch (delta.getKind()) {
		case IResourceDelta.ADDED: return "+";
		case IResourceDelta.REMOVED: return "-";
		case IResourceDelta.CHANGED: return "*";
		case IResourceDelta.ADDED_PHANTOM: return "%+%";
		case IResourceDelta.REMOVED_PHANTOM: return "%-%";
		default:
			throw assertFail();
		}
	}
	
	public static IProject createAndOpenProject(String name, boolean overwrite) throws CoreException {
		return createAndOpenProject(name, overwrite, null);
	}
	
	public static IProject createAndOpenProject(String name, boolean overwrite, IProgressMonitor pm)
			throws CoreException {
		return createAndOpenProject(name, null, overwrite, pm);
	}
	
	public static IProject createAndOpenProject(String name, IPath location, boolean overwrite, IProgressMonitor pm)
			throws CoreException {
		IProject project = EclipseUtils.getWorkspaceRoot().getProject(name);
		if(overwrite && project.exists()) {
			project.delete(true, pm);
		}
		
		IProjectDescription projectDesc = project.getWorkspace().newProjectDescription(project.getName());
		if(location != null) {
			IPath workspaceLocation = project.getWorkspace().getRoot().getLocation();
			if(location.equals(workspaceLocation.append(project.getName()))) {
				// Location is the default project location, so don't set it in description, this causes problems.
				// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=481508
			} else {
				projectDesc.setLocation(location);
			}
		}
		project.create(projectDesc, pm);
		
		project.open(pm);
		return project;
	}
	
	public static void tryDeleteProject(String projectName) throws CoreException {
		IProject project = EclipseUtils.getWorkspaceRoot().getProject(projectName);
		tryDeleteProject(project);
	}
	
	public static void tryDeleteProject(IProject project) throws CoreException {
		try {
			project.delete(true, null);
		} catch (CoreException ce) {
			if(project.exists()) {
				throw ce;
			}
		}
	}
	
	/* ----------------- file Buffer utils ----------------- */
	
	public static ITextFileBuffer getTextFileBuffer(ITextFileBufferManager fbm, Location fileLoc) {
		return getTextFileBuffer(fbm, fileLoc.path);
	}
	
	public static ITextFileBuffer getTextFileBuffer(ITextFileBufferManager fbm, Path filePath) {
		
		ITextFileBuffer fileBuffer;
		fileBuffer = fbm.getTextFileBuffer(epath(filePath), LocationKind.NORMALIZE);
		if(fileBuffer != null) {
			return fileBuffer;
		}
		
		// Could be an external file, try alternative API:
		fileBuffer = fbm.getFileStoreTextFileBuffer(FileBuffers.getFileStoreAtLocation(epath(filePath)));
		if(fileBuffer != null) {
			return fileBuffer;
		}
		
		// Fall back, try LocationKind.LOCATION
		fileBuffer = fbm.getTextFileBuffer(epath(filePath), LocationKind.LOCATION);
		if(fileBuffer != null) {
			return fileBuffer;
		}
		
		return null;
	}
	
	public static void connectResourceListener(IResourceChangeListener listener, 
			ThrowingRunnable<CoreException> initialUpdate, ISchedulingRule opRule, IOwner owner) {
		try {
			getWorkspace().run(new IWorkspaceRunnable() {
				@Override
				public void run(IProgressMonitor monitor) throws CoreException {
					getWorkspace().addResourceChangeListener(listener, IResourceChangeEvent.POST_CHANGE);
					initialUpdate.run();
				}
			}, opRule, IWorkspace.AVOID_UPDATE, null);
			
		} catch (CoreException ce) {
			LangCore.logStatus(ce);
			// This really should not happen, but still try to recover by registering the listener.
			getWorkspace().addResourceChangeListener(listener, IResourceChangeEvent.POST_CHANGE);
		}
		owner.bind(() -> ResourcesPlugin.getWorkspace().removeResourceChangeListener(listener));
	}
	
}