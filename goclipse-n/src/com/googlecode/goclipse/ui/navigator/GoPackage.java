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
public class GoPackage implements IGoPackage, IAdaptable,
		IContributorResourceAdapter {

	private IGoSourceFolder goSource;
	private IGoPackage parent;

	private IFolder folder;

	public GoPackage(IGoSourceFolder goSource, IGoPackage parent, IFolder folder) {
		this.goSource = goSource;
		this.parent = parent;
		this.folder = folder;
	}

	@Override
	public IFolder getFolder() {
		return folder;
	}

	public IGoSourceFolder getGoSource() {
		return goSource;
	}

	@Override
	public String getName() {
		return folder.getName();
	}

	public IGoPackage getParent() {
		return parent;
	}

	@Override
	public IProject getProject() {
		return folder.getProject();
	}

	@Override
	public boolean equals(Object otherObject) {
		if (otherObject instanceof GoPackage) {
			if (otherObject == this) {
				return true;
			}
			GoPackage other = (GoPackage) otherObject;
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
