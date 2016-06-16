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
package com.googlecode.goclipse.ui;

import org.eclipse.jface.preference.IPreferenceStore;

import melnorme.lang.ide.ui.LangUIPreferencesInitializer;

public class GoUIPreferencesInitializer extends LangUIPreferencesInitializer 
	implements GoUIPreferenceConstants {
	
	@Override
	protected void initializeDefaultPreferences_other(IPreferenceStore langUIStore) {
	}
	
}
