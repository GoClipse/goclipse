package LANG_PROJECT_ID.ide.core;

import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.LangNature;

public class LANGUAGE_Nature extends LangNature {
	
	@Override
	protected String getBuilderId() {
		return LangCore_Actual.BUILDER_ID;
	}
	
}