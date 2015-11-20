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

import static melnorme.lang.ide.core.operations.ToolchainPreferences.USE_PROJECT_SETTINGS;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.ToolchainPreferences;
import melnorme.lang.ide.core.utils.prefs.IProjectPreference;
import melnorme.lang.ide.core.utils.prefs.OptionalStringPreference;
import melnorme.lang.ide.core.utils.prefs.StringPreference;

public interface GoEnvironmentPrefs {
	
	IProjectPreference<String> GO_ROOT = new StringPreference(LangCore.PLUGIN_ID, 
		"com.googlecode.goclipse.goroot", "", USE_PROJECT_SETTINGS).getProjectPreference();
	IProjectPreference<String> GO_OS = new StringPreference(LangCore.PLUGIN_ID, 
		"com.googlecode.goclipse.goos", "", USE_PROJECT_SETTINGS).getProjectPreference();
	IProjectPreference<String> GO_ARCH = new StringPreference(LangCore.PLUGIN_ID, 
		"com.googlecode.goclipse.goarch", "", USE_PROJECT_SETTINGS).getProjectPreference();
	IProjectPreference<String> FORMATTER_PATH = new StringPreference(LangCore.PLUGIN_ID, 
		"com.googlecode.goclipse.formatter.path", "", USE_PROJECT_SETTINGS).getProjectPreference();
	IProjectPreference<String> DOCUMENTOR_PATH = new StringPreference(LangCore.PLUGIN_ID, 
		"com.googlecode.goclipse.documentor.path", "", USE_PROJECT_SETTINGS).getProjectPreference();
	
	
	IProjectPreference<String> GO_PATH = new OptionalStringPreference(LangCore.PLUGIN_ID, 
		"com.googlecode.goclipse.gopath", ToolchainPreferences.USE_PROJECT_SETTINGS).getProjectPreference();
	
//	IProjectPreference<Boolean> APPEND_PROJECT_LOC_TO_GOPATH = new BooleanPreference(LangCore.PLUGIN_ID,
//		"append_projloc_gopath", true, ToolchainPreferences.USE_PROJECT_SETTINGS).getProjectPreference();
	
}