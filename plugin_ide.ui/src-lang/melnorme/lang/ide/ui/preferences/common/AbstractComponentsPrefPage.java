/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.preferences.common;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.ArrayList;

import melnorme.lang.ide.ui.preferences.common.IPreferencesAdapterComponent.BooleanFieldAdapter;
import melnorme.lang.ide.ui.preferences.common.IPreferencesAdapterComponent.ComboFieldAdapter;
import melnorme.lang.ide.ui.preferences.common.IPreferencesAdapterComponent.StringFieldAdapter;
import melnorme.util.swt.components.AbstractField;
import melnorme.util.swt.components.fields.ComboBoxField;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;

/**
 * This is the preferred way to create Preference pages (as of 2015-02).
 * 
 * This page delegates apply/cancel/revert to preference components (Usually connected to {@link AbstractField}).
 */
public abstract class AbstractComponentsPrefPage extends AbstractLangPreferencesPage {
	
	private final ArrayList<IPreferencesAdapterComponent> prefComponents = new ArrayList<>();
	
	public AbstractComponentsPrefPage(IPreferenceStore store) {
		super(store);
	}
	
	@Override
	public void setPreferenceStore(IPreferenceStore store) {
		assertTrue(getPreferenceStore() == null); // Cannot change store after setting. 
		super.setPreferenceStore(store);
	}
	
	protected void addComponent(IPreferencesAdapterComponent prefComponent) {
		prefComponents.add(prefComponent);
		prefComponent.loadFromStore(getPreferenceStore());
	}
	
	protected void addStringComponent(String prefKey, AbstractField<String> field) {
		addComponent(new StringFieldAdapter(prefKey, field));
	}
	protected void addBooleanComponent(String prefKey, AbstractField<Boolean> field) {
		addComponent(new BooleanFieldAdapter(prefKey, field));
	}
	protected void addComboComponent(String prefKey, ComboBoxField field) {
		addComponent(new ComboFieldAdapter(prefKey, field));
	}
	
	protected void addStringComponent(String prefKey, Composite parent, AbstractField<String> field) {
		addComponent(new StringFieldAdapter(prefKey, field));
		field.createComponentInlined(parent);
	}
	protected void addBooleanComponent(String prefKey, Composite parent, AbstractField<Boolean> field) {
		addComponent(new BooleanFieldAdapter(prefKey, field));
		field.createComponentInlined(parent);
	}
	protected void addComboComponent(String prefKey, Composite parent, ComboBoxField field) {
		addComponent(new ComboFieldAdapter(prefKey, field));
		field.createComponentInlined(parent);
	}
	
	public void loadFromStore() {
		for(IPreferencesAdapterComponent configField : prefComponents) {
			configField.loadFromStore(getPreferenceStore());
		}	
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public void performDefaults() {
		loadStoreDefaults();
		
		super.performDefaults();
	}
	
	public void loadStoreDefaults() {
		for(IPreferencesAdapterComponent configField : prefComponents) {
			configField.loadStoreDefaults(getPreferenceStore());
		}
	}
	
	@Override
	public boolean performOk() {
		saveToStore();
		return true;
	}
	
	public void saveToStore() {
		for(IPreferencesAdapterComponent configField : prefComponents) {
			configField.saveToStore(getPreferenceStore());
		}
	}
	
}