package com.googlecode.goclipse.model;

import java.util.ArrayList;

public class Method extends Node {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Parameter> parameters = new ArrayList<Parameter>();

	/**
	 * @return the parameters
	 */
	public ArrayList<Parameter> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters(ArrayList<Parameter> parameters) {
		this.parameters = parameters;
	}

}
