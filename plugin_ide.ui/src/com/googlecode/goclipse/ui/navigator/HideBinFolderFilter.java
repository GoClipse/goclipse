package com.googlecode.goclipse.ui.navigator;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.googlecode.goclipse.builder.GoNature;
import com.googlecode.goclipse.core.GoWorkspace;

/**
 * 
 * @author Mihailo Vasiljevic
 * 
 */
public class HideBinFolderFilter extends ViewerFilter {

	public HideBinFolderFilter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {

		try {
			if (element instanceof IFolder) {
				IFolder folder = (IFolder) element;
				IProject project = folder.getProject();

				if (project.hasNature(GoNature.NATURE_ID)) {
					if (folder.getParent().equals(project)) {
						GoWorkspace goWorkspace = new GoWorkspace(project);
						IPath pkgOut = goWorkspace.getPkgFolderRelativePath();
						IPath cmdOut = goWorkspace.getBinFolderRelativePath();
						return !(folder.getProjectRelativePath().isPrefixOf(pkgOut) ||
								folder.getProjectRelativePath().isPrefixOf(cmdOut));
					}
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return true;
	}

}
