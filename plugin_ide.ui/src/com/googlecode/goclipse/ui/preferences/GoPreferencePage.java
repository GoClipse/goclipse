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
import com.googlecode.goclipse.core.GoEnvironmentPrefs;

import melnorme.lang.ide.ui.preferences.AbstractPreferencesBlockPrefPage;

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
		GoSDKConfigBlock goSDKBlock = new GoSDKConfigBlock();
		
		bindToPreference2(goSDKBlock.goRootField, GoEnvironmentPrefs.GO_ROOT);
		bindToPreference2(goSDKBlock.goOSField.asStringProperty(), GoEnvironmentPrefs.GO_OS);
		bindToPreference2(goSDKBlock.goArchField.asStringProperty(), GoEnvironmentPrefs.GO_ARCH);
		
		bindToPreference2(goSDKBlock.goToolPath, GoEnvironmentPrefs.COMPILER_PATH);
		bindToPreference2(goSDKBlock.goFmtPath, GoEnvironmentPrefs.FORMATTER_PATH);
		bindToPreference2(goSDKBlock.goDocPath, GoEnvironmentPrefs.DOCUMENTOR_PATH);
		
		bindToPreference2(goSDKBlock.goPathField.asEffectiveValueProperty(), GoEnvironmentPrefs.GO_PATH);
		
		return goSDKBlock;
	}
	
	@Override
	protected String getHelpId() {
		return null;
	}
	
}