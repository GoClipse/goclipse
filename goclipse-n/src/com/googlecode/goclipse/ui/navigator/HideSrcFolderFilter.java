package com.googlecode.goclipse.ui.navigator;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.googlecode.goclipse.builder.GoNature;

/**
 * 
 * @author Mihailo Vasiljevic
 * 
 */
public class HideSrcFolderFilter extends ViewerFilter {

	public HideSrcFolderFilter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {

		try {
			if (element instanceof IFolder) {
				IFolder folder = (IFolder) element;
				IProject project = folder.getProject();

				if (project.hasNature(GoNature.NATURE_ID)) {
					if (folder.getParent().equals(project)
							&& folder.getName().equals("src")) { // FIXME
						return false;
					}
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return true;
	}

}
