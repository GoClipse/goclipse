package com.googlecode.goclipse.ui.navigator;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IProject;

public class GoExternalFolder extends GoExternalContainer implements IGoSourceFolder {

	private IProject parent; 
	private String name;
	
	public GoExternalFolder(IProject parent, IFileStore store, String name) {
		super(store);
		this.parent = parent;
		this.name = name;
	}
	
	@Override
	protected Object createChild(IFileStore childStore) {
		return new GoExternalPackage(this, null, "", childStore);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object getParent() {
		return parent;
	}
	
	@Override
	public boolean equals(Object otherObject) {
		if (otherObject instanceof GoExternalFolder) {
			if (otherObject == this) {
				return true;
			}
			GoExternalFolder other = (GoExternalFolder) otherObject;
			return getFileStore().equals(other.getFileStore());
		}
		return false;
	}
	
}
