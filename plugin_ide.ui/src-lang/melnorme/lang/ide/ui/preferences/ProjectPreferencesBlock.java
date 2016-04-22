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
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.option;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;

import melnorme.lang.ide.core.utils.prefs.IGlobalPreference;
import melnorme.lang.ide.core.utils.prefs.IProjectPreference;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesBlock2;
import melnorme.lang.ide.ui.preferences.common.IPreferencesEditor;
import melnorme.lang.ide.ui.preferences.common.PreferencesPageContext;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.AbstractDisableableWidget;
import melnorme.util.swt.components.fields.CheckBoxField;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.fields.IProperty;

public abstract class ProjectPreferencesBlock extends AbstractPreferencesBlock2 implements IPreferencesEditor {
	
	protected final IProject project;
	protected final IProjectPreference<Boolean> useProjectSettingsPref;
	
	protected final CheckBoxField useProjectSettingsField = new CheckBoxField(LABEL_UseProjectSpecificSettings);
	protected final AbstractDisableableWidget projectSettingsBlock;
	
	public ProjectPreferencesBlock(IProject project, IProjectPreference<Boolean> useProjectSettingsPref) {
		super(new ProjectPreferencesPageContext(project, useProjectSettingsPref));
		this.project = project;
		this.useProjectSettingsPref = useProjectSettingsPref;
		
		this.projectSettingsBlock = init_createProjectSettingsBlock2();
		
		useProjectSettingsField.registerListener(
			(newValue) -> projectSettingsBlock.setEnabled(newValue));
		
		prefContext.bindToPreference(useProjectSettingsField, useProjectSettingsPref);
		
		validation.addStatusField(true, projectSettingsBlock.getStatusField());
	}
	
	protected abstract AbstractDisableableWidget init_createProjectSettingsBlock2();
	
	@Override
	public void doSaveSettings() throws CommonException {
		prefContext.doSaveSettings();
	}
	
	@Override
	public void loadDefaults() {
		prefContext.loadDefaults();
	}
	
	/* -----------------  ----------------- */ 
	
	@Override
	public int getPreferredLayoutColumns() {
		return 2;
	}
	
	@Override
	protected void createContents(Composite topControl) {
		useProjectSettingsField.createComponent(topControl, 
			GridDataFactory.fillDefaults().create());
		
		String prefPageId = getWorkspacePrefPageId();
		Link link = createOpenPreferencesDialogLink(topControl, prefPageId, LABEL_ConfigureWorkspaceSettings, null);
		GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).applyTo(link);
		
		SWTFactoryUtil.createLabel(topControl, SWT.SEPARATOR | SWT.HORIZONTAL, "", 
			GridDataFactory.fillDefaults().span(2, 1).create());
		
		projectSettingsBlock.createComponent(topControl, gdfFillDefaults().span(2, 1).grab(true, false).create());
		
		// Update projectSettingsBlock setEnabled status - because setEnabled only works after the control is created.
		useProjectSettingsField.fireFieldValueChanged();
	}
	
	protected String getWorkspacePrefPageId() {
		return LangUIPlugin.PLUGIN_ID + ".PreferencePages.Root";
	}
	
	/* -----------------  ----------------- */
	
	protected static class ProjectPreferencesPageContext extends PreferencesPageContext {
		
		protected final IProject project; 
		protected final IProjectPreference<Boolean> useProjectSettingsPref;
		
		public ProjectPreferencesPageContext(IProject project, IProjectPreference<Boolean> useProjectSettingsPref) {
			this.project = assertNotNull(project);
			this.useProjectSettingsPref = assertNotNull(useProjectSettingsPref);
		}
		
		@Override
		public <T> IPreferencesEditor getPreferencesBinder(IProperty<T> field, IGlobalPreference<T> globalPref) {
			IProjectPreference<T> pref = globalPref.getProjectPreference();
			assertTrue(pref == useProjectSettingsPref ||
					pref.getEnableProjectSettingPref() == useProjectSettingsPref);
			
			return new PreferencePropertyBinding<T>(field, pref, project);
		}
	}
	
	public static class PreferencePropertyBinding<T> implements IPreferencesEditor {
		
		protected final IProperty<T> property;
		protected final IProjectPreference<T> preference;
		protected final IProject project;
		
		public PreferencePropertyBinding(IProperty<T> property, IProjectPreference<T> preference, IProject project) {
			this.property = property;
			this.preference = preference;
			this.project = project;
			
			property.set(preference.getStoredValue(option(project)));
		}
		
		public void updateFieldFromInput() {
			property.set(preference.getStoredValue(option(project)));
		}
		
		@Override
		public void loadDefaults() {
			property.set(preference.getGlobalPreference().get());
		}
		
		@Override
		public void doSaveSettings() throws CommonException {
			preference.setValue(project, property.get());
		}
	}
	
}