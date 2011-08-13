package com.googlecode.goclipse.go.lib.indexer;

/**
 * Too lazy to complete graph right now
 * TODO typeName should point to actual type
 * @author steel
 */
public class Var {

	private String name;
	private UnresolvedType type;
	private boolean isPointer = false;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the typeName
	 */
	public UnresolvedType getType() {
		return type;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(UnresolvedType type) {
		this.type = type;
	}
	
	public void setPointer(boolean isPointer) {
		this.isPointer = isPointer;
	}
	
	public boolean isPointer() {
		return isPointer;
	}
	
	
}
