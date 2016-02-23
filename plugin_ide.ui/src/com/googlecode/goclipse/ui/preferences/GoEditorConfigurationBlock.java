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
package com.googlecode.goclipse.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import melnorme.lang.ide.ui.preferences.EditorConfigurationBlock;
import melnorme.lang.ide.ui.preferences.common.PreferencesPageContext;

public class GoEditorConfigurationBlock extends EditorConfigurationBlock {
	
	public GoEditorConfigurationBlock(PreferencesPageContext prefContext, IPreferenceStore store) {
		super(prefContext, store);
	}
	
}