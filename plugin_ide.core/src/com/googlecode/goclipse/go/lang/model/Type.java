package com.googlecode.goclipse.go.lang.model;

/**
 * 
 */
public class Type extends Node {
	
	private static final long serialVersionUID = 1L;
	
	private TypeClass typeClass = TypeClass.UNKNOWN;
	

	@Override
	public ENodeKind getNodeKind() {
		return ENodeKind.TYPE;
	}
	
	/**
	 * @return the typeClass
	 */
	public TypeClass getTypeClass() {
		return typeClass;
	}

	/**
	 * @param typeClass
	 *            the typeClass to set
	 */
	public void setTypeClass(TypeClass typeClass) {
		this.typeClass = typeClass;
	}

}
