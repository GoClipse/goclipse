package com.googlecode.goclipse.ui.navigator;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IContributorResourceAdapter;

/**
 * 
 * @author Mihailo Vasiljevic
 * 
 */
public class GoSourceFolder implements IGoSourceFolder, IAdaptable,
		IContributorResourceAdapter {

	private IFolder folder;

	public GoSourceFolder(IFolder folder) {
		this.folder = folder;
	}

	@Override
	public IProject getProject() {
		return folder.getProject();
	}

	@Override
	public IFolder getFolder() {
		return folder;
	}

	@Override
	public String getName() {
		return folder.getName();
	}

	@Override
	public boolean equals(Object otherObject) {
		if (otherObject instanceof GoSourceFolder) {
			if (otherObject == this) {
				return true;
			}
			GoSourceFolder other = (GoSourceFolder) otherObject;
			return folder.equals(other.getFolder());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return folder.hashCode();
	}

	@Override
	public Object getAdapter(@SuppressWarnings("unchecked") Class adapter) {
		if (adapter == IResource.class) {
			return folder;
		}
		if (adapter == IContributorResourceAdapter.class) {
			return this;
		}
		Object result = folder.getAdapter(adapter);
		return result;
	}

	@Override
	public IResource getAdaptedResource(IAdaptable adaptable) {
		return folder;
	}

}
