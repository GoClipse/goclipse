package com.googlecode.goclipse.ui.navigator;

import org.eclipse.core.resources.IFolder;

/**
 * 
 * @author Mihailo Vasiljevic
 * 
 */
public class GoSourceFolder extends GoSourceContainer implements IGoSourceFolder {

	public GoSourceFolder(IFolder folder) {
		super(folder);
	}

	@Override
	public Object getParent() {
		return getFolder().getProject();
	}

	@Override
	protected Object createChild(IFolder childFolder) {
		return new GoPackage(this, null, "", childFolder);
	}

	@Override
	public String getName() {
		return getFolder().getName();
	}
	
	@Override
	public boolean equals(Object otherObject) {
		if (otherObject instanceof GoSourceFolder) {
			if (otherObject == this) {
				return true;
			}
			GoSourceFolder other = (GoSourceFolder) otherObject;
			return getFolder().equals(other.getFolder());
		}
		return false;
	}

}
