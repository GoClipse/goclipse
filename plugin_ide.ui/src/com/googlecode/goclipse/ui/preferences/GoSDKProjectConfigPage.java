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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.core.operations.ToolchainPreferences;
import melnorme.lang.ide.ui.dialogs.AbstractLangPropertyPage;
import melnorme.lang.ide.ui.preferences.AbstractPreferencesBlockExt;
import melnorme.lang.ide.ui.preferences.LangSDKConfigBlock;
import melnorme.lang.ide.ui.preferences.ProjectSDKSettingsBlock;
import melnorme.lang.ide.ui.preferences.common.IPreferencesWidget;

public class GoSDKProjectConfigPage extends AbstractLangPropertyPage {
	
	@Override
	protected IPreferencesWidget createProjectConfigWidget(IProject project) {
		return new GoSDKProjectConfigBlock(project);
	}
	
	protected class GoSDKProjectConfigBlock extends ProjectSDKSettingsBlock {
		
		public GoSDKProjectConfigBlock(IProject project) {
			super(project, ToolchainPreferences.USE_PROJECT_SETTINGS);
		}
		
		@Override
		protected AbstractPreferencesBlockExt init_createProjectSettingsBlock2() {
			return new GoSDKConfigBlock(prefContext);
		}
		
		@Override
		protected LangSDKConfigBlock init_createLangSDKBlock() {
			throw assertFail();
		}
		
	}
	
}