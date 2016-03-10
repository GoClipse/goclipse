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
package melnorme.lang.ide.ui.preferences.pages;


import melnorme.lang.ide.ui.preferences.EditorTypingConfigurationBlock;
import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesBlockPrefPage;
import melnorme.lang.ide.ui.preferences.common.PreferencesPageContext;

public abstract class EditorTypingPreferencePage extends AbstractPreferencesBlockPrefPage {
	
	public EditorTypingPreferencePage() {
		super();
	}
	
	@Override
	protected EditorTypingConfigurationBlock init_createPreferencesBlock(PreferencesPageContext prefContext) {
		return new EditorTypingConfigurationBlock(prefContext);
	}
	
	@Override
	protected String getHelpId() {
		return null;
	}
	
}