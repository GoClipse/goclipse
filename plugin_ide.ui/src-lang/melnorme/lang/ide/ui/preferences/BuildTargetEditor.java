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
package melnorme.lang.ide.ui.preferences;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildManagerMessages;
import melnorme.lang.ide.core.operations.build.BuildTargetData;
import melnorme.lang.ide.core.operations.build.BuildTargetDataView;
import melnorme.lang.ide.core.operations.build.VariablesResolver;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.utils.ControlUtils;
import melnorme.util.swt.components.AbstractCompositeWidget;
import melnorme.util.swt.components.fields.CheckBoxField;
import melnorme.util.swt.components.fields.EnablementButtonTextField;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.CommonGetter;

public class BuildTargetEditor extends AbstractCompositeWidget {
	
	protected final BuildManager buildManager;
	
	protected final CheckBoxField normalEnableField;
	protected final CheckBoxField autoEnableField;
	
	protected final CommonGetter<String> getDefaultBuildCommand;
	public final CommandInvocationEditor buildCommandField;
	protected final CommonGetter<String> getDefaultProgramPath;
	public final EnablementButtonTextField programPathField;
	
	protected BuildTargetData btData = new BuildTargetData();
	
	public BuildTargetEditor(
			BuildManager buildManager,
			boolean createEnablementFields,
			CommonGetter<String> getDefaultBuildCommand, 
			CommonGetter<String> getDefaultProgramPath
	) {
		super(false);
		this.buildManager = assertNotNull(buildManager);
		
		this.getDefaultBuildCommand = assertNotNull(getDefaultBuildCommand);
		this.getDefaultProgramPath = assertNotNull(getDefaultProgramPath);
		
		normalEnableField = new CheckBoxField(BuildManagerMessages.LABEL_EnableForNormalBuild);
		normalEnableField.addListener(() -> btData.normalBuildEnabled = normalEnableField.getBooleanFieldValue());
		
		autoEnableField = new CheckBoxField(BuildManagerMessages.LABEL_EnableForAutoBuild);
		autoEnableField.addListener(() -> btData.autoBuildEnabled = autoEnableField.getBooleanFieldValue());
		
		if(createEnablementFields) {
			addSubComponents(normalEnableField, autoEnableField);
		}
		
		buildCommandField = addSubComponent(init_createArgumentsField());
		buildCommandField.addListener(() -> btData.buildArguments = getEffectiveBuildArgumentsValue());
		
		programPathField = addSubComponent(init_createProgramPathField());
		programPathField.addListener(() -> btData.executablePath = getEffectiveProgramPathValue());
//		buildCommandField.addListener(() -> programPathField.updateDefaultFieldValue());
	}
	
	protected BuildManager getBuildManager() {
		return buildManager;
	}
	
	protected CommandInvocationEditor init_createArgumentsField() {
		VariablesResolver varResolver = buildManager.getToolManager().getVariablesManager(null);
		return new CommandInvocationEditor(getDefaultBuildCommand, varResolver);
	}
	
	protected EnablementButtonTextField init_createProgramPathField() {	
		return new ProgramPathField();
	}
	
	/* ----------------- bindings ----------------- */
	
	public String getEffectiveBuildArgumentsValue() {
		return buildCommandField.getEffectiveFieldValue();
	}
	
	public String getEffectiveProgramPathValue() {
		return programPathField.getEffectiveFieldValue();
	}
	
	public void inputChanged(BuildTargetDataView buildTargetData) {
		normalEnableField.setFieldValue(buildTargetData.isNormalBuildEnabled());
		autoEnableField.setFieldValue(buildTargetData.isAutoBuildEnabled());
		buildCommandField.setEffectiveFieldValue(buildTargetData.getBuildArguments());
		programPathField.setEffectiveFieldValue(buildTargetData.getExecutablePath());
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public int getPreferredLayoutColumns() {
		return 1;
	}
	
	public class ProgramPathField extends EnablementButtonTextField {
		
		public ProgramPathField() {
			super(
				LangUIMessages.BuildTargetSettings_ProgramPathField_title, 
				EnablementButtonTextField.LABEL_UseDefault, 
				LangUIMessages.Fields_VariablesButtonLabel
			);
		}
		
		@Override
		protected String getDefaultFieldValue() throws CommonException {
			return getDefaultProgramPath.get();
		}
		
		@Override
		protected String getNewValueFromButtonSelection2() throws CommonException, OperationCancellation {
			return getFieldValue() + ControlUtils.openStringVariableSelectionDialog(text.getShell());
		}
		
	}
	
}