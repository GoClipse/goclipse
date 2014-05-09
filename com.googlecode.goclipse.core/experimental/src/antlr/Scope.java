package com.googlecode.goclipse.go.antlr;

import java.util.ArrayList;

/**
 * 
 * @author steel
 */
public class Scope {
	
	enum Type{UNKNOWN, FILE, STRUCT, INTERFACE, FUNCTION}
	
	public String name       = "";
	public int    startLine  = -1;
	public int    start 	 = -1;
	public int    stopLine   = -1;
	public int    stop 	     = -1;
	public Type   type       = Type.UNKNOWN;
	
	public ArrayList<Var>    variables = new ArrayList<Var>();
	public ArrayList<Func>   functions = new ArrayList<Func>();
	public ArrayList<Func>   methods   = new ArrayList<Func>();
	
	public Scope parent;
	public ArrayList<Scope> children = new ArrayList<Scope>();
}
