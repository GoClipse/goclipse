/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui.preferences;

import org.osgi.framework.Version;

import com.googlecode.goclipse.core.GoCorePlugin;

import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesBlockPrefPage;
import melnorme.lang.ide.ui.preferences.common.PreferencesPageContext;

public class GoPreferencePage extends AbstractPreferencesBlockPrefPage {
	
	public GoPreferencePage() {
		super();
		
		setDescription("GoClipse v" + getVersionText());
	}
	
	protected static String getVersionText() {
		Version version = GoCorePlugin.getDefault().getBundle().getVersion();
		
		return version.getMajor() + "." + version.getMinor() + "." + version.getMicro();
	}
	
	@Override
	protected GoSDKConfigBlock init_createPreferencesBlock(PreferencesPageContext prefContext) {
		return new GoSDKConfigBlock(prefContext);
	}
	
	@Override
	protected String getHelpId() {
		return null;
	}
	
}