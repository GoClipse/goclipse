/*******************************************************************************
 * Copyright (c) 2014, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.gocode.preferences;

import com.googlecode.goclipse.gocode.GocodePlugin;

import melnorme.lang.ide.core.utils.prefs.BooleanPreference;
import melnorme.lang.ide.core.utils.prefs.StringPreference;

public interface GocodePreferences {

	public static final BooleanPreference AUTO_START_SERVER =
			new BooleanPreference(GocodePlugin.PLUGIN_ID, "auto_start_server", true);
	public static final StringPreference GOCODE_PATH =
			new StringPreference(GocodePlugin.PLUGIN_ID, "gocode_path", "");
	public static final BooleanPreference GOCODE_CONSOLE_ENABLE =
			new BooleanPreference(GocodePlugin.PLUGIN_ID, "gocode_console_enable", true);
			
	public static final boolean USE_TCP = true;
	
}