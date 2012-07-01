package com.googlecode.goclipse.navigator;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.googlecode.goclipse.builder.GoNature;

/**
 * A filter to remove "_obj" folders from the navigator view.
 */
public class NavigatorObjViewerFilter extends ViewerFilter {

  /**
   * Create a new NavigatorObjViewerFilter.
   */
  public NavigatorObjViewerFilter() {

  }

  @Override
  public boolean select(Viewer viewer, Object parentElement, Object element) {
    try {
      if (element instanceof IFolder) {
        IFolder folder = (IFolder) element;
        IProject project = folder.getProject();

        if (project.hasNature(GoNature.NATURE_ID)) {
          return !folder.getName().equals("_obj");
        }
      }
    } catch (CoreException e) {
      
    }
    
    return true;
  }

}
