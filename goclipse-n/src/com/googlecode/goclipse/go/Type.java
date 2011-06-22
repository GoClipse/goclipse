package com.googlecode.goclipse.go;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author steel
 *
 */
public class Type {
	
	public static enum TypeClass{UNKNOWN, ALIAS, STRUCT, INTERFACE}
	
	public Package 		         pkg;
	public String 		         displayName;
	public String 		         name;
	public boolean               alias;
	public TypeClass             typeClass     = TypeClass.UNKNOWN;
	public Set<Func>             functions     = new HashSet<Func>();	
	public Set<Var>              variables     = new HashSet<Var>();
	public Set<UnresolvedType>   embeddedTypes = new HashSet<UnresolvedType>();
	
	/**
	 * @param packageName
	 * @param displayName
	 */
	public Type(Package pkg, String displayName){
		this.pkg         = pkg;
		this.displayName = displayName;
	}
	
	public void addVar(Var var){
		variables.add(var);
	}
	
	public void addEmbeddedTypes(UnresolvedType type){
		embeddedTypes.add(type);
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<Func> getFunctions(){
		return functions;
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<Var> getVariables(){
		return variables;
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<UnresolvedType> getEmbeddedTypes(){
		return embeddedTypes;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the typeClass
	 */
	public TypeClass getTypeClass() {
		return typeClass;
	}

	/**
	 * @param typeClass the typeClass to set
	 */
	public void setTypeClass(TypeClass typeClass) {
		this.typeClass = typeClass;
	}

	/**
	 * @return the alias
	 */
	public boolean isAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(boolean alias) {
		this.alias = alias;
	}
}
