package com.googlecode.goclipse.model;

import java.io.Serializable;

public class Parameter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private Type type;
	private boolean isArray = false;
	private boolean isMap = false;
	private boolean isChan = false;
	private boolean isPointer = false;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * @return the isArray
	 */
	public boolean isArray() {
		return isArray;
	}

	/**
	 * @param isArray
	 *            the isArray to set
	 */
	public void setArray(boolean isArray) {
		this.isArray = isArray;
	}

	/**
	 * @return the isMap
	 */
	public boolean isMap() {
		return isMap;
	}

	/**
	 * @param isMap
	 *            the isMap to set
	 */
	public void setMap(boolean isMap) {
		this.isMap = isMap;
	}

	/**
	 * @return the isChan
	 */
	public boolean isChan() {
		return isChan;
	}

	/**
	 * @param isChan
	 *            the isChan to set
	 */
	public void setChan(boolean isChan) {
		this.isChan = isChan;
	}

	/**
	 * @return the isPointer
	 */
	public boolean isPointer() {
		return isPointer;
	}

	/**
	 * @param isPointer
	 *            the isPointer to set
	 */
	public void setPointer(boolean isPointer) {
		this.isPointer = isPointer;
	}

}
