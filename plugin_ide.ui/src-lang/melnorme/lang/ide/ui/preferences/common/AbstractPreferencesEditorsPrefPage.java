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

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.core.utils.prefs.IGlobalPreference;
import melnorme.lang.ide.ui.preferences.common.IPreferencesEditor.GlobalPreferenceAdapter;
import melnorme.lang.tooling.data.IStatusMessage;
import melnorme.lang.tooling.data.IValidationSource;
import melnorme.lang.tooling.data.StatusLevel;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.fields.IDomainField;
import melnorme.utilbox.fields.IProperty;

/**
 * This is the preferred way to create Preference pages (as of 2015-10).
 *
 */
public abstract class AbstractPreferencesEditorsPrefPage extends AbstractLangPreferencesPage {
	
	protected final ArrayList2<IPreferencesEditor> prefAdapters = new ArrayList2<>();
	private final ArrayList2<IValidationSource> validators = new ArrayList2<>();
	
	public AbstractPreferencesEditorsPrefPage() {
		super(null);
	}
	
	public AbstractPreferencesEditorsPrefPage(IPreferenceStore store) {
		super(store);
	}
	
	/* -----------------  ----------------- */
	
	public void addPrefEditor(IPreferencesEditor prefComponent) {
		prefAdapters.add(prefComponent);
	}
	
	public <T> void bindToPreference(IGlobalPreference<T> pref, IProperty<T> field) {
		addPrefEditor(new GlobalPreferenceAdapter<>(pref, field));
	}
	public void addValidationSource(IValidationSource validationSource) {
		validators.add(validationSource);
	}
	
	public void addValidationStatusField(IDomainField<IStatusMessage> statusField) {
		IValidationSource validationSource;
		
		if(statusField instanceof IValidationSource) {
			 validationSource = (IValidationSource) statusField;
		} else {
			validationSource = new IValidationSource() {
				@Override
				public IStatusMessage getValidationStatus() {
					return statusField.getFieldValue();
				}
			};
		}
		addValidationSource(validationSource);
		statusField.addListener(() -> updateStatusMessage());
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public final void createControl(Composite parent) {
		super.createControl(parent);
		updateStatusMessage();
	}
	
	protected void updateStatusMessage() {
		if(!isControlCreated()) {
			return;
		}
		
		IStatusMessage status = IValidationSource.getHighestStatus(validators);
		if(status == null) {
			setMessage(null);
			setValid(true);
		} else {
			setMessage(status.getMessage(), statusLevelToMessageType(status.getStatusLevel()));
			setValid(status.getStatusLevel() != StatusLevel.ERROR);
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
	
	/* -----------------  ----------------- */
	
	@Override
	public void performDefaults() {
		loadStoreDefaults();
		
		super.performDefaults();
	}
	
	public void loadStoreDefaults() {
		for(IPreferencesEditor prefAdapter : prefAdapters) {
			prefAdapter.loadDefaults();
		}
	}
	
	@Override
	public boolean performOk() {
		return saveToStore();
	}
	
	public boolean saveToStore() {
		for(IPreferencesEditor prefAdapter : prefAdapters) {
			if(prefAdapter.saveSettings() == false) {
				return false;
			}
		}
		return true;
	}
	
}