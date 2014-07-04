package com.googlecode.goclipse.go.antlr;

public class Var extends CodeUnit{
	public Type type = new Type();
	public boolean isPointer = false;
	
	@Override
	public String toString() {
		return "Var -> "+super.toString();
	}
}
