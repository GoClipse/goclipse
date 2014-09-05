package com.googlecode.goclipse.ui.navigator;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.ui.GoPluginImages;

/**
 * 
 * 
 * @author Mihailo Vasiljevic
 */
public class GoNavigatorLabelProvider extends LabelProvider {

	public GoNavigatorLabelProvider() {

	}
	
	@Override
	public Image getImage(Object element) {
		if (element instanceof GoExternalFolder) {
			return GoPluginImages.NAVIGATOR_GO_PATH_ELEMENT.getImage();
		} else if (element instanceof IGoSourceFolder) {
			return GoPluginImages.ELEMENT_SOURCE_FOLDER.getImage();
		} else if (element instanceof IGoPackage) {
			return GoPluginImages.ELEMENT_PACKAGE.getImage();
		} else if (element instanceof IFileStore) {
			IFileStore store = (IFileStore) element;
			String name = store.fetchInfo().getName();
			try {
				IEditorDescriptor descriptor = IDE.getEditorDescriptor(name);
				if (descriptor != null) {
					return descriptor.getImageDescriptor().createImage();
				}
			} catch (PartInitException e) {
				Activator.logError(e);	
			}
			return PlatformUI.getWorkbench().getSharedImages()
					.getImageDescriptor(ISharedImages.IMG_OBJ_FILE).createImage();
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof IProject) {
			return ((IProject) element).getName();
		} else if (element instanceof IGoElement) {
			return ((IGoElement) element).getName();
		} else if (element instanceof IResource) {
			return ((IResource) element).getName();
		} else if (element instanceof IFileStore) {
			return ((IFileStore) element).fetchInfo().getName();
		}
		
		return null;
	}

}
