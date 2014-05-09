package com.googlecode.goclipse.go.lang.model;

import java.util.ArrayList;

public class Function extends Node {

	private static final long serialVersionUID = 1L;
	
	private ArrayList<Var> parameters = new ArrayList<Var>();

	@Override
	public ENodeKind getNodeKind() {
		return ENodeKind.FUNCTION;
	}
	
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
	
}
