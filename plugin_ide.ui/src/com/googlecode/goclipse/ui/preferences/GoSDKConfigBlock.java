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

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.googlecode.goclipse.core.GoEnvironmentPrefs;
import com.googlecode.goclipse.tooling.GoSDKLocationValidator;

import melnorme.lang.ide.ui.preferences.AbstractCompositePreferencesBlock;
import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesBlock2;
import melnorme.lang.ide.ui.preferences.common.PreferencesPageContext;
import melnorme.lang.tooling.data.IValidatableValue.ValidatableField;
import melnorme.lang.utils.EnvUtils;
import melnorme.util.swt.components.fields.CheckBoxField;
import melnorme.util.swt.components.fields.DirectoryTextField;
import melnorme.util.swt.components.fields.EnablementButtonTextField;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class GoSDKConfigBlock extends AbstractCompositePreferencesBlock {
	
	public final DirectoryTextField goRootField = new DirectoryTextField("Directory:");
	protected final GoSDKLocationValidator goSDKLocationValidator = new GoSDKLocationValidator();
	public final ValidatableField<String> validatedGoRoot = new ValidatableField<>(goRootField, goSDKLocationValidator);
	
	protected final EnablementButtonTextField goPathField = new GoPathField();
	protected final CheckBoxField gopathAppendProjectLocField = new CheckBoxField(
		"Also add project location to GOPATH, if it's not contained there already.");
	
	public GoSDKConfigBlock(PreferencesPageContext prefContext) {
		super(prefContext);
		
		addSubComponent(goRootField);
		
		addSubComponent(goPathField);
		addSubComponent(gopathAppendProjectLocField);
		
		prefContext.bindToPreference(goRootField, GoEnvironmentPrefs.GO_ROOT);
		
		prefContext.bindToPreference(goPathField.asEffectiveValueProperty2(), GoEnvironmentPrefs.GO_PATH);
		prefContext.bindToPreference(gopathAppendProjectLocField, GoEnvironmentPrefs.APPEND_PROJECT_LOC_TO_GOPATH);
		
		
		validation.addFieldValidation(true, goRootField, goSDKLocationValidator);
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public int getPreferredLayoutColumns() {
		return 1;
	}
	
	@Override
	protected void createContents(Composite topControl) {
		// Ignore super
		//super.createContents(topControl);
		
		Group goSDK = AbstractPreferencesBlock2.createOptionsSection(topControl, "Go installation (GOROOT):", 3,
			createSubComponentDefaultGridData());
		
		goRootField.createComponentInlined(goSDK);
		
		/* -----------------  ----------------- */
		
		Composite goPathFieldTopControl = goPathField.createComponent(topControl, 
			createSubComponentDefaultGridData());
		gopathAppendProjectLocField.createComponent(goPathFieldTopControl);
		
		goRootField.addListener(() -> handleGoRootChange());
	}
	
	@Override
	protected GridData createSubComponentDefaultGridData() {
		return GridDataFactory.fillDefaults().grab(true, false).minSize(300, SWT.DEFAULT).create();
	}
	
	protected void handleGoRootChange() {
	}
	
	/* -----------------  ----------------- */
	
	public static class GoPathField extends EnablementButtonTextField {
		
		public GoPathField() {
			super("Eclipse GOPATH:", "Use same as the GOPATH environment variable.", "Add Folder");
		}
		
		@Override
		protected String getDefaultFieldValue() throws CommonException {
			return EnvUtils.getVarFromEnvMap(System.getenv(), "GOPATH");
		}
		
		@Override
		protected String getNewValueFromButtonSelection2() throws CommonException, OperationCancellation {
			String newValue = DirectoryTextField.openDirectoryDialog("", text.getShell());
			return getFieldValue() + File.pathSeparator + newValue;
		}
		
	}
	
}