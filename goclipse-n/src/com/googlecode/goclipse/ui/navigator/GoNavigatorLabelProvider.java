package com.googlecode.goclipse.ui.navigator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

/**
 * 
 * @author Mihailo Vasiljevic
 * 
 */
public class GoNavigatorLabelProvider implements ILabelProvider {

	@Override
	public Image getImage(Object element) {
		if (element instanceof GoSourceFolder) {
			return ImageCache.getInstance().getCachedImage(
					ImageCache.SOURCE_FOLDER_ICON_PATH);
		} else if (element instanceof GoPackage) {
			return ImageCache.getInstance().getCachedImage(
					ImageCache.PACKAGE_ICON_PATH);
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
		}
		return null;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

}
