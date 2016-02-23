/*******************************************************************************
 * Copyright (c) 2008, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package LANG_PROJECT_ID.ide.ui.preferences;


import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesBlockPrefPage;
import melnorme.lang.ide.ui.preferences.common.PreferencesPageContext;

public class LANGUAGE_SourceColoringPreferencePage extends AbstractPreferencesBlockPrefPage {
	
	public final static String PAGE_ID = LANGUAGE_SourceColoringPreferencePage.class.getName();
	
	public LANGUAGE_SourceColoringPreferencePage() {
		super();
	}
	
	@Override
	protected String getHelpId() {
		return null;
	}
	
	@Override
	protected LANGUAGE_SourceColoringConfigurationBlock init_createPreferencesBlock(PreferencesPageContext prefContext) {
		return new LANGUAGE_SourceColoringConfigurationBlock(prefContext);
	}
	
}