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
package com.googlecode.goclipse.core;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.ToolchainPreferences;
import melnorme.lang.ide.core.utils.prefs.OptionalStringPreference;
import melnorme.lang.ide.core.utils.prefs.StringPreference;

public interface GoEnvironmentPrefs {
	
	static StringPreference GO_ROOT = new StringPreference(LangCore.PLUGIN_ID, 
		"com.googlecode.goclipse.goroot", "", ToolchainPreferences.USE_PROJECT_SETTINGS);
	static OptionalStringPreference GO_PATH = new OptionalStringPreference(LangCore.PLUGIN_ID, 
		"com.googlecode.goclipse.gopath", ToolchainPreferences.USE_PROJECT_SETTINGS);
	static StringPreference GO_OS = new StringPreference(LangCore.PLUGIN_ID, 
		"com.googlecode.goclipse.goos", "", ToolchainPreferences.USE_PROJECT_SETTINGS);
	static StringPreference GO_ARCH = new StringPreference(LangCore.PLUGIN_ID, 
		"com.googlecode.goclipse.goarch", "", ToolchainPreferences.USE_PROJECT_SETTINGS);
	
	static StringPreference FORMATTER_PATH = new StringPreference(LangCore.PLUGIN_ID, 
		"com.googlecode.goclipse.formatter.path", "", ToolchainPreferences.USE_PROJECT_SETTINGS);
	static StringPreference DOCUMENTOR_PATH = new StringPreference(LangCore.PLUGIN_ID, 
		"com.googlecode.goclipse.documentor.path", "", ToolchainPreferences.USE_PROJECT_SETTINGS);
	
}