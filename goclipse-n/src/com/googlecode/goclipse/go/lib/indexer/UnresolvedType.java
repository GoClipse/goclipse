package com.googlecode.goclipse.go.lib.indexer;

/**
 * @author steel
 */
public class UnresolvedType extends Type{

	/**
	 * 
	 * @param displayName
	 */
	public UnresolvedType(String displayName) {
		super(Package.UNDETERMINED_PKG, displayName);
	}
	
}
