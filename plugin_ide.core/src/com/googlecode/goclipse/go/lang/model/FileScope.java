package com.googlecode.goclipse.go.lang.model;

import java.util.ArrayList;

/**
 * @author steel
 */
public class FileScope extends Node{
	
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Import>   imports   = new ArrayList<Import>();
	private ArrayList<Var>      variables = new ArrayList<Var>();
	private ArrayList<Function> functions = new ArrayList<Function>();
	private ArrayList<Method>   methods   = new ArrayList<Method>();
	private ArrayList<Type>     types     = new ArrayList<Type>();
	private int start;
	private int end;

	/**
	 * File scope
	 * @param parent
	 */
	public FileScope(){}
	
	@Override
	public ENodeKind getNodeKind() {
		return ENodeKind.FILESCOPE;
	}
	
	/**
	 * @return the imports
	 */
	public ArrayList<Import> getImports() {
		return imports;
	}
	
	/**
	 * @return the variables
	 */
	public ArrayList<Var> getVariables() {
		return variables;
	}
	
	/**
	 * @return the functions
	 */
	public ArrayList<Function> getFunctions() {
		return functions;
	}
	
	/**
	 * @return the methods
	 */
	public ArrayList<Method> getMethods() {
		return methods;
	}
	
	/**
	 * @return the types
	 */
	public ArrayList<Type> getTypes() {
		return types;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void setVariables(ArrayList<Var> variables) {
		this.variables = variables;
	}
	
	/**
	 * 
	 * @param imp
	 */
	public void addImport(Import imp) {
		this.imports.add(imp);
	}
	
	/**
	 * Could begin to do semantic checking here
	 */
	public void addVariable(Var var){
		this.variables.add(var);
	}
	
	/**
	 * @param func
	 */
	public void addFunction(Function func){
		functions.add(func);
	}
	
	/**
	 * @param method
	 */
	public void addMethod(Method method){
		methods.add(method);
	}
	
	/**
	 * @param type
	 */
	public void addType(Type type){
		types.add(type);
	}
	
	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @return the end
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(int end) {
		this.end = end;
	}
}
