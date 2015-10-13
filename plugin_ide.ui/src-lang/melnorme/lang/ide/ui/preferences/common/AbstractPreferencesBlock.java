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

import melnorme.lang.ide.ui.preferences.common.IPreferencesDialogComponent.BooleanFieldAdapter;
import melnorme.lang.ide.ui.preferences.common.IPreferencesDialogComponent.ComboFieldAdapter;
import melnorme.lang.ide.ui.preferences.common.IPreferencesDialogComponent.StringFieldAdapter;
import melnorme.lang.ide.ui.utils.DialogPageUtils;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.AbstractComponent;
import melnorme.util.swt.components.FieldComponent;
import melnorme.util.swt.components.fields.ComboBoxField;
import melnorme.util.swt.components.fields.NumberField;

public abstract class AbstractPreferencesBlock extends AbstractComponent {
	
	protected final AbstractComponentsPrefPage prefPage;
	protected final IPreferenceStore preferenceStore;
	
	public AbstractPreferencesBlock(AbstractComponentsPrefPage prefPage) {
		this.prefPage = assertNotNull(prefPage);
		this.preferenceStore = prefPage.getPreferenceStore();
	}
	
	@Override
	public void updateComponentFromInput() {
	}
	
	protected void updateStatus(IStatus status) {
		DialogPageUtils.applyStatusToPreferencePage(status, prefPage);
	}
	
	protected void addPrefComponent(IPreferencesDialogComponent prefComponent) {
		prefPage.addPrefComponent(prefComponent);
	}
	
	protected void createStringField(Composite parent, String prefKey, FieldComponent<String> field) {
		field.createComponentInlined(parent);
		addPrefComponent(new StringFieldAdapter(prefKey, field));
	}
	
	protected void createBooleanField(Composite parent, String prefKey, FieldComponent<Boolean> field) {
		field.createComponentInlined(parent);
		addPrefComponent(new BooleanFieldAdapter(prefKey, field));
	}
	
	protected void createCheckboxField(Composite parent, String prefKey, ComboBoxField field) {
		field.createComponentInlined(parent);
		addPrefComponent(new ComboFieldAdapter(prefKey, field));
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