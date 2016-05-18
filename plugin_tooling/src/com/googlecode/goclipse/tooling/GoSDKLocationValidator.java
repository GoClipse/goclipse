package com.googlecode.goclipse.tooling;

import melnorme.lang.tooling.toolchain.ops.SDKLocationValidator;

public class GoSDKLocationValidator extends SDKLocationValidator {
	
	public GoSDKLocationValidator() {
		super("GOROOT:");
	}
	
	@Override
	protected String getSDKExecutable_append() {
		return "bin/go"; 
	}
}