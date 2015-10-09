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


import static melnorme.lang.ide.ui.preferences.PreferencesMessages.LABEL_ConfigureWorkspaceSettings;
import static melnorme.lang.ide.ui.preferences.PreferencesMessages.LABEL_UseProjectSpecificSettings;
import static melnorme.lang.ide.ui.utils.ControlUtils.createOpenPreferencesDialogLink;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.osgi.service.prefs.BackingStoreException;

import melnorme.lang.ide.core.utils.prefs.IProjectPreference;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.preferences.common.IPreferencesWidgetComponent;
import melnorme.util.swt.components.AbstractComponent;
import melnorme.util.swt.components.AbstractComponentExt;
import melnorme.util.swt.components.fields.CheckBoxField;

public abstract class ProjectAndPreferencesBlock extends AbstractComponent implements IPreferencesWidgetComponent {
	
	protected final IProject project;
	protected final IProjectPreference<Boolean> useProjectSettingsPref;
	
	protected final CheckBoxField useProjectSettingsField = new CheckBoxField(LABEL_UseProjectSpecificSettings);
	
	public ProjectAndPreferencesBlock(IProject project, IProjectPreference<Boolean> useProjectSettingsPref) {
		super();
		this.project = project;
		this.useProjectSettingsPref = useProjectSettingsPref;
		
		useProjectSettingsField.addValueChangedListener2(
			() -> getParentSettingsBlock().setEnabled(useProjectSettingsField.getFieldValue()));
	}
	
	public abstract AbstractComponentExt getParentSettingsBlock();
	
	@Override
	public int getPreferredLayoutColumns() {
		return 2;
	}
	
	@Override
	protected void createContents(Composite topControl) {
		useProjectSettingsField.createComponent(topControl, 
			GridDataFactory.fillDefaults().create());
		
		String prefPageId = getWorkspacePrefPageId();
		Link link = createOpenPreferencesDialogLink(topControl, prefPageId, LABEL_ConfigureWorkspaceSettings);
		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(link);
		
		getParentSettingsBlock().createComponent(topControl, gdFillDefaults().span(2, 1).grab(true, false).create());
	}
	
	protected String getWorkspacePrefPageId() {
		return LangUIPlugin.PLUGIN_ID + ".PreferencePages.Root";
	}
	
	@Override
	protected void updateComponentFromInput() {
		useProjectSettingsField.setFieldValue(useProjectSettingsPref.getStoredValue(project));
		updateComponentFromInput_do();
	}
	
	protected abstract void updateComponentFromInput_do();
	
	@Override
	public void loadDefaults() {
		useProjectSettingsField.setFieldValue(useProjectSettingsPref.getDefault());
		loadDefaults_do();
	}
	
	protected abstract void loadDefaults_do();
	
	@Override
	public boolean saveSettings() {
		try {
			useProjectSettingsPref.setValue(project, useProjectSettingsField.getBooleanFieldValue());
			saveSettings_do();
		} catch(BackingStoreException e) {
			return false;
		}
		return true;
	}
	
	protected abstract void saveSettings_do() throws BackingStoreException ;
	
}