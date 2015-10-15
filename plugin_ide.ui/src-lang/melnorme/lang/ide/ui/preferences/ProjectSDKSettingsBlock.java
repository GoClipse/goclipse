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
package melnorme.lang.ide.ui.preferences;

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.core.utils.prefs.IProjectPreference;

public abstract class ProjectSDKSettingsBlock extends ProjectAndPreferencesBlock {
	
	public ProjectSDKSettingsBlock(IProject project, IProjectPreference<Boolean> useProjectSettingsPref) {
		super(project, useProjectSettingsPref);
	}
	
	@Override
	protected AbstractPreferencesBlockExt init_createProjectSettingsBlock2() {
		return init_createLangSDKBlock();
	}
	
	protected abstract LangSDKConfigBlock init_createLangSDKBlock();
	
}