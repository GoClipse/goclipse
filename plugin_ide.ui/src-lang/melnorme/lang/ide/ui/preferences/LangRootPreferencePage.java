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
package melnorme.lang.ide.ui.preferences;


import melnorme.lang.ide.core.operations.ToolchainPreferences;

public abstract class LangRootPreferencePage extends AbstractPreferencesBlockPrefPage {
	
	public LangRootPreferencePage() {
		super();
	}
	
	@Override
	protected LangSDKConfigBlock init_createPreferencesBlock() {
		LangSDKConfigBlock langSDKConfigBlock = init_createLangSDKConfigBlock();
		
		bindToPreference2(langSDKConfigBlock.getLocationField(), ToolchainPreferences.SDK_PATH);
		
		return langSDKConfigBlock;
	}
	
	protected abstract LangSDKConfigBlock init_createLangSDKConfigBlock();
	
}