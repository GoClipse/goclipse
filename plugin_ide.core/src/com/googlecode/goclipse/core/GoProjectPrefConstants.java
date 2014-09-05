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

import com.googlecode.goclipse.Environment;

import melnorme.lang.ide.core.utils.prefs.BooleanPreference;
import melnorme.lang.ide.core.utils.prefs.IntPreference;
import melnorme.lang.ide.core.utils.prefs.StringPreference;


public interface GoProjectPrefConstants {
	
	BooleanPreference PROJECT_ENABLE_AUTO_UNIT_TEST = 
		new BooleanPreference("projectPrefs.auto_unit_test.enable" , false);
	StringPreference PROJECT_AUTO_UNIT_TEST_REGEX = 
		new StringPreference("projectPrefs.auto_unit_test.regex", Environment.getAutoUnitTestRegexDefault());
	IntPreference PROJECT_AUTO_UNIT_TEST_MAX_TIME = 
		new IntPreference("projectPrefs.auto_unit_test.max_time", Environment.getAutoUnitTestMaxTimeDefault());
	
}