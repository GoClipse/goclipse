/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.preferences.pages;


import melnorme.lang.ide.ui.dialogs.AbstractLangPropertyPage2;
import melnorme.lang.ide.ui.preferences.ProjectBuildConfigurationComponent;

public abstract class BuildConfigurationPropertyPage 
	extends AbstractLangPropertyPage2<ProjectBuildConfigurationComponent> {
	
	public BuildConfigurationPropertyPage() {
		super();
		noDefaultAndApplyButton();
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public void applyData(Object data) {
		if(data instanceof String) {
			String targetName = (String) data;
			getPreferencesWidget().getBuildTargetField().setFieldValue(targetName);
		}
	}
	
	@Override
	public boolean performOk() {
		return getPreferencesWidget().saveSettings();
	}
	
	@Override
	protected void performDefaults() {
		getPreferencesWidget().loadDefaults();
	}
	
}