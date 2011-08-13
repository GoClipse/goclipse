package com.googlecode.goclipse.go.lang.model;

import org.eclipse.swt.graphics.Image;

import com.googlecode.goclipse.Activator;

public class Package extends Node{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Image getImage() {
		return Activator.getImage("icons/package.gif");
	}
	
}
