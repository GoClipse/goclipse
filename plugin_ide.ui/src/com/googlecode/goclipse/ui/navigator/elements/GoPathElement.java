package com.googlecode.goclipse.ui.navigator.elements;

import java.io.File;

/**
 * A representation of top level root nodes, like GOROOT and GOPATH entries.
 */
public abstract class GoPathElement implements IGoPathElement {
	
	protected String name;
	protected File rootFolder;
	
	public GoPathElement(String name, File rootFolder) {
		this.name = name;
		this.rootFolder = rootFolder;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public File getDirectory() {
		return rootFolder;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public int hashCode() {
		return rootFolder.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null || getClass() != obj.getClass()) {
			return false;
		}
		
		GoPathElement other = (GoPathElement) obj;
		
		return rootFolder.equals(other.rootFolder);
	}
	
}