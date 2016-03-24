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
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTargetData;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.utils.ControlUtils;
import melnorme.util.swt.components.AbstractWidget;
import melnorme.util.swt.components.fields.EnablementButtonTextField;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.CommonGetter;

public class BuildTargetSettingsComponent extends AbstractWidget {
	
	protected final CommonGetter<String> getDefaultBuildTargetArguments;
	public final BuildArgumentsField buildArgumentsField;
	public final BuildArgumentsField checkArgumentsField;
	protected final CommonGetter<String> getDefaultProgramPath;
	public final EnablementButtonTextField programPathField;
	
	protected BuildTargetData buildTargetData = new BuildTargetData();
	
	public BuildTargetSettingsComponent(
			CommonGetter<String> getDefaultBuildArguments, 
			CommonGetter<String> getDefaultCheckArguments,
			CommonGetter<String> getDefaultProgramPath
	) {
		this.getDefaultBuildTargetArguments = assertNotNull(getDefaultBuildArguments);
		this.getDefaultProgramPath = assertNotNull(getDefaultProgramPath);
		
		buildArgumentsField = init_createArgumentsField();
		buildArgumentsField.addListener(() -> buildTargetData.buildArguments = getEffectiveBuildArgumentsValue());
		
		if(getDefaultCheckArguments == null) {
			checkArgumentsField = null;
		} else {
			checkArgumentsField = init_createCheckArgumentsField(getDefaultCheckArguments);
			checkArgumentsField.addListener(() -> buildTargetData.checkArguments = getEffectiveCheckArgumentsValue());
		}
		
		programPathField = init_createProgramPathField();
		programPathField.addListener(() -> buildTargetData.executablePath = getEffectiveProgramPathValue());
	}
	
	protected BuildManager getBuildManager() {
		return LangCore.getBuildManager();
	}
	
	protected BuildArgumentsField init_createArgumentsField() {
		return new BuildArgumentsField();
	}
	
	protected BuildArgumentsField init_createCheckArgumentsField(CommonGetter<String> getDefaultCheckArguments) {
		return new BuildArgumentsField() {
			@Override
			protected String getDefaultFieldValue() throws CommonException {
				return getDefaultCheckArguments.get();
			}
			
			@Override
			protected void doSetEnabled(boolean enabled) {
				super.doSetEnabled(enabled);
			}
		};
	}
	
	protected EnablementButtonTextField init_createProgramPathField() {	
		return new ProgramPathField();
	}
	
	/* ----------------- bindings ----------------- */
	
	public String getEffectiveBuildArgumentsValue() {
		return buildArgumentsField.getEffectiveFieldValue();
	}
	
	public String getEffectiveCheckArgumentsValue() {
		return checkArgumentsField.getEffectiveFieldValue();
	}
	
	public String getEffectiveProgramPathValue() {
		return programPathField.getEffectiveFieldValue();
	}
	
	public void inputChanged(BuildTargetData buildTargetData) {
		buildArgumentsField.setEffectiveFieldValue(buildTargetData.buildArguments);
		if(checkArgumentsField != null) {
			checkArgumentsField.setEffectiveFieldValue(buildTargetData.checkArguments);
		}
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
		
		if(checkArgumentsField == null) {
			buildArgumentsField.createComponent(topControl, 
				gdFillDefaults().grab(true, false).hint(200, SWT.DEFAULT).create());
		} else {
			
			TabFolder tabFolder = new TabFolder(topControl, SWT.NONE);
			
			TabItem buildArgsTab = new TabItem(tabFolder, SWT.NONE);
			buildArgsTab.setText("Build command");
			buildArgsTab.setControl(buildArgumentsField.createComponent(tabFolder));
			
			TabItem checkArgsTab = new TabItem(tabFolder, SWT.NONE);
			checkArgsTab.setText("Check command" );
			checkArgsTab.setControl(checkArgumentsField.createComponent(tabFolder));
			
			tabFolder.setLayoutData(
				gdFillDefaults().grab(true, false).hint(200, SWT.DEFAULT).create());
		}
		
		programPathField.createComponent(topControl, 
			new GridData(GridData.FILL_HORIZONTAL));
	}
	
	public void setEnabled(boolean enabled) {
		buildArgumentsField.setEnabled(enabled);
		programPathField.setEnabled(enabled);
		if(checkArgumentsField != null) {
			checkArgumentsField.setEnabled(enabled);
		}
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
		protected String getNewValueFromButtonSelection2() throws OperationCancellation {
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
		protected String getNewValueFromButtonSelection2() throws CommonException, OperationCancellation {
			return getFieldValue() + ControlUtils.openStringVariableSelectionDialog(text.getShell());
		}
		
	}
	
}