/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.preferences.common;


import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.ui.dialogs.AbstractLangPropertyPage2;
import melnorme.lang.ide.ui.preferences.ProjectSDKSettingsBlock;

public abstract class AbstractProjectToolchainSettingsPage extends AbstractLangPropertyPage2<ProjectSDKSettingsBlock> {
	
	public AbstractProjectToolchainSettingsPage() {
		super();
	}
	
	@Override
	protected abstract ProjectSDKSettingsBlock createProjectConfigWidget(IProject project);
	
	@Override
	public boolean performOk() {
		return getPreferencesWidget().saveSettings();
	}
	
	@Override
	protected void performDefaults() {
		getPreferencesWidget().loadDefaults();
	}
	
}