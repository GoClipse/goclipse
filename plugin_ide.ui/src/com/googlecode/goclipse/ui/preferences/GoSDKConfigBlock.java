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

import static melnorme.lang.tooling.ops.util.PathValidator.LocationKind.FILE_ONLY;
import static melnorme.utilbox.core.CoreUtil.array;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.googlecode.goclipse.core.GoEnvironmentPrefs;
import com.googlecode.goclipse.tooling.GoSDKLocationValidator;
import com.googlecode.goclipse.tooling.env.GoArch;
import com.googlecode.goclipse.tooling.env.GoOs;

import melnorme.lang.ide.ui.preferences.AbstractCompositePreferencesBlock;
import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesBlock2;
import melnorme.lang.ide.ui.preferences.common.PreferencesPageContext;
import melnorme.lang.tooling.data.IValidatableValue.ValidatableField;
import melnorme.lang.tooling.ops.util.LocationValidator;
import melnorme.lang.utils.EnvUtils;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.fields.CheckBoxField;
import melnorme.util.swt.components.fields.ComboBoxField;
import melnorme.util.swt.components.fields.DirectoryTextField;
import melnorme.util.swt.components.fields.EnablementButtonTextField;
import melnorme.util.swt.components.fields.FileTextField;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.ArrayUtil;
import melnorme.utilbox.misc.MiscUtil;

public class GoSDKConfigBlock extends AbstractCompositePreferencesBlock {
	
	public final DirectoryTextField goRootField = new DirectoryTextField("GO&ROOT:");
	protected final GoSDKLocationValidator goSDKLocationValidator = new GoSDKLocationValidator();
	public final ValidatableField<String> validatedGoRoot = new ValidatableField<>(goRootField, goSDKLocationValidator);
	
	protected final FileTextField goFmtPath = new FileTextField("gofmt:");
	protected final FileTextField goDocPath = new FileTextField("godoc:");
	
	protected final ComboBoxField goOSField = new ComboBoxField("G&OOS:",
		ArrayUtil.prepend("<default>", GoOs.GOOS_VALUES),
		ArrayUtil.prepend("", GoOs.GOOS_VALUES));
	protected final ComboBoxField goArchField = new ComboBoxField("GO&ARCH:", 
		array("<default>", GoArch.ARCH_AMD64, GoArch.ARCH_386,  GoArch.ARCH_ARM), 
		array(""         , GoArch.ARCH_AMD64, GoArch.ARCH_386, GoArch.ARCH_ARM));
	
	protected final EnablementButtonTextField goPathField = new GoPathField();
	protected final CheckBoxField gopathAppendProjectLocField = new CheckBoxField(
		"Also add project location to GOPATH, if it's not contained there already.");
	
	public GoSDKConfigBlock(PreferencesPageContext prefContext) {
		super(prefContext);
		
		subComponents.add(goRootField);
		subComponents.add(goFmtPath);
		subComponents.add(goDocPath);
		subComponents.add(goOSField);
		subComponents.add(goArchField);
		
		subComponents.add(goPathField);
		subComponents.add(gopathAppendProjectLocField);
		
		prefContext.bindToPreference(goRootField, GoEnvironmentPrefs.GO_ROOT);
		prefContext.bindToPreference(goFmtPath, GoEnvironmentPrefs.FORMATTER_PATH);
		prefContext.bindToPreference(goDocPath, GoEnvironmentPrefs.DOCUMENTOR_PATH);
		
		prefContext.bindToPreference(goOSField.asStringProperty(), GoEnvironmentPrefs.GO_OS);
		prefContext.bindToPreference(goArchField.asStringProperty(), GoEnvironmentPrefs.GO_ARCH);
		
		prefContext.bindToPreference(goPathField.asEffectiveValueProperty2(), GoEnvironmentPrefs.GO_PATH);
		prefContext.bindToPreference(gopathAppendProjectLocField, GoEnvironmentPrefs.APPEND_PROJECT_LOC_TO_GOPATH);
		
		
		validation.addFieldValidation(true, goRootField, goSDKLocationValidator);
		
		validation.addFieldValidation(true, goFmtPath, new LocationValidator(goFmtPath.getLabelText(), FILE_ONLY));
		validation.addFieldValidation(true, goDocPath, new LocationValidator(goDocPath.getLabelText(), FILE_ONLY));
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
		
		int numColumns = 3;
		Group goSDK = AbstractPreferencesBlock2.createOptionsSection(topControl, "Go installation:", numColumns,
			getPreferenceGroupDefaultLayout());
		
		goRootField.createComponentInlined(goSDK);
		
		goFmtPath.createComponentInlined(goSDK);
		goDocPath.createComponentInlined(goSDK);
		
		SWTFactoryUtil.createLabel(goSDK, 
			SWT.SEPARATOR | SWT.HORIZONTAL, "",
			gdFillDefaults().span(numColumns, 1).grab(true, false).indent(0, 5).create());
		
		goOSField.createComponentInlined(goSDK);
		goArchField.createComponentInlined(goSDK);
		
		/* -----------------  ----------------- */
		
		Composite goPathFieldTopControl = goPathField.createComponent(topControl, getPreferenceGroupDefaultLayout());
		gopathAppendProjectLocField.createComponent(goPathFieldTopControl);
		
		goRootField.addListener(() -> handleGoRootChange());
	}
	
	protected GridData getPreferenceGroupDefaultLayout() {
		return GridDataFactory.fillDefaults().grab(true, false).minSize(300, SWT.DEFAULT).create();
	}
	
	protected void handleGoRootChange() {
		IPath gorootPath = new Path(goRootField.getFieldValue());
		
		if(validatedGoRoot.getValidationStatusLevel().isOkStatus()) {
			
			String goFmtName = "gofmt" + MiscUtil.getExecutableSuffix();
			setValueIfFileExists(goFmtPath, gorootPath.append("bin").append(goFmtName).toFile());
			
			String goDocName = "godoc" + MiscUtil.getExecutableSuffix();
			setValueIfFileExists(goDocPath, gorootPath.append("bin").append(goDocName).toFile());
		}
	}
	
	protected void setValueIfFileExists(FileTextField fileField, File filePath) {
		if (filePath != null && filePath.exists() && filePath.isFile()) {
			fileField.setFieldValue(filePath.getAbsolutePath());
		} else {
			fileField.setFieldValue("");
		}
	}
	
	/* -----------------  ----------------- */
	
	public static class GoPathField extends EnablementButtonTextField {
		
		public GoPathField() {
			super("GOPATH:", "Use same as GOPATH environment variable.", "Add Folder");
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