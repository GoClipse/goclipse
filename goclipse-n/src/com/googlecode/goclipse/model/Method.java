package com.googlecode.goclipse.model;

import org.eclipse.swt.graphics.Image;

import com.googlecode.goclipse.Activator;


public class Method extends Function {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Var receiver = new Var();

	/**
	 * @return the receiver
	 */
	public Var getReceiver() {
		return receiver;
	}

	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(Var receiver) {
		this.receiver = receiver;
	}

	/**
	 * @return the image
	 */
	public Image getImage() {
		return Activator.getImage("icons/public_co.gif");
	}
	
}
