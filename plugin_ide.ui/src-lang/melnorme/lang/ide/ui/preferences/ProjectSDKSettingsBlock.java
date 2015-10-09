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
import org.osgi.service.prefs.BackingStoreException;

import melnorme.lang.ide.core.utils.prefs.IProjectPreference;
import melnorme.util.swt.components.AbstractComponentExt;
import melnorme.util.swt.components.fields.ButtonTextField;

public abstract class ProjectSDKSettingsBlock extends ProjectAndPreferencesBlock {
	
	protected final LangSDKConfigBlock langSDKConfigBlock = init_createSDKLocationGroup();

	protected final ButtonTextField sdkLocationField = langSDKConfigBlock.sdkLocationGroup.sdkLocationField;
	
	protected final IProjectPreference<String> sdkLocationPref;
	
	public ProjectSDKSettingsBlock(IProject project, 
			IProjectPreference<Boolean> useProjectSettingsPref, 
			IProjectPreference<String> sdkLocationPref) {
		super(project, useProjectSettingsPref);
		this.sdkLocationPref = sdkLocationPref;
	}
	
	protected abstract LangSDKConfigBlock init_createSDKLocationGroup();
	
	@Override
	public AbstractComponentExt getParentSettingsBlock() {
		return langSDKConfigBlock;
	}
	
	@Override
	protected void updateComponentFromInput_do() {
		sdkLocationField.setFieldValue(sdkLocationPref.getStoredValue(project));
	}
	
	@Override
	protected void loadDefaults_do() {
		sdkLocationField.setFieldValue(sdkLocationPref.getGlobalPreference().get());
	}
	
	@Override
	protected void saveSettings_do() throws BackingStoreException {
		sdkLocationPref.setValue(project, sdkLocationField.getFieldValue());
	}
	
}