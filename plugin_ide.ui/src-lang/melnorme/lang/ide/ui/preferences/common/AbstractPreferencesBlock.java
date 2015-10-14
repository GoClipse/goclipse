/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.preferences.common;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.core.utils.prefs.BooleanPreference;
import melnorme.lang.ide.core.utils.prefs.IGlobalPreference;
import melnorme.lang.ide.core.utils.prefs.IntPreference;
import melnorme.lang.ide.core.utils.prefs.StringPreference;
import melnorme.lang.ide.ui.utils.DialogPageUtils;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.AbstractComponent;
import melnorme.util.swt.components.FieldComponent;
import melnorme.util.swt.components.fields.ComboBoxField;
import melnorme.util.swt.components.fields.NumberField;

public abstract class AbstractPreferencesBlock extends AbstractComponent {
	
	protected final AbstractPreferencesEditorsPrefPage prefPage;
	protected final IPreferenceStore store;
	
	public AbstractPreferencesBlock(AbstractPreferencesEditorsPrefPage prefPage) {
		this.prefPage = assertNotNull(prefPage);
		this.store = prefPage.getPreferenceStore();
	}
	
	@Override
	public void updateComponentFromInput() {
	}
	
	protected void updateStatus(IStatus status) {
		DialogPageUtils.applyStatusToPreferencePage(status, prefPage);
	}
	
	public <T> void createAndBindComponent(Composite parent, IGlobalPreference<T> pref, FieldComponent<T> field) {
		field.createComponentInlined(parent);
		prefPage.bindToPreference(pref, field);
	}
	
	protected <T> void createStringField(Composite parent, IGlobalPreference<T> pref, FieldComponent<T> field) {
		createAndBindComponent(parent, pref, field);
	}
	
	protected void createBooleanField(Composite parent, BooleanPreference pref, FieldComponent<Boolean> field) {
		createAndBindComponent(parent, pref, field);
	}
	
	protected void createIntField(Composite parent, IntPreference pref, NumberField field) {
		field.createComponentInlined(parent);
		prefPage.bindToPreference(pref, field.asIntProperty());
	}
	
	protected void createCheckboxField(Composite parent, StringPreference pref, ComboBoxField field) {
		field.createComponentInlined(parent);
		prefPage.bindToPreference(pref, field.asStringProperty());
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