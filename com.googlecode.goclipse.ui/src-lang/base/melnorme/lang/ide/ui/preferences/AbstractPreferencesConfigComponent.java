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

import melnorme.lang.ide.ui.preferences.fields.AbstractConfigField;
import melnorme.lang.ide.ui.preferences.fields.CheckBoxConfigField;
import melnorme.lang.ide.ui.preferences.fields.NumberConfigField;
import melnorme.lang.ide.ui.preferences.fields.TextConfigField;
import melnorme.lang.ide.ui.utils.DialogPageUtils;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.AbstractComponentExt;
import melnorme.util.swt.components.IDisposable;

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
	
	protected <T extends AbstractConfigField<?>> T addConfigComponent(Composite parent, T configControl) {
		return addConfigComponent(parent, 0, configControl);
	}
	
	protected <T extends AbstractConfigField<?>> T addConfigComponent(Composite parent, int indentation, 
			T configControl) {
		configControl.createControl(parent, indentation);
		addConfigComponent(configControl);
		return configControl;
	}
	
	protected void addConfigComponent(IPreferencesComponent configComponent) {
		configFields.add(configComponent);
	}
	
	/* -----------------  ----------------- */
	
	protected Composite createSubsection(Composite parent, String label) {
		return SWTFactoryUtil.createGroup(parent, label, 
			new GridData(SWT.FILL, SWT.CENTER, true, false));
	}
	
	protected CheckBoxConfigField addCheckBox(Composite parent, String label, String prefKey, int indentation) {
		return addConfigComponent(parent, indentation, new CheckBoxConfigField(label, prefKey));
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