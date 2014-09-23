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

import melnorme.lang.ide.core.utils.prefs.StringPreference;

public interface GoToolPreferences {
	
	StringPreference GO_ORACLE_Path = new StringPreference("GoToolPreferences.GO_ORACLE_Path", 
		"D:/devel/tools.Go/go-workspace/bin/oracle");
	
}