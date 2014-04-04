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

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.preferences.fields.AbstractConfigField;
import melnorme.lang.ide.ui.preferences.fields.NumberConfigField;
import melnorme.lang.ide.ui.preferences.fields.TextConfigField;
import melnorme.lang.ide.ui.utils.DialogPageUtils;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.IDisposable;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

public abstract class AbstractPreferencesConfigComponent implements IDisposable, 
	IPreferenceConfigurationBlock2 {
	
	protected static final Status NO_STATUS = LangCore.createOkStatus(null);
	
	protected final ArrayList<IConfigComponent> configFields = new ArrayList<>();
	protected final IPreferenceStore store;
	protected final PreferencePage prefPage;
	
	public AbstractPreferencesConfigComponent(IPreferenceStore store, PreferencePage prefPage) {
		this.store = assertNotNull(store);
		this.prefPage = assertNotNull(prefPage);
	}
	
	protected final IPreferenceStore getPreferenceStore() {
		return store;
	}
	
	@Override
	public void performOk() {
		for(IConfigComponent configField : configFields) {
			configField.saveToStore(store);
		}
	}
	
	@Override
	public void performDefaults() {
		updateStatus(NO_STATUS);
		
		for(IConfigComponent configField : configFields) {
			configField.resetToDefaults(store);
			configField.loadFromStore(store);
		}
	}
	
	@Override
	public void dispose() {
	}
	
	protected void updateStatus(IStatus status) {
		DialogPageUtils.applyStatusToPreferencePage(status, prefPage);
	}
	
	protected <T extends AbstractConfigField<?>> T addConfigComponent(Composite parent, int indentation, 
			T configControl) {
		configControl.createControl(parent, indentation);
		addConfigComponent(configControl);
		return configControl;
	}
	
	protected void addConfigComponent(IConfigComponent configComponent) {
		configComponent.loadFromStore(store);
		configFields.add(configComponent);
	}
	
	/* -----------------  ----------------- */
	
	protected Composite createSubsection(Composite parent, String label) {
		return SWTFactoryUtil.createGroup(parent, label, 
			new GridData(SWT.FILL, SWT.CENTER, true, false));
	}
	
	protected TextConfigField createNumberField(String label, String key, int textLimit) {
		return new NumberConfigField(label, key, textLimit) {
			@Override
			protected void statusChanged(IStatus status) {
				updateStatus(status);
			}
		};
	}
	
}