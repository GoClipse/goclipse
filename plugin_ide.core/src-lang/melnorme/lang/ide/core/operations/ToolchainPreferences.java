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

import melnorme.lang.ide.core.utils.prefs.BooleanPreference;
import melnorme.lang.ide.core.utils.prefs.StringPreference;

public interface ToolchainPreferences {
	
	public static final StringPreference SDK_PATH = new StringPreference("sdk_path", "");
	
	
	public static final BooleanPreference AUTO_START_DAEMON =
			new BooleanPreference("auto_start_daemon", true);
	public static final StringPreference DAEMON_PATH =
			new StringPreference("daemon_path", "");
	public static final BooleanPreference DAEMON_CONSOLE_ENABLE =
			new BooleanPreference("daemon_console_enable", true);
	
}