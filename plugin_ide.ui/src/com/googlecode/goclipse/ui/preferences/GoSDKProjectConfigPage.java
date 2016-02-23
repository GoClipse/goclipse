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

import com.github.rustdt.ide.ui.preferences.AbstractProjectToolchainSettingsPage;

import melnorme.lang.ide.core.operations.ToolchainPreferences;
import melnorme.lang.ide.ui.preferences.ProjectSDKSettingsBlock;

public class GoSDKProjectConfigPage extends AbstractProjectToolchainSettingsPage {
	
	@Override
	protected GoSDKProjectConfigBlock createProjectConfigWidget(IProject project) {
		return new GoSDKProjectConfigBlock(project);
	}
	
	protected class GoSDKProjectConfigBlock extends ProjectSDKSettingsBlock {
		
		public GoSDKProjectConfigBlock(IProject project) {
			super(project, ToolchainPreferences.USE_PROJECT_SETTINGS);
		}
		
		@Override
		protected GoSDKConfigBlock init_createProjectSettingsBlock2() {
			return new GoSDKConfigBlock(prefContext);
		}
		
	}
	
}