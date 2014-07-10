/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.preferences;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.ArrayList;

import melnorme.lang.ide.ui.preferences.fields.BooleanFieldAdapter;
import melnorme.lang.ide.ui.preferences.fields.ComboFieldAdapter;
import melnorme.lang.ide.ui.preferences.fields.StringFieldAdapter;
import melnorme.lang.ide.ui.utils.DialogPageUtils;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.AbstractComponentExt;
import melnorme.util.swt.components.AbstractField;
import melnorme.util.swt.components.IDisposable;
import melnorme.util.swt.components.fields.ComboBoxField;
import melnorme.util.swt.components.fields.NumberField;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

public abstract class AbstractPreferencesConfigBlock extends AbstractComponentExt 
	implements IDisposable, IPreferencesBlock {
	
	protected final ArrayList<IPreferencesComponent> configFields = new ArrayList<>();
	protected final PreferencePage prefPage;
	protected final IPreferenceStore preferenceStore;
	
	public AbstractPreferencesConfigBlock(PreferencePage prefPage) {
		this.prefPage = assertNotNull(prefPage);
		this.preferenceStore = prefPage.getPreferenceStore();
	}
	
	@Override
	public void updateComponentFromInput() {
		loadFromStore();
	}
	
	@Override
	public void loadFromStore() {
		for(IPreferencesComponent configField : configFields) {
			configField.loadFromStore(preferenceStore);
		}	
	}
	
	@Override
	public void saveToStore() {
		for(IPreferencesComponent configField : configFields) {
			configField.saveToStore(preferenceStore);
		}
	}
	
	@Override
	public void loadStoreDefaults() {
		for(IPreferencesComponent configField : configFields) {
			configField.loadStoreDefaults(preferenceStore);
		}
	}
	
	@Override
	public void dispose() {
	}
	
	protected void updateStatus(IStatus status) {
		DialogPageUtils.applyStatusToPreferencePage(status, prefPage);
	}
	
	protected <T extends IPreferencesComponent> T addConfigComponent(Composite parent, T configControl) {
		configControl.createComponentInlined(parent);
		addConfigComponent(configControl);
		return configControl;
	}
	
	protected StringFieldAdapter createStringField(Composite parent, String prefKey, AbstractField<String> field) {
		return addConfigComponent(parent, new StringFieldAdapter(prefKey, field));
	}
	
	protected BooleanFieldAdapter createBooleanField(Composite parent, String prefKey, AbstractField<Boolean> field) {
		return addConfigComponent(parent, new BooleanFieldAdapter(prefKey, field));
	}
	
	protected ComboFieldAdapter createCheckboxField(Composite parent, String prefKey, ComboBoxField field) {
		return addConfigComponent(parent, new ComboFieldAdapter(prefKey, field));
	}
	
	protected void addConfigComponent(IPreferencesComponent configComponent) {
		configFields.add(configComponent);
	}
	
	/* -----------------  ----------------- */
	
	protected Composite createSubsection(Composite parent, String label) {
		return SWTFactoryUtil.createGroup(parent, label, 
			new GridData(SWT.FILL, SWT.CENTER, true, false));
	}
	
	protected NumberField createNumberField(String label, int textLimit) {
		return new NumberField(label, textLimit) {
			@Override
			protected void statusChanged(IStatus status) {
				updateStatus(status);
			}
		};
	}
	
}