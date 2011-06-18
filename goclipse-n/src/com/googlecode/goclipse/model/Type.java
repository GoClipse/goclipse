package com.googlecode.goclipse.model;

import org.eclipse.swt.graphics.Image;

import com.googlecode.goclipse.Activator;

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
	
	/**
	 * @return the image
	 */
	public Image getImage() {
		//return Activator.getImage("icons/function_co.png");
		return Activator.getImage("icons/struct.png");
	}
	
	
}
