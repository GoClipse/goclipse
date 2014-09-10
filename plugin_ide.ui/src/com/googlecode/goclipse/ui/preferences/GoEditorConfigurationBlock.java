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
package com.googlecode.goclipse.ui.preferences;

import melnorme.lang.ide.ui.preferences.LangEditorConfigurationBlock;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;

public class GoEditorConfigurationBlock extends LangEditorConfigurationBlock {
	
	public GoEditorConfigurationBlock(PreferencePage mainPreferencePage) {
		super(mainPreferencePage);
	}
	
	@Override
	protected void createAppearanceGroup(Composite parent) {
		//super.createAppearanceGroup(parent);
		// return, don't create appearance group
	}
	
}