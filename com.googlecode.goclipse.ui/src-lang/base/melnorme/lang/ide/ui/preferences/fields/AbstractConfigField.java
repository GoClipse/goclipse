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
package melnorme.lang.ide.ui.preferences.fields;

import melnorme.lang.ide.ui.preferences.IPreferencesComponent;
import melnorme.util.swt.components.AbstractField;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public abstract class AbstractConfigField<VALUE> extends AbstractField<VALUE> implements IPreferencesComponent {
	
	protected final String prefKey;
	protected final String label;
	
	public AbstractConfigField(String prefKey, String label) {
		this.prefKey = prefKey;
		this.label = label;
	}
	
	/** @return the leftmost control */
	public void createControl(Composite parent, int indentation) {
		Control leftMostControl = createControl(parent);
		((GridData) leftMostControl.getLayoutData()).horizontalIndent = indentation;
	}
	
	@Override
	public void createComponent(Composite parent) {
		createControl(parent);
	}
	
	@Override
	public void resetToDefaults(IPreferenceStore store) {
		store.setToDefault(prefKey);
		loadFromStore(store);
	}
	
}