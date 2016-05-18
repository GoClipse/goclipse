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

import LANG_PROJECT_ID.tooling.toolchain.LANGUAGE_SDKLocationValidator;
import melnorme.lang.ide.ui.preferences.LangSDKConfigBlock;
import melnorme.lang.ide.ui.preferences.common.PreferencesPageContext;
import melnorme.lang.ide.ui.preferences.pages.RootPreferencePage;
import melnorme.lang.tooling.toolchain.ops.SDKLocationValidator;


/**
 * The main/root preference page.
 */
public class LANGUAGE_RootPreferencePage extends RootPreferencePage {
	
	public LANGUAGE_RootPreferencePage() {
		super();
	}
	
	@Override
	protected LangSDKConfigBlock init_createPreferencesBlock(PreferencesPageContext prefContext) {
		return new LANGUAGE_SDKConfigBlock(prefContext);
	}
	
	public static class LANGUAGE_SDKConfigBlock extends LangSDKConfigBlock {
		
		public LANGUAGE_SDKConfigBlock(PreferencesPageContext prefContext) {
			super(prefContext);
		}
		
		@Override
		protected SDKLocationValidator getSDKValidator() {
			return new LANGUAGE_SDKLocationValidator();
		}
	}
	
}