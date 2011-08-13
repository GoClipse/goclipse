package com.googlecode.goclipse.go.lang.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.eclipse.core.resources.IResource;

public class GoSourceMetadata implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<IResource> imports = new ArrayList<IResource>();
	private ArrayList<IResource> exports = new ArrayList<IResource>();
	private ArrayList<Method> pubMethods = new ArrayList<Method>();
	private ArrayList<Member> pubMember  = new ArrayList<Member>();

	/**
	 * @return the imports
	 */
	public ArrayList<IResource> getImports() {
		return imports;
	}

	/**
	 * @param imports
	 *            the imports to set
	 */
	public void setImports(ArrayList<IResource> imports) {
		this.imports = imports;
	}

	/**
	 * @return the exports
	 */
	public ArrayList<IResource> getExports() {
		return exports;
	}

	/**
	 * @param exports
	 *            the exports to set
	 */
	public void setExports(ArrayList<IResource> exports) {
		this.exports = exports;
	}

	/**
	 * @return the pubMethods
	 */
	public ArrayList<Method> getPubMethods() {
		return pubMethods;
	}

	/**
	 * @param pubMethods
	 *            the pubMethods to set
	 */
	public void setPubMethods(ArrayList<Method> pubMethods) {
		this.pubMethods = pubMethods;
	}

	/**
	 * @return the pubMember
	 */
	public ArrayList<Member> getPubMember() {
		return pubMember;
	}

	/**
	 * @param pubMember
	 *            the pubMember to set
	 */
	public void setPubMember(ArrayList<Member> pubMember) {
		this.pubMember = pubMember;
	}

}
