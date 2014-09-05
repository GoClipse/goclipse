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
package com.googlecode.goclipse.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.debug.core.DebugPlugin;

import melnorme.lang.ide.core.utils.prefs.BooleanPreference;
import melnorme.lang.ide.core.utils.prefs.IntPreference;
import melnorme.lang.ide.core.utils.prefs.StringPreference;


public interface GoProjectPrefConstants {
	
	StringPreference GO_BUILD_EXTRA_OPTIONS = 
			new StringPreference("projectPrefs.build_extra_options", "-gcflags \"-N -l\"");
	
	public static class GO_BUILD_EXTRA_OPTIONS_Helper {
		
		public static String[] getExtraCommands(IProject project) {
			String extraOptionsString = GO_BUILD_EXTRA_OPTIONS.get(project);
			return DebugPlugin.parseArguments(extraOptionsString);
		}
		
	}
	
	BooleanPreference ENABLE_AUTO_UNIT_TEST = 
		new BooleanPreference("projectPrefs.auto_unit_test.enable" , false);
	StringPreference AUTO_UNIT_TEST_REGEX = 
		new StringPreference("projectPrefs.auto_unit_test.regex", "TestAuto[A-Za-z0-9_]*");
	IntPreference AUTO_UNIT_TEST_MAX_TIME = 
		new IntPreference("projectPrefs.auto_unit_test.max_time", 5000);
	
}