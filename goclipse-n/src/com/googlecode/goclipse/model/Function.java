package com.googlecode.goclipse.model;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Image;

import com.googlecode.goclipse.Activator;

public class Function extends Node{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Var> parameters = new ArrayList<Var>();

	/**
	 * @return the parameters
	 */
	public ArrayList<Var> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters(ArrayList<Var> parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * @return the image
	 */
	public Image getImage() {
		//return Activator.getImage("icons/function_co.png");
		return Activator.getImage("icons/function_co.png");
	}
	
}
