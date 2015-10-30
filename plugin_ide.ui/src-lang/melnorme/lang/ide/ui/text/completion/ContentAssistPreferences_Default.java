/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.text.completion;

import static melnorme.lang.ide.ui.LangUIPlugin.PLUGIN_ID;
import melnorme.lang.ide.core.utils.prefs.BooleanPreference;
import melnorme.lang.ide.core.utils.prefs.IntPreference;
import melnorme.lang.ide.ui.ContentAssistPreferences;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.utilbox.misc.MiscUtil;


public interface ContentAssistPreferences_Default {
	
	BooleanPreference AUTO_INSERT__SingleProposals = 
			new BooleanPreference(PLUGIN_ID, "AUTO_INSERT__SingleProposals", true);
	BooleanPreference AUTO_INSERT__CommonPrefixes = 
			new BooleanPreference(PLUGIN_ID, "AUTO_INSERT__CommonPrefixes", true);

	BooleanPreference AUTO_ACTIVATE__DotTrigger = 
			new BooleanPreference(LangUIPlugin.PLUGIN_ID, "AUTO_ACTIVATE__DotTrigger", true);
	BooleanPreference AUTO_ACTIVATE__DoubleColonTrigger = 
			new BooleanPreference(LangUIPlugin.PLUGIN_ID, "AUTO_ACTIVATE__DoubleColonTrigger", false);
	BooleanPreference AUTO_ACTIVATE__AlphaNumericTrigger = 
			new BooleanPreference(PLUGIN_ID, "AUTO_ACTIVATE__AlphaNumericTrigger", false);
	IntPreference AUTO_ACTIVATE__Delay = 
			new IntPreference(PLUGIN_ID, "AUTO_ACTIVATE__Delay", 200);
	
	
	BooleanPreference ShowDialogIfContentAssistErrors = 
			new BooleanPreference("daemon_showDialogIfErrors", true);
	
	
	class Helper {

		public static void initDefaults() {
			// Ensure all default values are initialized, in case prefs are accessed by means other
			// than by referencing the constants above 
			MiscUtil.loadClass(ContentAssistPreferences.class);
		}
		
	}
	
}