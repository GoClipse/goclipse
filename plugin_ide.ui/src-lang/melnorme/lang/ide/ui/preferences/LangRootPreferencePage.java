/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.preferences;


import melnorme.lang.ide.core.bundlemodel.SDKPreferences;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.preferences.common.AbstractComponentsPrefPage;
import melnorme.lang.tooling.data.LocationValidator;
import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.data.StatusException.StatusLevel;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.MiscUtil;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public abstract class LangRootPreferencePage extends AbstractComponentsPrefPage {
	
	protected final LangSDKConfigBlock langSDKConfigBlock;
	
	public LangRootPreferencePage() {
		super(LangUIPlugin.getCorePrefStore());
		
		langSDKConfigBlock = createLangSDKConfigBlock();
	}
	
	protected LangSDKConfigBlock createLangSDKConfigBlock() {
		LangSDKConfigBlock langSDKConfigBlock = new LangSDKConfigBlock();
		
		connectStringField(SDKPreferences.SDK_PATH.key, langSDKConfigBlock.getLocationField(), 
			getSDKValidator());
		
		return langSDKConfigBlock;
	}
	
	protected abstract SDKLocationValidator getSDKValidator();
	
	@Override
	protected Control createContents(Composite parent) {
		return langSDKConfigBlock.createComponent(parent);
	}
	
	protected static abstract class SDKLocationValidator extends LocationValidator {
		
		public SDKLocationValidator() {
			directoryOnly = true;
		}
		
		@Override
		protected Location getValidatedField_rest(Location sdkLocation) throws StatusException {
			Location sdkExecutableLocation = getSDKExecutableLocation(sdkLocation);
			
			if(!sdkExecutableLocation.toFile().exists()) {
				throw new StatusException(StatusLevel.WARNING, getSDKExecutableErrorMessage(sdkExecutableLocation));
			}
			return sdkLocation;
		}
		
		protected Location getSDKExecutableLocation(Location location) {
			String exeRelativePath = getSDKExecutable_append();
			if(MiscUtil.OS_IS_WINDOWS) {
				exeRelativePath += ".exe"; 
			}
			return location.resolve_fromValid(exeRelativePath);
		}
		
		protected abstract String getSDKExecutable_append();
		
		protected abstract String getSDKExecutableErrorMessage(Location exeLocation);
	}
	
}