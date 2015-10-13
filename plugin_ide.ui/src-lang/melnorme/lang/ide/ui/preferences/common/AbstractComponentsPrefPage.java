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
package melnorme.lang.ide.ui.preferences.common;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import melnorme.lang.ide.core.utils.prefs.BooleanPreference;
import melnorme.lang.ide.core.utils.prefs.PreferenceHelper;
import melnorme.lang.ide.core.utils.prefs.StringPreference;
import melnorme.lang.ide.ui.preferences.common.IPreferencesDialogComponent.BooleanFieldAdapter;
import melnorme.lang.ide.ui.preferences.common.IPreferencesDialogComponent.ComboFieldAdapter;
import melnorme.lang.ide.ui.preferences.common.IPreferencesDialogComponent.StringFieldAdapter;
import melnorme.lang.tooling.data.IFieldValidator;
import melnorme.lang.tooling.data.LocationOrSinglePathValidator;
import melnorme.lang.tooling.data.LocationValidator;
import melnorme.lang.tooling.data.PathValidator;
import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.data.StatusLevel;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.FieldComponent;
import melnorme.util.swt.components.fields.ComboBoxField;
import melnorme.util.swt.components.fields.DirectoryTextField;
import melnorme.util.swt.components.fields.FileTextField;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.fields.IDomainField;
import melnorme.utilbox.fields.IValidatedField;
import melnorme.utilbox.fields.IValidatedField.ValidatedField;

/**
 * This is the preferred way to create Preference pages (as of 2015-02).
 * 
 * This page delegates apply/cancel/revert to preference components 
 * (Usually connected to {@link FieldComponent}).
 */
public abstract class AbstractComponentsPrefPage extends AbstractLangPreferencesPage 
	implements IPreferencesDialog {
	
	private final ArrayList2<IPreferencesDialogComponent> prefComponents = new ArrayList2<>();
	private final ArrayList2<IValidatedField<?>> validators = new ArrayList2<>();
	
	public AbstractComponentsPrefPage(IPreferenceStore store) {
		super(store);
	}
	
	@Override
	public void setPreferenceStore(IPreferenceStore store) {
		assertTrue(getPreferenceStore() == null); // Cannot change store after setting. 
		super.setPreferenceStore(store);
	}
	
	@Override
	public void addPrefComponent(IPreferencesDialogComponent prefComponent) {
		prefComponents.add(prefComponent);
		prefComponent.loadFromStore(getPreferenceStore());
	}
	
	public void loadFromStore() {
		for(IPreferencesDialogComponent configField : prefComponents) {
			configField.loadFromStore(getPreferenceStore());
		}	
	}
	
	public void connectStringField(StringPreference pref, IDomainField<String> field, IFieldValidator validator) {
		assertNotNull(validator);
		connectStringComponent(pref, field, new ValidatedField(field, validator));
	}
	
	public void connectStringComponent(StringPreference pref, IDomainField<String> field,
			IValidatedField<?> validationSource) {
		addStringComponent(pref, field);
		
		addValidationSource(validationSource);
		
		field.addValueChangedListener(() -> updateStatusMessage());
	}
	
	protected void addValidationSource(IValidatedField<?> validationSource) {
		validators.add(validationSource);
	}
	
	protected void updateStatusMessage() {
		if(!isControlCreated()) {
			return;
		}
		
		StatusException se = IValidatedField.getHighestStatus(validators);
		if(se == null) {
			setMessage(null);
			setValid(true);
		} else {
			setMessage(se.getMessage(), statusLevelToMessageType(se.getStatusLevel()));
			setValid(se.getStatusLevel() != StatusLevel.ERROR);
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
	
	/* ======================= Helpers:  ======================= */
	
	public static Group createOptionsSection(Composite parent, String label, GridData gridData, int numColumns) {
		Group group = SWTFactoryUtil.createGroup(parent, label, gridData);
		group.setLayout(GridLayoutFactory.fillDefaults().numColumns(numColumns).spacing(6, 4).margins(6, 4).create());
		return group;
	}
	
	public void addStringComponent(StringPreference pref, IDomainField<String> field) {
		addPrefComponent(new StringFieldAdapter(pref.key, field));
	}
	public void addBooleanComponent(BooleanPreference pref, IDomainField<Boolean> field) {
		addPrefComponent(new BooleanFieldAdapter(pref.key, field));
	}
	public void addComboComponent(PreferenceHelper<?> pref, ComboBoxField field) {
		addPrefComponent(new ComboFieldAdapter(pref.key, field));
	}
	
	public void addStringComponent(StringPreference pref, Composite parent, FieldComponent<String> field) {
		addStringComponent(pref, field);
		field.createComponentInlined(parent);
	}
	public void addBooleanComponent(BooleanPreference pref, Composite parent, FieldComponent<Boolean> field) {
		addBooleanComponent(pref, field);
		field.createComponentInlined(parent);
	}
	public void addComboComponent(PreferenceHelper<?> pref, Composite parent, ComboBoxField field) {
		addComboComponent(pref, field);
		field.createComponentInlined(parent);
	}
	
	/* FIXME: remove deprecated */
	
	@Deprecated
	public void connectFileField(StringPreference pref, IDomainField<String> stringField, 
			boolean allowSinglePath, String fieldNamePrefix) {
		PathValidator validator = (allowSinglePath ? 
				new LocationOrSinglePathValidator(fieldNamePrefix) : new LocationValidator(fieldNamePrefix)).setFileOnly(true);
		connectStringField(pref, stringField, validator);
	}
	@Deprecated
	public void connectDirectoryField(StringPreference pref, IDomainField<String> stringField, 
			boolean allowSinglePath, String fieldNamePrefix) {
		PathValidator validator = (allowSinglePath ? 
				new LocationOrSinglePathValidator(fieldNamePrefix) : new LocationValidator(fieldNamePrefix)).setDirectoryOnly(true);
		connectStringField(pref, stringField, validator);
	}
	
	@Deprecated
	public FileTextField createFileComponent(Group group, String label, StringPreference pref, 
			boolean allowSinglePath) {
		FileTextField pathField = new FileTextField(label);
		pathField.createComponentInlined(group);
		connectFileField(pref, pathField, allowSinglePath, label);
		return pathField;
	}
	@Deprecated
	public DirectoryTextField createDirectoryComponent(Group group, String label, StringPreference pref, 
			boolean allowSinglePath) {
		DirectoryTextField pathField = new DirectoryTextField(label);
		pathField.createComponentInlined(group);
		connectDirectoryField(pref, pathField, allowSinglePath, label);
		return pathField;
	}
	
}