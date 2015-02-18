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


import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import melnorme.lang.ide.ui.preferences.common.IPreferencesDialogComponent.BooleanFieldAdapter;
import melnorme.lang.ide.ui.preferences.common.IPreferencesDialogComponent.ComboFieldAdapter;
import melnorme.lang.ide.ui.preferences.common.IPreferencesDialogComponent.StringFieldAdapter;
import melnorme.lang.tooling.data.IFieldValidator;
import melnorme.lang.tooling.data.LocationOrSinglePathValidator;
import melnorme.lang.tooling.data.LocationValidator;
import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.data.StatusException.StatusLevel;
import melnorme.util.swt.components.AbstractField;
import melnorme.util.swt.components.IFieldValueListener;
import melnorme.util.swt.components.fields.ComboBoxField;
import melnorme.util.swt.components.fields.DirectoryTextField;
import melnorme.util.swt.components.fields.FileTextField;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * This is the preferred way to create Preference pages (as of 2015-02).
 * 
 * This page delegates apply/cancel/revert to preference components (Usually connected to {@link AbstractField}).
 */
public abstract class AbstractComponentsPrefPage extends AbstractLangPreferencesPage 
	implements IPreferencesDialog {
	
	private final ArrayList<IPreferencesDialogComponent> prefComponents = new ArrayList<>();
	private final LinkedHashMap<AbstractField<String>, IFieldValidator<?>> validators = new LinkedHashMap<>();
	
	private StatusException currentStatus = null;
	
	public AbstractComponentsPrefPage(IPreferenceStore store) {
		super(store);
	}
	
	@Override
	public void setPreferenceStore(IPreferenceStore store) {
		assertTrue(getPreferenceStore() == null); // Cannot change store after setting. 
		super.setPreferenceStore(store);
	}
	
	@Override
	public void addComponent(IPreferencesDialogComponent prefComponent) {
		prefComponents.add(prefComponent);
		prefComponent.loadFromStore(getPreferenceStore());
	}
	
	public void loadFromStore() {
		for(IPreferencesDialogComponent configField : prefComponents) {
			configField.loadFromStore(getPreferenceStore());
		}	
	}
	
	public StatusLevel getFieldStatus(AbstractField<String> field) {
		IFieldValidator<?> validator = validators.get(field);
		StatusException fieldStatus = LocationValidator.getFieldStatus(validator, field.getFieldValue());
		if(fieldStatus == null) {
			return StatusLevel.OK;
		}
		return fieldStatus.getStatusLevel();
	}
	
	public void connectStringField(String prefKey, AbstractField<String> field, IFieldValidator<?> validator) {
		addStringComponent(prefKey, field);
		
		validators.put(field, validator);
		
		field.addValueChangedListener(new IFieldValueListener() {
			@Override
			public void fieldValueChanged() {
				updateStatusMessage();
			}
		});
	}
	
	protected void updateStatusMessage() {
		if(!isControlCreated()) {
			return;
		}
		
		currentStatus = null;
		setMessage(null);
		setValid(true);
		
		for (Entry<AbstractField<String>, IFieldValidator<?>> entry : validators.entrySet()) {
			AbstractField<String> field = entry.getKey();
			IFieldValidator<?> validator = entry.getValue();
			
			try {
				validator.getValidatedField(field.getFieldValue());
			} catch (StatusException se) {
				if(currentStatus == null || se.getStatusLevelOrdinal() > currentStatus.getStatusLevelOrdinal()) {
					currentStatus = se;
					setMessage(se.getMessage(), statusLevelToMessageType(se.getStatusLevel()));
					setValid(se.getStatusLevel() != StatusLevel.ERROR);
				}
			}
		}
	}
	
	public static int statusLevelToMessageType(StatusLevel statusLevel) {
		switch (statusLevel) {
		case OK: return IMessageProvider.NONE;
		case INFO: return IMessageProvider.INFORMATION;
		case WARNING: return IMessageProvider.WARNING;
		case ERROR: return IMessageProvider.ERROR;
		}
		throw assertFail();
	}
	
	@Override
	public final void createControl(Composite parent) {
		super.createControl(parent);
		updateStatusMessage();
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public void performDefaults() {
		loadStoreDefaults();
		
		super.performDefaults();
	}
	
	public void loadStoreDefaults() {
		for(IPreferencesDialogComponent configField : prefComponents) {
			configField.loadStoreDefaults(getPreferenceStore());
		}
	}
	
	@Override
	public boolean performOk() {
		saveToStore();
		return true;
	}
	
	public void saveToStore() {
		for(IPreferencesDialogComponent configField : prefComponents) {
			configField.saveToStore(getPreferenceStore());
		}
	}
	
	/* ----------------- Helpers:  ----------------- */
	
	public void addStringComponent(String prefKey, AbstractField<String> field) {
		addComponent(new StringFieldAdapter(prefKey, field));
	}
	public void addBooleanComponent(String prefKey, AbstractField<Boolean> field) {
		addComponent(new BooleanFieldAdapter(prefKey, field));
	}
	public void addComboComponent(String prefKey, ComboBoxField field) {
		addComponent(new ComboFieldAdapter(prefKey, field));
	}
	
	public void addStringComponent(String prefKey, Composite parent, AbstractField<String> field) {
		addStringComponent(prefKey, field);
		field.createComponentInlined(parent);
	}
	public void addBooleanComponent(String prefKey, Composite parent, AbstractField<Boolean> field) {
		addBooleanComponent(prefKey, field);
		field.createComponentInlined(parent);
	}
	public void addComboComponent(String prefKey, Composite parent, ComboBoxField field) {
		addComboComponent(prefKey, field);
		field.createComponentInlined(parent);
	}
	
	public void connectFileField(String prefKey, AbstractField<String> stringField, boolean allowSinglePath) {
		LocationValidator validator = allowSinglePath ? new LocationOrSinglePathValidator() : new LocationValidator();
		validator.fileOnly = true;
		connectStringField(prefKey, stringField, validator);
	}
	public void connectDirectoryField(String prefKey, AbstractField<String> stringField, boolean allowSinglePath) {
		LocationValidator validator = allowSinglePath ? new LocationOrSinglePathValidator() : new LocationValidator();
		validator.directoryOnly = true;
		connectStringField(prefKey, stringField, validator);
	}
	
	public void createFileComponent(Group group, String label, String prefKey) {
		createFileComponent(group, label, prefKey, false);
	}
	public void createFileComponent(Group group, String label, String prefKey, boolean allowSinglePath) {
		FileTextField pathField = new FileTextField(label);
		pathField.createComponentInlined(group);
		connectFileField(prefKey, pathField, allowSinglePath);
	}
	public void createDirectoryComponent(Group group, String label, String prefKey) {
		createDirectoryComponent(group, label, prefKey, false);
	}
	public void createDirectoryComponent(Group group, String label, String prefKey, boolean allowSinglePath) {
		DirectoryTextField pathField = new DirectoryTextField(label);
		pathField.createComponentInlined(group);
		connectDirectoryField(prefKey, pathField, allowSinglePath);
	}
	
}