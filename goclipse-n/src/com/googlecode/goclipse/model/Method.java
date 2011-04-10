package com.googlecode.goclipse.model;


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

}
