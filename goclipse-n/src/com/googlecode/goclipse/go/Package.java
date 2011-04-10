package com.googlecode.goclipse.go;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author steel
 */
public class Package implements ReferencialEntity {

	public static final Package UNDETERMINED_PKG = new Package("UNDETERMINED");
	
	private String 	  displayName;
	private long   	  lastModified;
	private Set<Type> publicTypes 		= new HashSet<Type>();
	private Set<Type> privateTypes		= new HashSet<Type>();
	private Set<Type> interfaces		= new HashSet<Type>();
	private Set<Func> publicFunctions	= new HashSet<Func>();
	private Set<Func> privateFunctions  = new HashSet<Func>();
	
	/**
	 * 
	 */
	public Package(String displayName){
		this.displayName = displayName;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public String getInsertionName() {
		return displayName;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getLastModified() {
		return lastModified;
	}
	
	/**
	 * 
	 * @param type
	 */
	public void addPublicType(Type type){
		publicTypes.add(type);
	}

	/**
	 * 
	 * @return
	 */
	public Set<Type> getPublicTypes() {
		return publicTypes;
	}

	/**
	 * 
	 * @return
	 */
	public void setPublicTypes(Set<Type> publicTypes) {
		this.publicTypes = publicTypes;
	}

	/**
	 * 
	 * @return
	 */
	public Set<Type> getPrivateTypes() {
		return privateTypes;
	}

	/**
	 * 
	 * @return
	 */
	public void setPrivateTypes(Set<Type> privateTypes) {
		this.privateTypes = privateTypes;
	}

	/**
	 * 
	 * @return
	 */
	public Set<Type> getInterfaces() {
		return interfaces;
	}

	/**
	 * 
	 * @return
	 */
	public void setInterfaces(Set<Type> interfaces) {
		this.interfaces = interfaces;
	}

	/**
	 * 
	 * @return
	 */
	public Set<Func> getPublicFunctions() {
		return publicFunctions;
	}

	/**
	 * 
	 * @return
	 */
	public void setPublicFunctions(Set<Func> publicFunctions) {
		this.publicFunctions = publicFunctions;
	}

	/**
	 * 
	 * @return
	 */
	public Set<Func> getPrivateFunctions() {
		return privateFunctions;
	}

	/**
	 * 
	 * @return
	 */
	public void setPrivateFunctions(Set<Func> privateFunctions) {
		this.privateFunctions = privateFunctions;
	}

	/**
	 * 
	 * @return
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * 
	 * @return
	 */
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
}
