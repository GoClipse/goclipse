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


import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.preferences.AbstractConfigurationBlockPreferencePage2;
import melnorme.lang.ide.ui.preferences.IPreferenceConfigurationBlock2;


public class GoEditorPreferencePage extends AbstractConfigurationBlockPreferencePage2 {
	
	@Override
	protected void setDescription() {
		setDescription(null);
	}
	
	@Override
	protected void setPreferenceStore() {
		setPreferenceStore(LangUIPlugin.getInstance().getPreferenceStore());
	}
	
	@Override
	protected IPreferenceConfigurationBlock2 createConfigurationBlock() {
		return new GoEditorConfigurationBlock(this, getPreferenceStore());
	}
	
	@Override
	protected String getHelpId() {
		return null;
	}
	
}