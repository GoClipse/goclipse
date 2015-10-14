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

import melnorme.util.swt.components.IWidgetComponent;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Abstract preference page wrapping {@link IWidgetComponent}.
 */
public abstract class AbstractPreferencesBlockPrefPage extends AbstractPreferencesEditorsPrefPage {
	
	protected final IWidgetComponent preferencesBlock;
	
	public AbstractPreferencesBlockPrefPage(IPreferenceStore store) {
		super(store);
		
		preferencesBlock = createPreferencesBlock();
		
		if(preferencesBlock instanceof IPreferencesEditor) {
			IPreferencesEditor preferencesEditor = (IPreferencesEditor) preferencesBlock;
			/* FIXME: */
			addPrefEditor(preferencesEditor);
		}
	}
	
	protected abstract IWidgetComponent createPreferencesBlock();
	
	@Override
	protected Control createContents(Composite parent) {
		return preferencesBlock.createComponent(parent);
	}
	
}