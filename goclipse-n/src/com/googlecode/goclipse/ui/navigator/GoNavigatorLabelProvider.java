package com.googlecode.goclipse.ui.navigator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.googlecode.goclipse.Activator;

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
		if (element instanceof GoSourceFolder) {
			return Activator.getImage("icons/source-folder.gif");
		} else if (element instanceof GoPackage) {
			return Activator.getImage("icons/package.gif");
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

}
