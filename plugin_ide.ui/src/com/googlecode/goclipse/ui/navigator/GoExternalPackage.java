package com.googlecode.goclipse.ui.navigator;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;

import com.googlecode.goclipse.Activator;

public class GoExternalPackage extends GoExternalContainer implements IGoPackage {

	private IGoSourceFolder goSource;
	private IGoPackage parent;
	private String appendPath;
	
	public GoExternalPackage(IGoSourceFolder goSource, IGoPackage parent, String appendPath,
			IFileStore store) {
		super(store);
		this.goSource = goSource;
		this.parent = parent;
		this.appendPath = appendPath;
	}

	@Override
	protected Object createChild(IFileStore childStore) {
		String appendPath = "";
		IFileStore current = childStore;
		try {
			while(true) {
				IFileStore[] grandSons = current.childStores(EFS.NONE, null);
				if (grandSons.length == 1 && grandSons[0].fetchInfo().isDirectory()) {
					appendPath = appendPath + current.fetchInfo().getName() + "/";
					current = grandSons[0];
				} else {
					break;
				}
			}
		} catch(CoreException e) {
			Activator.logError(e);
		}
		return new GoExternalPackage(goSource, this, appendPath, current);
	}
	
	@Override
	public String getName() {
		return appendPath + getFileStore().fetchInfo().getName();
	}

	@Override
	public IGoSourceFolder getGoSource() {
		return goSource;
	}

	@Override
	public IGoPackage getParent() {
		return parent;
	}
	
	@Override
	public boolean equals(Object otherObject) {
		if (otherObject instanceof GoExternalPackage) {
			if (otherObject == this) {
				return true;
			}
			GoExternalPackage other = (GoExternalPackage) otherObject;
			return getFileStore().equals(other.getFileStore());
		}
		return false;
	}
	
}
