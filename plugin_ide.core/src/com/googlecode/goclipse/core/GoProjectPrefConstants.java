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

import melnorme.lang.ide.core.utils.prefs.BooleanPreference;
import melnorme.lang.ide.core.utils.prefs.IntPreference;
import melnorme.lang.ide.core.utils.prefs.ProgramArgumentsPreference;
import melnorme.lang.ide.core.utils.prefs.StringPreference;


public interface GoProjectPrefConstants {
	
	ProgramArgumentsPreference GO_BUILD_EXTRA_OPTIONS = 
			new ProgramArgumentsPreference("projectPrefs.build_extra_options", "-gcflags \"-N -l\"");
	
	
	BooleanPreference ENABLE_AUTO_UNIT_TEST = 
		new BooleanPreference("projectPrefs.auto_unit_test.enable" , false);
	StringPreference AUTO_UNIT_TEST_REGEX = 
		new StringPreference("projectPrefs.auto_unit_test.regex", "TestAuto[A-Za-z0-9_]*");
	IntPreference AUTO_UNIT_TEST_MAX_TIME = 
		new IntPreference("projectPrefs.auto_unit_test.max_time", 5000);

        StringPreference GO_BUILD_TARGET =
                new StringPreference("projectPrefs.go_build.target", "./...");
	
}
