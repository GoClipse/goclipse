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
package melnorme.lang.ide.core;

import melnorme.lang.ide.core.utils.PreferencesLookupHelper;

import org.eclipse.core.resources.IProject;

public class CorePreferencesLookup extends PreferencesLookupHelper {

	public CorePreferencesLookup() {
		this(null);
	}
	
	public CorePreferencesLookup(IProject project) {
		super(LangCore.PLUGIN_ID, project);
	}
	
	public String getString(String key) {
		return getString(key, "");
	}
	
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}
	
}