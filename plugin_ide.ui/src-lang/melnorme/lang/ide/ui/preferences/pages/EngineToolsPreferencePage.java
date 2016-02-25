/*******************************************************************************
 * Copyright (c) 2014, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.preferences.pages;

import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesBlockPrefPage;
import melnorme.lang.ide.ui.preferences.common.PreferencesPageContext;

public abstract class EngineToolsPreferencePage extends AbstractPreferencesBlockPrefPage {
	
	public EngineToolsPreferencePage() {
	}
	
	@Override
	protected String getHelpId() {
		return null;
	}
	
	/* -----------------  ----------------- */
	
	@Override
	protected LanguageToolsBlock init_createPreferencesBlock(PreferencesPageContext prefContext) {
		return new LanguageToolsBlock(prefContext);
	}
	
}