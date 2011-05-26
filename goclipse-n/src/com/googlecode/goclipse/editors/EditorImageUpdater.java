package com.googlecode.goclipse.editors;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.services.IDisposable;

import com.googlecode.goclipse.Activator;

/**
 * This class listens for marker changes to the editor's underlying IFile, and
 * updates the editor's title image with the appropriate icon decorations.
 */
public class EditorImageUpdater implements IDisposable {
	private GoEditor editor;
	
	private Image defaultImage;
	private Image warningImage;
	private Image errorImage;
	
	private IResourceChangeListener resourceChangeListener;
	
	/**
	 * Create a new EditorImageUpdater.
	 * 
	 * @param editor
	 */
	public EditorImageUpdater(GoEditor editor) {
		this.editor = editor;
		
		defaultImage = editor.getTitleImage();
		
		warningImage = Activator.getImage(new DecorationOverlayIcon(defaultImage, 
			Activator.getImageDescriptor("icons/warning_co.gif"), IDecoration.BOTTOM_LEFT));
		errorImage = Activator.getImage(new DecorationOverlayIcon(defaultImage, 
				Activator.getImageDescriptor("icons/error_co.gif"), IDecoration.BOTTOM_LEFT));
		
		installResourceChangeListener(editor);
	}

	private void installResourceChangeListener(GoEditor editor) {
		IEditorInput input = editor.getEditorInput();
		
		IResource resource = (IResource)input.getAdapter(IResource.class);
		
		if (resource != null) {
			resourceChangeListener = new SingleResourceListener(resource);
			ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener);
			
			updateResourceIcon(resource);
		}
	}

	public void dispose() {
		if (resourceChangeListener != null) {
			ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceChangeListener);
			resourceChangeListener = null;
		}
	}
	
	private void updateResourceIcon(IResource resource) {
		try {
			int severity = resource.findMaxProblemSeverity(IMarker.PROBLEM, true, IResource.DEPTH_ONE);
			
			if (severity == IMarker.SEVERITY_ERROR) {
				updateIcon(errorImage);
			} else if (severity == IMarker.SEVERITY_WARNING) {
				updateIcon(warningImage);
			} else {
				updateIcon(defaultImage);
			}
		} catch (CoreException ce) {
			// ignore
		}
	}
	
	private void updateIcon(final Image icon) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				editor.setTitleImage(icon);
			}
		});
	}

	private class SingleResourceListener implements IResourceChangeListener {
		IResource targetResource;
		
		public SingleResourceListener(IResource targetResource) {
			this.targetResource = targetResource;
		}

		@Override
		public void resourceChanged(IResourceChangeEvent event) {
			if (event.getDelta() != null) {
				IResourceDelta delta = event.getDelta().findMember(targetResource.getFullPath());
				
				if (delta != null) {
					if (delta.getMarkerDeltas().length > 0) {
						updateResourceIcon(targetResource);
					}
				}
			}
		}
		
	}
	
}
