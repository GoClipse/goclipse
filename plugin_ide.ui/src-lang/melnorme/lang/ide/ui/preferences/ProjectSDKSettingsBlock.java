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
import melnorme.util.swt.components.AbstractDisableableWidget;

public abstract class ProjectSDKSettingsBlock extends ProjectPreferencesBlock {
	
	public ProjectSDKSettingsBlock(IProject project, IProjectPreference<Boolean> useProjectSettingsPref) {
		super(project, useProjectSettingsPref);
	}
	
	@Override
	protected abstract AbstractDisableableWidget init_createProjectSettingsBlock2();
	
}