package com.googlecode.goclipse.navigator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;

import com.googlecode.goclipse.ui.GoPluginImages;
import com.googlecode.goclipse.ui.navigator.elements.GoPathElement;

/**
 * An IWorkbenchAdapter implementation for GoPathElement objects.
 */
class GoPathElementWorkbenchAdapter implements IWorkbenchAdapter {
	private Object[] EMPTY = new Object[0];
	
	@Override
	public Object[] getChildren(Object object) {
		return EMPTY;
	}
	
	@Override
	public ImageDescriptor getImageDescriptor(Object object) {
		return GoPluginImages.NAVIGATOR_GO_PATH_ELEMENT.getDescriptor();
	}
	
	@Override
	public String getLabel(Object object) {
		GoPathElement pathElement = (GoPathElement) object;
		
		return pathElement.getName();
	}
	
	@Override
	public Object getParent(Object object) {
		return null;
	}
	
}