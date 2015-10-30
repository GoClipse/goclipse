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

import melnorme.lang.ide.ui.preferences.LangSDKConfigBlock;
import melnorme.lang.ide.ui.preferences.pages.RootPreferencePage;
import melnorme.lang.tooling.data.LANGUAGE_SDKLocationValidator;
import melnorme.lang.tooling.ops.SDKLocationValidator;


/**
 * The main/root preference page.
 */
public class LANGUAGE_RootPreferencePage extends RootPreferencePage {
	
	public LANGUAGE_RootPreferencePage() {
		super();
	}
	
	@Override
	protected LangSDKConfigBlock init_createLangSDKConfigBlock() {
		return new LANGUAGE_SDKConfigBlock();
	}
	
	public static class LANGUAGE_SDKConfigBlock extends LangSDKConfigBlock {
		
		public LANGUAGE_SDKConfigBlock() {
			super(null);
		}
		
		@Override
		protected SDKLocationValidator getSDKValidator() {
			return new LANGUAGE_SDKLocationValidator();
		}
	}
	
}