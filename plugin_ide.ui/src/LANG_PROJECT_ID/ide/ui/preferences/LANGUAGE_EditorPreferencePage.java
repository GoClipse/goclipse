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
package LANG_PROJECT_ID.ide.ui.preferences;


import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.preferences.LangEditorConfigurationBlock;
import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesBlockPrefPage;


public class LANGUAGE_EditorPreferencePage extends AbstractPreferencesBlockPrefPage {
	
	public LANGUAGE_EditorPreferencePage() {
		super();
	}
	
	@Override
	protected LangEditorConfigurationBlock init_createPreferencesBlock() {
		return new LangEditorConfigurationBlock(LangUIPlugin.getInstance().getPreferenceStore());
	}
	
	@Override
	protected String getHelpId() {
		return null;
	}
	
}