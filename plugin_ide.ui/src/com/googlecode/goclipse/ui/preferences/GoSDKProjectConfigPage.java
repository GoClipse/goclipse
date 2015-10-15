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

import org.eclipse.core.resources.IProject;

import com.googlecode.goclipse.core.GoEnvironmentPrefs;

import melnorme.lang.ide.core.operations.ToolchainPreferences;
import melnorme.lang.ide.ui.dialogs.AbstractLangPropertyPage;
import melnorme.lang.ide.ui.preferences.ProjectAndPreferencesBlock;
import melnorme.lang.ide.ui.preferences.common.IPreferencesWidget;

public class GoSDKProjectConfigPage extends AbstractLangPropertyPage {
	
	@Override
	protected IPreferencesWidget createProjectConfigWidget(IProject project) {
		return new GoSDKProjectConfigBlock(project);
	}
	
	protected class GoSDKProjectConfigBlock extends ProjectAndPreferencesBlock {
		
		public GoSDKProjectConfigBlock(IProject project) {
			super(project, ToolchainPreferences.USE_PROJECT_SETTINGS);
		}
		
		@Override
		protected GoSDKConfigBlock init_createProjectSettingsBlock2() {
			/* FIXME: validation*/
			GoSDKConfigBlock goSDKBlock = new GoSDKConfigBlock();
			
			bindToProjectPref(goSDKBlock.goRootField, GoEnvironmentPrefs.GO_ROOT);
			bindToProjectPref(goSDKBlock.goOSField.asStringProperty(), GoEnvironmentPrefs.GO_OS);
			bindToProjectPref(goSDKBlock.goArchField.asStringProperty(), GoEnvironmentPrefs.GO_ARCH);
			
			bindToProjectPref(goSDKBlock.goToolPath, GoEnvironmentPrefs.COMPILER_PATH);
			bindToProjectPref(goSDKBlock.goFmtPath, GoEnvironmentPrefs.FORMATTER_PATH);
			bindToProjectPref(goSDKBlock.goDocPath, GoEnvironmentPrefs.DOCUMENTOR_PATH);
			
			bindToProjectPref(goSDKBlock.goPathField.asEffectiveValueProperty(), GoEnvironmentPrefs.GO_PATH);
			
			return goSDKBlock;
		}
		
	}
	
}