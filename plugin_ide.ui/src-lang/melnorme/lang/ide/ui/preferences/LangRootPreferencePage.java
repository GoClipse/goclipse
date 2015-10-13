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


import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import melnorme.lang.ide.core.operations.ToolchainPreferences;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.preferences.common.AbstractComponentsPrefPage;

public abstract class LangRootPreferencePage extends AbstractComponentsPrefPage {
	
	protected final LangSDKConfigBlock langSDKConfigBlock;
	
	public LangRootPreferencePage() {
		super(LangUIPlugin.getCorePrefStore());
		
		langSDKConfigBlock = createLangSDKConfigBlock();
	}
	
	protected LangSDKConfigBlock createLangSDKConfigBlock() {
		LangSDKConfigBlock langSDKConfigBlock = doCreateLangSDKConfigBlock();
		
		addValidationStatusField(langSDKConfigBlock.validation);
		
		addStringComponent(ToolchainPreferences.SDK_PATH, langSDKConfigBlock.getLocationField());
		
		return langSDKConfigBlock;
	}
	
	protected abstract LangSDKConfigBlock doCreateLangSDKConfigBlock();
	
	@Override
	protected Control createContents(Composite parent) {
		return langSDKConfigBlock.createComponent(parent);
	}
	
}