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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.IFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import melnorme.lang.ide.core.LangCore;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.StringUtil;

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
	
	public static IProject getProject(String name) {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(name);
	}
	
	public static IProject getProject(Location fileLocation) {
		IFile[] files = getWorkspaceRoot().findFilesForLocationURI(fileLocation.toUri());
		
		for (IFile file : files) {
			IProject project = file.getProject();
			if(project.exists() && project.isOpen()) {
				return project;
			}
		}
		
		return null;
	}
	
	public static Location getProjectLocation(IProject project) throws CoreException {
		try {
			return getProjectLocation2(project);
		} catch (CommonException e) {
			throw LangCore.createCoreException(e);
		}
	}
	
	public static Location getProjectLocation2(IProject project) throws CommonException {
		IPath location = project.getLocation();
		if(location == null) {
			throw new CommonException("Invalid project location: " + project.getLocationURI());
		}
		return Location.create(location.toFile().toPath());
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
	
	@Deprecated
	public static void deleteProject_unchecked(String projectName) {
		IProject project = EclipseUtils.getWorkspaceRoot().getProject(projectName);
		try {
			project.delete(true, null);
		} catch (CoreException e) {
			// Ignore
		}
	}
	
	public static void tryDeleteProject(String projectName) throws CoreException {
		IProject project = EclipseUtils.getWorkspaceRoot().getProject(projectName);
		try {
			project.delete(true, null);
		} catch (CoreException ce) {
			if(project.exists()) {
				throw ce;
			}
		}
	}
	
	/* ----------------- file Buffer utils ----------------- */
	
	public static ITextFileBuffer getTextFileBuffer(Location fileLoc) {
		return getTextFileBuffer(fileLoc.path);
	}
	
	public static ITextFileBuffer getTextFileBuffer(Path filePath) {
		
		ITextFileBufferManager fbm = FileBuffers.getTextFileBufferManager();
		
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
	
	public static Location getFileBufferLocation(IFileBuffer buffer) throws CoreException {
		IFileStore fileStore = buffer.getFileStore();
		if(fileStore == null) {
			throw LangCore.createCoreException("Error in discardWorkingCopy: listener fileStore == null", null);
		}
		Path filePath;
		try {
			filePath = Paths.get(fileStore.toURI());
		} catch (RuntimeException e) {
			throw LangCore.createCoreException("Error converting URI to path.", e);
		}
		
		Location fileLoc;
		try {
			fileLoc = Location.create(filePath);
		} catch (CommonException ce) {
			throw LangCore.createCoreException(ce);
		}
		return fileLoc;
	}
	
}