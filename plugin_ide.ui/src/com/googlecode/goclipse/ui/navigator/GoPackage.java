package com.googlecode.goclipse.ui.navigator;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import com.googlecode.goclipse.Activator;

/**
 * 
 * @author Mihailo Vasiljevic
 * 
 */
public class GoPackage extends GoSourceContainer implements IGoPackage {

	private IGoSourceFolder goSource;
	private IGoPackage parent;
	private String appendPath;

	public GoPackage(IGoSourceFolder goSource, IGoPackage parent, String appendPath,
			IFolder folder) {
		super(folder);
		this.goSource = goSource;
		this.parent = parent;
		this.appendPath = appendPath;
	}
	
	@Override
	protected Object createChild(IFolder childFolder) {
		String appendPath = "";
		IFolder current = childFolder;
		try {
			while(true) {
				IResource[] grandSons = current.members();
				if (grandSons.length == 1 && grandSons[0] instanceof IFolder) {
					appendPath = appendPath + current.getName() + "/";
					current = (IFolder)grandSons[0];
				} else {
					break;
				}
			}
		} catch(CoreException e) {
			Activator.logError(e);
		}
		return new GoPackage(goSource, this, appendPath, current);
	}

	@Override
	public String getName() {
		return appendPath + getFolder().getName();
	}

	@Override
	public IGoSourceFolder getGoSource() {
		return goSource;
	}

	@Override
	public Object getParent() {
		if(parent != null) {
			return parent;
		}
		return goSource;
	}

	@Override
	public boolean equals(Object otherObject) {
		if (otherObject instanceof GoPackage) {
			if (otherObject == this) {
				return true;
			}
			GoPackage other = (GoPackage) otherObject;
			return getFolder().equals(other.getFolder());
		}
		return false;
	}
	
}
