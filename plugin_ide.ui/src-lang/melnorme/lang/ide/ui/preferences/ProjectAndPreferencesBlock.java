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
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.preferences.common.IPreferencesWidgetComponent;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.AbstractComponent;
import melnorme.util.swt.components.AbstractComponentExt;
import melnorme.util.swt.components.fields.CheckBoxField;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.fields.IDomainField;

public abstract class ProjectAndPreferencesBlock extends AbstractComponent implements IPreferencesWidgetComponent {
	
	protected final IProject project;
	protected final IProjectPreference<Boolean> useProjectSettingsPref;
	protected final ArrayList2<PreferenceFieldBinding<?>> fieldBindings = new ArrayList2<>();
	
	protected final CheckBoxField useProjectSettingsField = new CheckBoxField(LABEL_UseProjectSpecificSettings);
	
	public ProjectAndPreferencesBlock(IProject project, IProjectPreference<Boolean> useProjectSettingsPref) {
		super();
		this.project = project;
		this.useProjectSettingsPref = useProjectSettingsPref;
		
		useProjectSettingsField.addValueChangedListener2(
			() -> getParentSettingsBlock().setEnabled(useProjectSettingsField.getFieldValue()));
		
		addFieldBinding(useProjectSettingsField, useProjectSettingsPref);
	}
	
	public abstract AbstractComponentExt getParentSettingsBlock();
	
	public <T> void addFieldBinding(IDomainField<T> field, IProjectPreference<T> preference) {
		assertTrue(preference == useProjectSettingsPref ||
			preference.getEnableProjectSettingPref() == useProjectSettingsPref);
		
		fieldBindings.add(new PreferenceFieldBinding<>(field, preference, project));
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
		
		getParentSettingsBlock().createComponent(topControl, gdFillDefaults().span(2, 1).grab(true, false).create());
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
			for(PreferenceFieldBinding<?> binding : fieldBindings) {
				binding.saveSettings();
			}
			return true;
		} catch(BackingStoreException e) {
			return false;
		}
	}
	
	public static class PreferenceFieldBinding<T> {
		
		protected final IDomainField<T> field;
		protected final IProjectPreference<T> preference;
		protected final IProject project;
		
		public PreferenceFieldBinding(IDomainField<T> field, IProjectPreference<T> preference, IProject project) {
			this.field = field;
			this.preference = preference;
			this.project = project;
		}
		
		public void updateFieldFromInput() {
			field.setFieldValue(preference.getStoredValue(project));
		}
		
		public void loadDefaults() {
			field.setFieldValue(preference.getGlobalPreference().get());
		}
		
		public void saveSettings() throws BackingStoreException {
			preference.setValue(project, field.getFieldValue());
		}
	}
	
}