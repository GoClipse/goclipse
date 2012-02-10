package com.googlecode.goclipse.debug.sourceLookup;

import com.googlecode.goclipse.builder.GoConstants;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.sourcelookup.ISourceContainer;
import org.eclipse.debug.core.sourcelookup.ISourcePathComputerDelegate;
import org.eclipse.debug.core.sourcelookup.containers.FolderSourceContainer;
import org.eclipse.debug.core.sourcelookup.containers.ProjectSourceContainer;
import org.eclipse.debug.core.sourcelookup.containers.WorkspaceSourceContainer;

/**
 * 
 * @author devoncarew
 */
public class GoSourcePathComputerDelegate implements ISourcePathComputerDelegate {

	public GoSourcePathComputerDelegate() {

	}

	@Override
	public ISourceContainer[] computeSourceContainers(ILaunchConfiguration configuration,
		IProgressMonitor monitor) throws CoreException {
		
		String path = configuration.getAttribute(GoConstants.GO_CONF_ATTRIBUTE_PROJECT, (String) null);
		
		ISourceContainer sourceContainer = null;
		
		if (path != null) {
			IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(path));
			
			// TODO: add the src folder here?
			
			if (resource != null) {
				if (resource.getType() == IResource.PROJECT) {
					sourceContainer = new ProjectSourceContainer((IProject) resource, false);
				} else if (resource.getType() == IResource.FOLDER) {
					sourceContainer = new FolderSourceContainer((IContainer)resource, false);
				}
			}
		}
		
		if (sourceContainer == null) {
			sourceContainer = new WorkspaceSourceContainer();
		}
		
		return new ISourceContainer[] { sourceContainer };
	}

}
