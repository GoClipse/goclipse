package com.googlecode.goclipse.go.lang;

public enum GoPathType {
	/**
	 * mean this gopath is set up in os environment
	 */
	OS_Env, 
	
	/**
	 * mean this gopath is set up in goclipse preference
	 */
	Preference,
	
	/**
	 * mean this gopath is set up in project preference
	 */
	Project
}
