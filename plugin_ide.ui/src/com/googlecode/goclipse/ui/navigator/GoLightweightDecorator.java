package com.googlecode.goclipse.ui.navigator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.builder.GoNature;
import com.googlecode.goclipse.ui.GoPluginImages;

/**
 * A lightweight decorator for Go problem decorations.
 */
public class GoLightweightDecorator implements ILightweightLabelDecorator {

	public GoLightweightDecorator() {
		
	}
	
	@Override
	public void addListener(ILabelProviderListener listener) {

	}

	@Override
	public void removeListener(ILabelProviderListener listener) {

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void decorate(Object element, IDecoration decoration) {
		if (element instanceof IFile) {
			IFile file = (IFile)element;
			
			if ("go".equals(file.getFileExtension()) && isGoProject(file.getProject())) {
				ImageDescriptor overlayImageDescriptor = getDecorationForResource(file);
				
				if (overlayImageDescriptor != null) {
					decoration.addOverlay(overlayImageDescriptor, IDecoration.BOTTOM_LEFT);
				}
			}
		} else if (element instanceof IGoSourceContainer) {
			IGoSourceContainer sourceContainer = (IGoSourceContainer)element;
			
			ImageDescriptor overlayImageDescriptor = getDecorationForResource(sourceContainer.getFolder());
			
			if (overlayImageDescriptor != null) {
				decoration.addOverlay(overlayImageDescriptor, IDecoration.BOTTOM_LEFT);
			}
		}
	}
	
	@Override
	public void dispose() {

	}

	private boolean isGoProject(IProject project) {
		try {
			return project.hasNature(GoNature.NATURE_ID);
		} catch (CoreException e) {
			return false;
		}
	}

	private ImageDescriptor getDecorationForResource(IResource resource) {
		try {
			int severity = resource.findMaxProblemSeverity(
				IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);

			if (severity == IMarker.SEVERITY_ERROR) {
				return GoPluginImages.OVERLAYS_ERROR;
			} else if (severity == IMarker.SEVERITY_ERROR) {
				return GoPluginImages.OVERLAYS_WARNING;
			} else {
				return null;
			}
		} catch (CoreException ce) {
			// ignore

			return null;
		}
	}

}
