/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 		steel - initial API and implementation
 * 		Bruno Medeiros - rewrite using lang code
 *******************************************************************************/
package com.googlecode.goclipse.ui.launch;

import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.core.operations.GoBuildManager;
import com.googlecode.goclipse.tooling.GoPackageName;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.launching.LaunchConstants;
import melnorme.lang.ide.ui.fields.ProjectRelativePathField;
import melnorme.lang.ide.ui.launch.MainLaunchConfigurationTab;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;
import melnorme.lang.tooling.data.StatusException;
import melnorme.util.swt.components.fields.ButtonTextField;
import melnorme.util.swt.components.fields.CheckBoxField;
import melnorme.util.swt.components.fields.ComboOptionsField;
import melnorme.util.swt.components.fields.EnablementButtonTextField;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.ArrayUtil;
import melnorme.utilbox.misc.StringUtil;

/**
 * Go Launch config tab uses the BuildTarget field in a different way.
 * In Go, the available targets are not predetermined by the Build model,
 * rather the user can specify its own Go Package path as a build target. 
 */
public class GoMainLaunchConfigurationTab extends MainLaunchConfigurationTab {
	
	protected final ButtonTextField goPackageField = init_createGoPackageField();
	protected final ComboOptionsField buildTypeField = init_createBuildTypeField();
	
	public GoMainLaunchConfigurationTab() {
		super(false);
		ArrayList2<String> fieldOptions = GoBuildManager.BUILD_TYPES_Names.toArrayList();
		fieldOptions.remove(GoBuildManager.BUILD_TYPE_RunTests);
		buildTypeField.setFieldOptions(fieldOptions);
		initBindings();
	}
	
	protected ButtonTextField init_createGoPackageField() {
		return new GoPackageField("Go package to build:", null, "Select...");
	}
	
	protected ComboOptionsField init_createBuildTypeField() {
		return new ComboOptionsField("Build type:");
	}
	
	@Override
	protected ProjectRelativePathField init_createProgramPathField() {
		return new MainLaunchTab_ProgramPathField() {
			@Override
			protected CheckBoxField createUseDefaultField(String enablementCheckBoxLabel) {
				return super.createUseDefaultField("Use default:");
			}
		};
	}
	
	@Override
	protected void initBindings() {
		super.initBindings();
		goPackageField.addValueChangedListener(this::buildTargetFieldChanged);
		buildTypeField.addValueChangedListener(this::buildTargetFieldChanged);
	}
	
	/* -----------------  Control creation  ----------------- */
	
	@Override
	protected void createCustomControls(Composite parent) {
		goPackageField.createComponent(parent, new GridData(GridData.FILL_HORIZONTAL));
//		buildTargetField.createComponent(parent, new GridData(GridData.FILL_HORIZONTAL));
		programPathField.createComponent(parent, new GridData(GridData.FILL_HORIZONTAL));
	}
	
	protected class GoPackageField extends EnablementButtonTextField {
		public GoPackageField(String labelText, String useDefaultField_Label, String buttonlabel) {
			super(labelText, useDefaultField_Label, buttonlabel);
		}
		
		@Override
		protected String getDefaultFieldValue() throws CommonException {
			return null;
		}
		
		@Override
		protected void createContents_all(Composite topControl) {
			super.createContents_all(topControl);
			
			buildTypeField.createComponent(topControl, new GridData(GridData.FILL_HORIZONTAL));
		}
		
		@Override
		protected String getNewValueFromButtonSelection() throws StatusException {
			return GoMainLaunchConfigurationTab.this.openProgramPathDialog(validateProject());
		}
	}
	
	protected String openProgramPathDialog(IProject project) {
		// TODO: this should be refactored to show only main packages
		
		try {
			
			ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), new LabelProvider() {
				@Override
				public String getText(Object element) {
					GoPackageName goPackageName = (GoPackageName) element;
					return goPackageName.getFullNameAsString();
				}
			});
			dialog.setTitle("Select Go main package");
			dialog.setMessage("Select Go main package");
			
			GoEnvironment goEnv = GoProjectEnvironment.getGoEnvironment(project);
			Collection<GoPackageName> sourcePackages = GoProjectEnvironment.getSourcePackages(project, goEnv);
			
			dialog.setElements(ArrayUtil.createFrom(sourcePackages));
			
			if (dialog.open() == IDialogConstants.OK_ID) {
				GoPackageName goPackageName = (GoPackageName) dialog.getFirstResult();
				return goPackageName.getFullNameAsString();
			}
			
		} catch (CoreException ce) {
			UIOperationExceptionHandler.handleOperationStatus("Error selecting package from dialog: ", ce);
		}
		return null;
	}
	
	/* -----------------  save/apply  ----------------- */
	
	@Override
	public void initializeFrom(ILaunchConfiguration config) {
		super.initializeFrom(config);
		String buildTargetName = getConfigAttribute(config, LaunchConstants.ATTR_BUILD_TARGET, "");
		String buildConfiguration = LangCore.getBuildManager().getBuildConfigString(buildTargetName);
		String buildTypeName = LangCore.getBuildManager().getBuildTypeString(buildTargetName);
		goPackageField.setFieldValue(buildConfiguration);
		buildTypeField.setFieldValue(buildTypeName);
		if(buildTypeField.getFieldValue() == null) {
			buildTypeField.setFieldValue(buildTypeField.getComboOptions().get(0));
		}
	}
	
	@Override
	protected String getBuildTargetName() {
		String buildType = buildTypeField.getFieldValue();
		return goPackageField.getFieldValue() + StringUtil.prefixStr("#", buildType); 
	}
	
}