/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.tools.console;

import melnorme.lang.ide.core.utils.prefs.BooleanPreference;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.OperationsConsolePrefDefaults_Actual;
import melnorme.lang.ide.ui.preferences.ColorPreference;

public interface ToolsConsolePrefs extends OperationsConsolePrefDefaults_Actual {
	
	BooleanPreference ACTIVATE_ON_ERROR_MESSAGES = 
			new BooleanPreference(LangUIPlugin.PLUGIN_ID, "console.do_activate", false); 
	
	ColorPreference INFO_COLOR = 
			new ColorPreference(LangUIPlugin.PLUGIN_ID, "console.info_color", INFO_COLOR_DEFAULT); 
	ColorPreference STDERR_COLOR = 
			new ColorPreference(LangUIPlugin.PLUGIN_ID, "console.stderr_color", STDERR_COLOR_DEFAULT);
	ColorPreference STDOUT_COLOR = 
			new ColorPreference(LangUIPlugin.PLUGIN_ID, "console.stdout_color", STDOUT_COLOR_DEFAULT);
	ColorPreference BACKGROUND_COLOR = 
			new ColorPreference(LangUIPlugin.PLUGIN_ID, "console.background_color", BACKGROUND_COLOR_DEFAULT);
	
}