package com.googlecode.goclipse.go.lang.model;

import java.util.ArrayList;

public class Scope extends Node {

	private static final long serialVersionUID = 1L;
	
	private Scope parent;
	private ArrayList<Var>      variables = new ArrayList<Var>();
	private ArrayList<Function> functions = new ArrayList<Function>();
	private ArrayList<Method>   methods   = new ArrayList<Method>();
	private ArrayList<Type>     types     = new ArrayList<Type>();
	private ArrayList<Scope>    children  = new ArrayList<Scope>();
	private int start;
	private int end;
	private String textStartedOn = "none";
	
	
	@Override
	public ENodeKind getNodeKind() {
		return ENodeKind.SCOPE;
	}

	/**
	 * Sets parent and adds itself as a child to the parent
	 * @param parent
	 */
	public Scope(Scope parent, String textStartedOn){
		this.parent        = parent;
		this.textStartedOn = textStartedOn;
		if(parent!=null){
			parent.children.add(this);
		}
	}

	/**
	 * @return the children
	 */
	public ArrayList<Scope> getChildren() {
		return children;
	}
	
	/**
	 * @param scope
	 */
	public void addChild(Scope scope){
		children.add(scope);
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
	
	public Scope getParent() {
		return parent;
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
	
	public void print(String indent){
		System.out.println(indent+"-------------------------------------------------<"+textStartedOn+">");
		for(Var var: variables){
			System.out.println(indent+"var    > "+var.getInsertionText()+" : "+var.getLine());
		}
		
		for(Type type: types){
			System.out.println(indent+"type   > "+type.getInsertionText());
		}
		
		for(Method method: methods){
			System.out.println(indent+"method > "+method.getInsertionText());
		}
		
		for(Function func: functions){
			System.out.println(indent+"func   > "+func.getInsertionText());
		}
	}
	
}