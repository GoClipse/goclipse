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

import static melnorme.lang.ide.ui.preferences.PreferencesMessages.USE_PROJECT_SPECIFIC_SETTINGS;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Composite;
import org.osgi.service.prefs.BackingStoreException;

import melnorme.lang.ide.core.utils.prefs.IProjectPreference;
import melnorme.lang.ide.ui.preferences.LangSDKConfigBlock.LanguageSDKLocationGroup;
import melnorme.lang.ide.ui.preferences.common.IPreferencesWidgetComponent;
import melnorme.util.swt.components.AbstractComponent;
import melnorme.util.swt.components.fields.ButtonTextField;
import melnorme.util.swt.components.fields.CheckBoxField;

public class ProjectSDKSettingsBlock extends AbstractComponent implements IPreferencesWidgetComponent {
	
	protected final CheckBoxField useProjectSettingsField = new CheckBoxField(USE_PROJECT_SPECIFIC_SETTINGS);
	protected final LanguageSDKLocationGroup langSDKGroup = new LanguageSDKLocationGroup();
	protected final ButtonTextField sdkLocationField = langSDKGroup.sdkLocationField;
	
	protected final IProject project;
	protected final IProjectPreference<Boolean> useProjectPref;
	protected final IProjectPreference<String> sdkLocationPref;
	
	public ProjectSDKSettingsBlock(IProject project, 
			IProjectPreference<Boolean> useProjectSettings, 
			IProjectPreference<String> sdkLocationPref) {
		this.project = project;
		this.useProjectPref = useProjectSettings;
		this.sdkLocationPref = sdkLocationPref;
		
		useProjectSettingsField.addValueChangedListener2(
			() -> langSDKGroup.setEnabled(useProjectSettingsField.getFieldValue()));
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 1;
	}
	
	@Override
	protected void createContents(Composite topControl) {
		useProjectSettingsField.createComponentInlined(topControl);
		langSDKGroup.createComponent(topControl, gdFillDefaults().grab(true, false).create());
	}
	
	@Override
	protected void updateComponentFromInput() {
		useProjectSettingsField.setFieldValue(useProjectPref.getStoredValue(project));
		sdkLocationField.setFieldValue(sdkLocationPref.getStoredValue(project));
	}
	
	@Override
	public void loadDefaults() {
		useProjectSettingsField.setFieldValue(useProjectPref.getDefault());
		sdkLocationField.setFieldValue(sdkLocationPref.getGlobalPreference().get());
	}
	
	@Override
	public boolean saveSettings() {
		try {
			useProjectPref.setValue(project, useProjectSettingsField.getBooleanFieldValue());
			sdkLocationPref.setValue(project, sdkLocationField.getFieldValue());
		} catch(BackingStoreException e) {
			return false;
		}
		return true;
	}
	
}