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

import melnorme.lang.ide.ui.preferences.LangRootPreferencePage;
import melnorme.lang.ide.ui.preferences.LangSDKConfigBlock;

public class GoPreferencePage extends LangRootPreferencePage {
	
	public GoPreferencePage() {
		super();
		
		setDescription("GoClipse v" + getVersionText());
	}
	
	protected static String getVersionText() {
		Version version = GoCore.getDefault().getBundle().getVersion();
		
		return version.getMajor() + "." + version.getMinor() + "." + version.getMicro();
	}
	
	
	@Override
	protected LangSDKConfigBlock init_createLangSDKConfigBlock2() {
		return doCreateLangSDKConfigBlock();
	}
	
	@Override
	protected LangSDKConfigBlock doCreateLangSDKConfigBlock() {
		GoSDKConfigBlock goSDKBlock = new GoSDKConfigBlock();
		
		bindToPreference(GoEnvironmentPrefs.GO_ROOT, goSDKBlock.goRootField);
		bindToPreference(GoEnvironmentPrefs.GO_OS, goSDKBlock.goOSField.asStringProperty());
		bindToPreference(GoEnvironmentPrefs.GO_ARCH, goSDKBlock.goArchField.asStringProperty());
		
		bindToPreference(GoEnvironmentPrefs.COMPILER_PATH, goSDKBlock.goToolPath);
		bindToPreference(GoEnvironmentPrefs.FORMATTER_PATH, goSDKBlock.goFmtPath);
		bindToPreference(GoEnvironmentPrefs.DOCUMENTOR_PATH, goSDKBlock.goDocPath);
		
		bindToPreference(GoEnvironmentPrefs.GO_PATH, goSDKBlock.goPathField); /*FIXME: BUG here*/
		
		return goSDKBlock;
	}
	
	@Override
	protected String getHelpId() {
		return null;
	}
	
}