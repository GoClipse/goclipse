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

import com.googlecode.goclipse.core.GoCore;

import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesBlockPrefPage;

public class GoPreferencePage extends AbstractPreferencesBlockPrefPage {
	
	public GoPreferencePage() {
		super();
		
		setDescription("GoClipse v" + getVersionText());
	}
	
	protected static String getVersionText() {
		Version version = GoCore.getDefault().getBundle().getVersion();
		
		return version.getMajor() + "." + version.getMinor() + "." + version.getMicro();
	}
	
	@Override
	protected GoSDKConfigBlock init_createPreferencesBlock() {
		return new GoSDKConfigBlock(null);
	}
	
	@Override
	protected String getHelpId() {
		return null;
	}
	
}