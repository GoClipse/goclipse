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
package com.googlecode.goclipse.ui.preferences;

import java.io.File;

import com.googlecode.goclipse.core.GoEnvironmentPrefs;
import com.googlecode.goclipse.tooling.GoSDKLocationValidator;

import melnorme.lang.ide.ui.preferences.LangSDKConfigBlock;
import melnorme.lang.ide.ui.preferences.common.PreferencesPageContext;
import melnorme.lang.utils.EnvUtils;
import melnorme.lang.utils.validators.PathValidator;
import melnorme.util.swt.components.fields.ButtonTextField;
import melnorme.util.swt.components.fields.CheckBoxField;
import melnorme.util.swt.components.fields.DirectoryTextField;
import melnorme.util.swt.components.fields.EnablementButtonTextField2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class GoSDKConfigBlock extends LangSDKConfigBlock {
	
	public GoSDKConfigBlock(PreferencesPageContext prefContext) {
		super(prefContext);
		
		addChildWidget(new GoPathGroup());
		
	}
	
	@Override
	protected PathValidator getSDKValidator() {
		return new GoSDKLocationValidator();
	}
	

	/* -----------------  ----------------- */
	
	public class GoPathGroup extends EnablementButtonTextField2 {
		
		protected final CheckBoxField gopathAppendProjectLocField;
		
		public GoPathGroup() {
			super("Eclipse GOPATH:", "Use same value as the GOPATH environment variable.");
			
			prefContext.bindToPreference(asEffectiveValueProperty(), GoEnvironmentPrefs.GO_PATH);
			
			this.gopathAppendProjectLocField = new CheckBoxField(
					"Also add project location to GOPATH, if it's not contained there already.");
			addChildWidget(gopathAppendProjectLocField);
			prefContext.bindToPreference(gopathAppendProjectLocField, GoEnvironmentPrefs.APPEND_PROJECT_LOC_TO_GOPATH);
		}
		
		@Override
		protected ButtonTextField init_createButtonTextField() {
			return new ButtonTextField(null, "Add Folder") {
				@Override
				protected String getNewValueFromButtonSelection() throws CommonException, OperationCancellation {
					String newValue = DirectoryTextField.openDirectoryDialog("", text.getShell());
					return getFieldValue() + File.pathSeparator + newValue;
				}
			};
		}
		
		@Override
		protected String getDefaultFieldValue() throws CommonException {
			return EnvUtils.getVarFromEnvMap(System.getenv(), "GOPATH");
		}
		
	}
	
}