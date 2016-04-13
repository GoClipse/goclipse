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

import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildManagerMessages;
import melnorme.lang.ide.core.operations.build.BuildTargetData;
import melnorme.lang.ide.core.operations.build.VariablesResolver;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.utils.ControlUtils;
import melnorme.util.swt.components.AbstractWidget;
import melnorme.util.swt.components.fields.CheckBoxField;
import melnorme.util.swt.components.fields.EnablementButtonTextField;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.CommonGetter;

public class BuildTargetSettingsComponent extends AbstractWidget {
	
	protected final BuildManager buildManager = LangCore.getBuildManager();
	
	protected boolean createEnablementFields = true;
	
	protected final CheckBoxField normalEnableField;
	protected final CheckBoxField autoEnableField;
	
	protected final CommonGetter<String> getDefaultBuildTargetArguments;
	public final BuildArgumentsField buildArgumentsField;
	protected final CommonGetter<String> getDefaultProgramPath;
	public final EnablementButtonTextField programPathField;
	
	
	protected BuildTargetData btData = new BuildTargetData();
	
	public BuildTargetSettingsComponent(
			CommonGetter<String> getDefaultBuildArguments, 
			CommonGetter<String> getDefaultProgramPath
	) {
		this.getDefaultBuildTargetArguments = assertNotNull(getDefaultBuildArguments);
		this.getDefaultProgramPath = assertNotNull(getDefaultProgramPath);
		
		normalEnableField = new CheckBoxField(BuildManagerMessages.LABEL_EnableForNormalBuild);
		normalEnableField.addListener(() -> btData.normalBuildEnabled = normalEnableField.getBooleanFieldValue());
		
		autoEnableField = new CheckBoxField(BuildManagerMessages.LABEL_EnableForAutoBuild);
		autoEnableField.addListener(() -> btData.autoBuildEnabled = autoEnableField.getBooleanFieldValue());
		
		buildArgumentsField = init_createArgumentsField();
		buildArgumentsField.addListener(() -> btData.buildArguments = getEffectiveBuildArgumentsValue());
		
		programPathField = init_createProgramPathField();
		programPathField.addListener(() -> btData.executablePath = getEffectiveProgramPathValue());
	}
	
	protected BuildManager getBuildManager() {
		return buildManager;
	}
	
	protected BuildArgumentsField init_createArgumentsField() {
		VariablesResolver varResolver = buildManager.getToolManager().getVariablesManager(null);
		return new BuildArgumentsField(getDefaultBuildTargetArguments, varResolver);
	}
	
	protected EnablementButtonTextField init_createProgramPathField() {	
		return new ProgramPathField();
	}
	
	/* ----------------- bindings ----------------- */
	
	public String getEffectiveBuildArgumentsValue() {
		return buildArgumentsField.getEffectiveFieldValue();
	}
	
	public String getEffectiveProgramPathValue() {
		return programPathField.getEffectiveFieldValue();
	}
	
	public void inputChanged(BuildTargetData buildTargetData) {
		normalEnableField.setFieldValue(buildTargetData.normalBuildEnabled);
		autoEnableField.setFieldValue(buildTargetData.autoBuildEnabled);
		buildArgumentsField.setEffectiveFieldValue(buildTargetData.buildArguments);
		programPathField.setEffectiveFieldValue(buildTargetData.executablePath);
	}
	
	@Override
	protected void updateComponentFromInput() {
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public int getPreferredLayoutColumns() {
		return 1;
	}
	
	@Override
	protected void createContents(Composite topControl) {
		if(createEnablementFields) {
			normalEnableField.createComponent(topControl, horizontalExpandDefault());
			autoEnableField.createComponent(topControl, horizontalExpandDefault());
		}
		
		buildArgumentsField.createComponent(topControl, horizontalExpandDefault(200));
		programPathField.createComponent(topControl, horizontalExpandDefault());
	}
	
	public void setEnabled(boolean enabled) {
		normalEnableField.setEnabled(enabled);
		autoEnableField.setEnabled(enabled);
		buildArgumentsField.setEnabled(enabled);
		programPathField.setEnabled(enabled);
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