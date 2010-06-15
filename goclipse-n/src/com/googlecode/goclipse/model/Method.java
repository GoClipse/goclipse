package com.googlecode.goclipse.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Method implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private ArrayList<Parameter> parameters = new ArrayList<Parameter>();
	private int line;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

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
