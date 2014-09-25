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

import melnorme.lang.ide.core.operations.DaemonEnginePreferences;
import melnorme.lang.ide.core.utils.prefs.BooleanPreference;
import melnorme.lang.ide.core.utils.prefs.StringPreference;

public interface GocodePreferences extends DaemonEnginePreferences {

	public static final BooleanPreference AUTO_START_SERVER = DaemonEnginePreferences.AUTO_START_SERVER;
	public static final StringPreference GOCODE_PATH = DaemonEnginePreferences.DAEMON_PATH;
	public static final BooleanPreference GOCODE_CONSOLE_ENABLE = DaemonEnginePreferences.DAEMON_CONSOLE_ENABLE;
	
}