package com.googlecode.goclipse.navigator;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.googlecode.goclipse.builder.GoNature;

/**
 * A filter to remove bin and pkg folders from the navigator view.
 */
public class NavigatorPackageViewerFilter extends ViewerFilter {

  /**
   * Create a new NavigatorPackageViewerFilter.
   */
  public NavigatorPackageViewerFilter() {

  }

  @Override
  public boolean select(Viewer viewer, Object parentElement, Object element) {
    try {
      if (element instanceof IFolder) {
        IFolder folder = (IFolder) element;
        IProject project = folder.getProject();

        if (folder.getParent() instanceof IProject && project.hasNature(GoNature.NATURE_ID)) {
          return !(folder.getName().equals("bin") || folder.getName().equals("pkg"));
        }
      }
    } catch (CoreException e) {
      
    }
    
    return true;
  }

}
