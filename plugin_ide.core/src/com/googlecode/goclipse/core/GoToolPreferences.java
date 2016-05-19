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

import java.nio.file.Path;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.prefs.DerivedValuePreference;
import melnorme.lang.ide.core.utils.prefs.OptionalStringPreference;
import melnorme.lang.utils.validators.LocationOrSinglePathValidator;

public interface GoToolPreferences {
	
	DerivedValuePreference<Path> GO_GURU_Path = new DerivedValuePreference<Path>(LangCore.PLUGIN_ID, 
		"GoToolPreferences.GO_GURU_Path", "", null,  
		(new LocationOrSinglePathValidator("guru path:")).setFileOnly(true));
	
	DerivedValuePreference<Path> GODEF_Path = new DerivedValuePreference<Path>(LangCore.PLUGIN_ID, 
		"GoToolPreferences.godef.Path", "", null, 
		(new LocationOrSinglePathValidator("godef path:")).setFileOnly(true));
	
	DerivedValuePreference<Path> GOFMT_Path = new DerivedValuePreference<Path>(
		new OptionalStringPreference(LangCore.PLUGIN_ID, "GoToolPreferences.GOFMT_Path", null), 
		(new LocationOrSinglePathValidator("gofmt path:")).setFileOnly(true));
	
}