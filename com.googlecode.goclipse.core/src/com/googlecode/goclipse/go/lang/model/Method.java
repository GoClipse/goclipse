package com.googlecode.goclipse.go.lang.model;


public class Method extends Function {

	private static final long serialVersionUID = 1L;
	
	private Var receiver = new Var();
	
	@Override
	public ENodeKind getNodeKind() {
		return ENodeKind.METHOD;
	}
	
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
