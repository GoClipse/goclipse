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


import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import melnorme.lang.ide.core.utils.prefs.BooleanPreference;
import melnorme.lang.ide.core.utils.prefs.StringPreference;
import melnorme.lang.tooling.data.IFieldValidator;
import melnorme.lang.tooling.data.IValidatedField.ValidatedField;
import melnorme.lang.tooling.ops.util.LocationOrSinglePathValidator;
import melnorme.lang.tooling.ops.util.LocationValidator;
import melnorme.lang.tooling.ops.util.PathValidator;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.FieldComponent;
import melnorme.util.swt.components.fields.DirectoryTextField;
import melnorme.util.swt.components.fields.FileTextField;
import melnorme.utilbox.fields.IDomainField;

/**
 * This is the preferred way to create Preference pages (as of 2015-02).
 * 
 * This page delegates apply/cancel/revert to preference components 
 * (Usually connected to {@link FieldComponent}).
 */
public abstract class AbstractStoreComponentsPrefPage extends AbstractPreferencesEditorsPrefPage {
	
	protected final IPreferenceStore store;
	
	public AbstractStoreComponentsPrefPage(IPreferenceStore store) {
		super();
		this.store = store;
		setPreferenceStore(store);
	}
	
	@Override
	public void setPreferenceStore(IPreferenceStore store) {
		assertTrue(getPreferenceStore() == null); // Cannot change store after setting. 
		super.setPreferenceStore(store);
	}
	
	/* ======================= Field helpers: ======================= */
	
	public void addStringComponent(StringPreference pref, Composite parent, FieldComponent<String> field) {
		bindToPreference(pref, field);
		field.createComponentInlined(parent);
	}
	public void addBooleanComponent(BooleanPreference pref, Composite parent, FieldComponent<Boolean> field) {
		bindToPreference(pref, field);
		field.createComponentInlined(parent);
	}
	
	/* ----------------- Some field creation helpers, not recommended though ----------------- */
	
	public static Group createOptionsSection(Composite parent, String label, GridData gridData, int numColumns) {
		Group group = SWTFactoryUtil.createGroup(parent, label, gridData);
		group.setLayout(GridLayoutFactory.fillDefaults().numColumns(numColumns).spacing(6, 4).margins(6, 4).create());
		return group;
	}
	
	public FileTextField createFileComponent(Group group, String label, StringPreference pref, 
			boolean allowSinglePath) {
		FileTextField pathField = new FileTextField(label);
		pathField.createComponentInlined(group);
		PathValidator validator = (allowSinglePath ? 
				new LocationOrSinglePathValidator(label) : new LocationValidator(label)).setFileOnly(true);
		connectStringField(pref, pathField, validator);
		return pathField;
	}
	public DirectoryTextField createDirectoryComponent(Group group, String label, StringPreference pref, 
			boolean allowSinglePath) {
		DirectoryTextField pathField = new DirectoryTextField(label);
		pathField.createComponentInlined(group);
		PathValidator validator = (allowSinglePath ? 
				new LocationOrSinglePathValidator(label) : new LocationValidator(label)).setDirectoryOnly(true);
		connectStringField(pref, pathField, validator);
		return pathField;
	}
	
	public void connectStringField(StringPreference pref, IDomainField<String> field, IFieldValidator validator) {
		bindToPreference(pref, field);
		
		addValidationSource(new ValidatedField(field, validator));
		
		field.addValueChangedListener(() -> updateStatusMessage());
	}
	
}