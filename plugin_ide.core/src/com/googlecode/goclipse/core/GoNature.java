package com.googlecode.goclipse.core;

import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.LangNature;

public class GoNature extends LangNature {
	
	@Override
	protected String getBuilderId() {
		return LangCore_Actual.BUILDER_ID;
	}
	
}