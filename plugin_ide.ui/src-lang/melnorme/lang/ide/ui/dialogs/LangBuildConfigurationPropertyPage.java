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
package melnorme.lang.ide.ui.dialogs;


import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.ui.preferences.LangProjectBuildConfigurationComponent;

public abstract class LangBuildConfigurationPropertyPage extends AbstractLangPropertyPage {
	
	public LangBuildConfigurationPropertyPage() {
		super();
		noDefaultAndApplyButton();
	}
	
	/* -----------------  ----------------- */
	
	@Override
	protected abstract LangProjectBuildConfigurationComponent createProjectConfigComponent(IProject project);
	
	@Override
	public LangProjectBuildConfigurationComponent getSettingsComponent() {
		return (LangProjectBuildConfigurationComponent) super.getSettingsComponent();
	}
	
	@Override
	public void applyData(Object data) {
		if(data instanceof String) {
			String targetName = (String) data;
			getSettingsComponent().getBuildTargetField().setFieldValue(targetName);
		}
	}
	
}