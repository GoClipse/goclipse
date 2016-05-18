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
package melnorme.lang.ide.ui.build;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildManagerMessages;
import melnorme.lang.ide.core.operations.build.BuildTargetData;
import melnorme.lang.ide.core.operations.build.VariablesResolver;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.utils.ControlUtils;
import melnorme.lang.tooling.commands.CommandInvocation;
import melnorme.lang.tooling.commands.CommandInvocation.ValidatedCommandArgumentsSource;
import melnorme.util.swt.components.CompositeWidget;
import melnorme.util.swt.components.fields.CheckBoxField;
import melnorme.util.swt.components.fields.EnablementButtonTextField;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.CommonGetter;
import melnorme.utilbox.status.StatusException;

public class BuildTargetEditor extends CompositeWidget {
	
	protected final BuildManager buildManager;
	
	protected final CheckBoxField normalEnableField;
	protected final CheckBoxField autoEnableField;
	
	protected final CommonGetter<CommandInvocation> getDefaultBuildCommand;
	public final CommandInvocationEditor buildCommandField;
	protected final CommonGetter<String> getDefaultProgramPath;
	public final EnablementButtonTextField programPathField;
	
	protected BuildTargetData btData = new BuildTargetData();
	
	public BuildTargetEditor(
			BuildManager buildManager,
			boolean createEnablementFields,
			CommonGetter<CommandInvocation> getDefaultBuildCommand, 
			CommonGetter<String> getDefaultProgramPath
	) {
		super(false);
		this.buildManager = assertNotNull(buildManager);
		
		this.getDefaultBuildCommand = assertNotNull(getDefaultBuildCommand);
		this.getDefaultProgramPath = assertNotNull(getDefaultProgramPath);
		
		normalEnableField = new CheckBoxField(BuildManagerMessages.LABEL_EnableForNormalBuild);
		normalEnableField.addListener((newValue) -> btData.normalBuildEnabled = newValue);
		
		autoEnableField = new CheckBoxField(BuildManagerMessages.LABEL_EnableForAutoBuild);
		autoEnableField.addListener((newValue) -> btData.autoBuildEnabled = newValue);
		
		if(createEnablementFields) {
			addChildWidget(normalEnableField, autoEnableField);
		}
		
		buildCommandField = addChildWidget(init_createArgumentsField());
		buildCommandField.addEffectiveValueChangeListener(() -> {
			btData.buildCommand = getEffectiveBuildCommand();
		});
		
		programPathField = addChildWidget(init_createProgramPathField());
		programPathField.addListener((__) -> btData.executablePath = getEffectiveProgramPathValue());
//		buildCommandField.addListener(() -> programPathField.updateDefaultFieldValue());
	}
	
	protected BuildManager getBuildManager() {
		return buildManager;
	}
	
	protected CommandInvocationEditor init_createArgumentsField() {
		VariablesResolver varResolver = buildManager.getToolManager().getVariablesManager(null);
		return new BuildCommandEditor(getDefaultBuildCommand, varResolver);
	}
	
	protected EnablementButtonTextField init_createProgramPathField() {	
		return new ProgramPathField();
	}
	
	/* ----------------- input ----------------- */
	
	public CommandInvocation getEffectiveBuildCommand() {
		return buildCommandField.getEffectiveFieldValue();
	}
	
	public String getEffectiveProgramPathValue() {
		return programPathField.getEffectiveFieldValue();
	}
	
	public void setInput(BuildTargetData buildTargetData) {
		this.btData = buildTargetData; 
		normalEnableField.setFieldValue(buildTargetData.isNormalBuildEnabled());
		autoEnableField.setFieldValue(buildTargetData.isAutoBuildEnabled());
		buildCommandField.setEffectiveFieldValue(buildTargetData.getBuildCommand());
		programPathField.setEffectiveFieldValue(buildTargetData.getExecutablePath());
	}
	
	/* -----------------  ----------------- */
	
	public static class BuildCommandEditor extends CommandInvocationEditor {
		
		public BuildCommandEditor(CommonGetter<CommandInvocation> getDefaultBuildCommand,
				VariablesResolver variablesResolver) {
			super(getDefaultBuildCommand, variablesResolver);
			
			this.label = LangUIMessages.Fields_BuildCommand;
		}
		
		@Override
		protected void validateArguments() throws StatusException {
			try {
				super.validateArguments();
			} catch(StatusException se) {
				if(se.getMessage() == ValidatedCommandArgumentsSource.MSG_NO_COMMAND_SPECIFIED) {
					throw new StatusException("No build command supplied.");
				}
			}
		}
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
		protected String getNewValueFromButtonSelection() throws CommonException, OperationCancellation {
			return getFieldValue() + ControlUtils.openStringVariableSelectionDialog(text.getShell());
		}
		
	}
	
}