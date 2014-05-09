package com.googlecode.goclipse.go.lang.model;

public class Package extends Node{

	private static final long serialVersionUID = 1L;
	
	@Override
	public ENodeKind getNodeKind() {
		return ENodeKind.PACKAGE;
	}
	
}
