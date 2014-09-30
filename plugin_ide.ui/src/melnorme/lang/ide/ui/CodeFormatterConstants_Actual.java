/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui;

import melnorme.lang.ide.core.utils.prefs.IntPreference;
import melnorme.lang.ide.core.utils.prefs.StringPreference;
import melnorme.utilbox.misc.MiscUtil;


public interface CodeFormatterConstants_Actual {
	
	public static final String QUALIFIER = LangUIPlugin.PLUGIN_ID;
	
	public static final String TAB = "CodeFormatterConstants.TAB";
	public static final String SPACES = "CodeFormatterConstants.SPACE";
	public static final String MIXED = "CodeFormatterConstants.MIXED";
	
	
	StringPreference FORMATTER_INDENT_MODE = 
			new StringPreference(QUALIFIER, "CodeFormatterConstants.FORMATTER_TAB_CHAR", TAB);
	IntPreference FORMATTER_TAB_SIZE = 
			new IntPreference(QUALIFIER, "CodeFormatterConstants.FORMATTER_TAB_SIZE", 4);
	IntPreference FORMATTER_INDENTATION_SPACES_SIZE = 
			new IntPreference(QUALIFIER, "CodeFormatterConstants.FORMATTER_INDENTATION_SIZE", 4);
	
	class Helper {
		
		public static void initDefaults() {
			// Ensure all default values are initialized, in case prefs are accessed by means other
			// than by referencing the constants above 
			MiscUtil.loadClass(LangAutoEditPreferenceConstants_Actual.class);
		}
		
	}
	
}