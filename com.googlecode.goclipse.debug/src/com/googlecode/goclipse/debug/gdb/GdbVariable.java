package com.googlecode.goclipse.debug.gdb;

/**
 * 
 * 
 * @author devoncarew
 */
public class GdbVariable {
	private String name;
	private String value;

	public GdbVariable(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

}
