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

import org.eclipse.swt.graphics.RGB;

import melnorme.lang.ide.core.utils.prefs.BooleanPreference;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.OperationsConsolePrefDefaults_Actual;
import melnorme.lang.ide.ui.text.coloring.ThemedColorPreference;

public interface ToolsConsolePrefs extends OperationsConsolePrefDefaults_Actual {
	
	BooleanPreference ACTIVATE_ON_ERROR_MESSAGES = new BooleanPreference(
		LangUIPlugin.PLUGIN_ID, "console.do_activate2", true); 
	
	ThemedColorPreference INFO_COLOR = new ThemedColorPreference("console.info_color", 
		INFO_COLOR_Default, INFO_COLOR_DefaultDark); 
	ThemedColorPreference STDERR_COLOR = new ThemedColorPreference("console.stderr_color", 
		new RGB(255, 0, 0), new RGB(255,0,0));
	ThemedColorPreference STDOUT_COLOR = new ThemedColorPreference("console.stdout_color", 
		new RGB(0, 0, 0), new RGB(230,230,230));
	ThemedColorPreference BACKGROUND_COLOR = new ThemedColorPreference("console.background_color", 
		BACKGROUND_COLOR_Default, BACKGROUND_COLOR_DefaultDark);
	 
}