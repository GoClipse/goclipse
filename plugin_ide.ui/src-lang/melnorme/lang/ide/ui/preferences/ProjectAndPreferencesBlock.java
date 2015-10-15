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
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.osgi.service.prefs.BackingStoreException;

import melnorme.lang.ide.core.utils.prefs.IProjectPreference;
import melnorme.lang.ide.core.utils.prefs.PreferenceHelper;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.preferences.common.IPreferencesWidget;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.AbstractComponent;
import melnorme.util.swt.components.AbstractComponentExt;
import melnorme.util.swt.components.fields.CheckBoxField;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.fields.IProperty;

public abstract class ProjectAndPreferencesBlock extends AbstractComponent implements IPreferencesWidget {
	
	protected final ArrayList2<PreferencePropertyBinding<?>> fieldBindings = new ArrayList2<>();
	
	protected final IProject project;
	protected final IProjectPreference<Boolean> useProjectSettingsPref;
	
	protected final CheckBoxField useProjectSettingsField = new CheckBoxField(LABEL_UseProjectSpecificSettings);
	protected final AbstractComponentExt projectSettingsBlock;
	
	public ProjectAndPreferencesBlock(IProject project, IProjectPreference<Boolean> useProjectSettingsPref) {
		this.project = project;
		this.useProjectSettingsPref = useProjectSettingsPref;
		
		this.projectSettingsBlock = init_createProjectSettingsBlock2();
		
		useProjectSettingsField.addValueChangedListener2(
			() -> projectSettingsBlock.setEnabled(useProjectSettingsField.getFieldValue()));
		
		bindToProjectPref(useProjectSettingsField, useProjectSettingsPref);
	}
	
	protected abstract AbstractComponentExt init_createProjectSettingsBlock2();
	
	public <T> void bindToProjectPref(IProperty<T> field, PreferenceHelper<T> preference) {
		bindToProjectPref(field, preference.getProjectPreference());
	}
	
	public <T> void bindToProjectPref(IProperty<T> field, IProjectPreference<T> preference) {
		assertTrue(preference == useProjectSettingsPref ||
			preference.getEnableProjectSettingPref() == useProjectSettingsPref);
		
		fieldBindings.add(new PreferencePropertyBinding<>(field, preference, project));
	}
	
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
		
		projectSettingsBlock.createComponent(topControl, gdFillDefaults().span(2, 1).grab(true, false).create());
	}
	
	protected String getWorkspacePrefPageId() {
		return LangUIPlugin.PLUGIN_ID + ".PreferencePages.Root";
	}
	
	@Override
	protected void updateComponentFromInput() {
		fieldBindings.forEach(binding -> binding.updateFieldFromInput());
	}
	
	@Override
	public void loadDefaults() {
		fieldBindings.forEach(binding -> binding.loadDefaults());
	}
	
	@Override
	public boolean saveSettings() {
		try {
			for(PreferencePropertyBinding<?> binding : fieldBindings) {
				binding.saveSettings();
			}
			return true;
		} catch(BackingStoreException e) {
			return false;
		}
	}
	
	public static class PreferencePropertyBinding<T> {
		
		protected final IProperty<T> property;
		protected final IProjectPreference<T> preference;
		protected final IProject project;
		
		public PreferencePropertyBinding(IProperty<T> property, IProjectPreference<T> preference, IProject project) {
			this.property = property;
			this.preference = preference;
			this.project = project;
		}
		
		public void updateFieldFromInput() {
			property.setValue(preference.getStoredValue(project));
		}
		
		public void loadDefaults() {
			property.setValue(preference.getGlobalPreference().get());
		}
		
		public void saveSettings() throws BackingStoreException {
			preference.setValue(project, property.getValue());
		}
	}
	
}