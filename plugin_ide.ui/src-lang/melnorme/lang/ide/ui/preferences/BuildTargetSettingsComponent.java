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

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTarget.BuildTargetData;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.utils.ControlUtils;
import melnorme.util.swt.components.AbstractComponent;
import melnorme.util.swt.components.fields.EnablementButtonTextField;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.CommonGetter;

public class BuildTargetSettingsComponent extends AbstractComponent {
	
	protected final CommonGetter<String> getDefaultBuildTargetArguments;
	public final BuildArgumentsField buildArgumentsField;
	protected final CommonGetter<String> getDefaultProgramPath;
	public final EnablementButtonTextField programPathField;
	
	public BuildTargetSettingsComponent(
			CommonGetter<String> getDefaultBuildTargetArguments, CommonGetter<String> getDefaultProgramPath) {
		this.getDefaultBuildTargetArguments = assertNotNull(getDefaultBuildTargetArguments);
		this.getDefaultProgramPath = assertNotNull(getDefaultProgramPath);
		
		this.buildArgumentsField = init_createArgumentsField();
		this.programPathField = init_createProgramPathField();
	}
	
	protected BuildArgumentsField init_createArgumentsField() {
		return new BuildArgumentsField();
	}
	
	protected EnablementButtonTextField init_createProgramPathField() {	
		return new ProgramPathField();
	}
	
	protected BuildManager getBuildManager() {
		return LangCore.getBuildManager();
	}
	
	public String getEffectiveArgumentsValue() {
		return buildArgumentsField.getEffectiveFieldValue();
	}
	
	public String getEffectiveProgramPathValue() {
		return programPathField.getEffectiveFieldValue();
	}
	
	/* ----------------- bindings ----------------- */
	
	public void inputChanged(BuildTargetData buildTargetData) {
		buildArgumentsField.setEffectiveFieldValue(buildTargetData.buildArguments);
		programPathField.setEffectiveFieldValue(buildTargetData.artifactPath);
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
		buildArgumentsField.createComponent(topControl, 
			gdFillDefaults().grab(true, false).hint(200, SWT.DEFAULT).create());
		
		programPathField.createComponent(topControl, 
			new GridData(GridData.FILL_HORIZONTAL));
	}
	
	public void setEnabled(boolean enabled) {
		buildArgumentsField.setEnabled(enabled);
		programPathField.setEnabled(enabled);
	}

	public class BuildArgumentsField extends EnablementButtonTextField {
		
		public BuildArgumentsField() {
			super(LangUIMessages.Fields_BuildArguments, LABEL_UseDefault, LangUIMessages.Fields_VariablesButtonLabel);
			defaultTextStyle = SWT.MULTI | SWT.BORDER;
		}
		
		@Override
		protected void createContents_Label(Composite parent) {
			// Do not create label
		}
		
		@Override
		public int getPreferredLayoutColumns() {
			return 1;
		}
		
		@Override
		protected void createContents_layout() {
			text.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).hint(40, 100).create());
			button.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		}
		
		/* -----------------  ----------------- */
		
		@Override
		protected String getDefaultFieldValue() throws CommonException {
			return getDefaultBuildTargetArguments.get();
		}
		
		@Override
		protected String getNewValueFromButtonSelection() throws OperationCancellation {
			return getFieldValue() + ControlUtils.openStringVariableSelectionDialog(text.getShell());
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