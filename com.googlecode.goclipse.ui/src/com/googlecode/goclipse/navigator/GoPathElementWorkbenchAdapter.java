package com.googlecode.goclipse.navigator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;

import com.googlecode.goclipse.Activator;

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
    return Activator.getImageDescriptor("icons/jar_l_obj-1.gif");
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
