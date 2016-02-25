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
package melnorme.lang.ide.core.operations;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.prefs.BooleanPreference;
import melnorme.lang.ide.core.utils.prefs.IProjectPreference;
import melnorme.lang.ide.core.utils.prefs.StringPreference;

public interface ToolchainPreferences {
	
	IProjectPreference<Boolean> USE_PROJECT_SETTINGS = new BooleanPreference(
		"toolchain_prefs.use_project_settings", false).getProjectPreference();
	
	public static final IProjectPreference<String> SDK_PATH2 = new StringPreference(LangCore.PLUGIN_ID, "sdk_path", "", 
		USE_PROJECT_SETTINGS).getProjectPreference();
	
	public static final BooleanPreference FORMAT_ON_SAVE =
			new BooleanPreference(LangCore.PLUGIN_ID, "format_onSave", false, USE_PROJECT_SETTINGS);
	
	public static final BooleanPreference AUTO_START_DAEMON =
			new BooleanPreference("auto_start_daemon", true);
	public static final StringPreference DAEMON_PATH =
			new StringPreference("daemon_path", "");
	
	/* -----------------  ----------------- */
	
	public static final IProjectPreference<Boolean> PROJ_AUTO_BUILD_DISABLED = new BooleanPreference( 
		"prj_auto_build_disabled", false).getProjectPreference();
	
}