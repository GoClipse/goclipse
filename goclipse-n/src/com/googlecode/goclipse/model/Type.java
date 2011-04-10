package com.googlecode.goclipse.model;

public class Type extends Node{

	private TypeClass typeClass = TypeClass.UNKNOWN;

	/**
	 * @return the typeClass
	 */
	public TypeClass getTypeClass() {
		return typeClass;
	}

	/**
	 * @param typeClass the typeClass to set
	 */
	public void setTypeClass(TypeClass typeClass) {
		this.typeClass = typeClass;
	}
	
	
}
