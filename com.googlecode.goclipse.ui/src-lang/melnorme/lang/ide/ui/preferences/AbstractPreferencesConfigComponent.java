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

public abstract class AbstractPreferencesConfigComponent extends AbstractComponentExt 
	implements IDisposable, IPreferencesComponent {
	
	protected final ArrayList<IPreferencesComponent> configFields = new ArrayList<>();
	protected final PreferencePage prefPage;
	
	public AbstractPreferencesConfigComponent(PreferencePage prefPage) {
		this.prefPage = assertNotNull(prefPage);
	}
	
	@Override
	public void updateComponentFromInput() {
		loadFromStore(prefPage.getPreferenceStore());
	}
	
	@Override
	public void loadFromStore(IPreferenceStore store) {
		for(IPreferencesComponent configField : configFields) {
			configField.loadFromStore(store);
		}	
	}
	
	@Override
	public void saveToStore(IPreferenceStore store) {
		for(IPreferencesComponent configField : configFields) {
			configField.saveToStore(store);
		}
	}
	
	@Override
	public void resetToDefaults(IPreferenceStore store) {
		for(IPreferencesComponent configField : configFields) {
			configField.resetToDefaults(store);
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