package com.googlecode.goclipse.core;

import com.googlecode.goclipse.core.tools.GoBuilder;

import melnorme.lang.ide.core.LangNature;

public class GoNature extends LangNature {
	
	@Override
	protected String getBuilderId() {
		return GoBuilder.BUILDER_ID;
	}
	
}