/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
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
import melnorme.lang.ide.core.launch.BuildTargetLaunchSettings;
import melnorme.lang.ide.core.launch.ProjectLaunchSettings;
import melnorme.lang.ide.ui.launch.MainLaunchConfigurationTab;
import melnorme.lang.ide.ui.utils.UIOperationsStatusHandler;
import melnorme.lang.tooling.data.StatusException;
import melnorme.util.swt.components.fields.ButtonTextField;
import melnorme.util.swt.components.fields.ComboOptionsField;
import melnorme.util.swt.components.fields.EnablementButtonTextField;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Collection2;
import melnorme.utilbox.core.CommonException;

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
	protected String getBuildTargetName() {
		String buildType = buildTypeField.getFieldValue();
		return getBuildManager().new BuildTargetName(goPackageField.getFieldValue(), buildType).getResolvedName();
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
		buildTargetSettings.createComponent(parent, new GridData(GridData.FILL_BOTH));
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
			return GoMainLaunchConfigurationTab.this.openProgramPathDialog(getValidProject());
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
			Collection2<GoPackageName> sourcePackages = GoProjectEnvironment.findSourcePackages(project, goEnv);
			dialog.setElements(sourcePackages.toArray());
			
			if (dialog.open() == IDialogConstants.OK_ID) {
				GoPackageName goPackageName = (GoPackageName) dialog.getFirstResult();
				return goPackageName.getFullNameAsString();
			}
			
		} catch (CoreException ce) {
			UIOperationsStatusHandler.handleOperationStatus("Error selecting package from dialog: ", ce);
		}
		return null;
	}
	
	/* -----------------  save/apply  ----------------- */
	
	@Override
	protected ProjectLaunchSettings getDefaultProjectLaunchSettings() {
		return new BuildTargetLaunchSettings() {
			@Override
			public ProjectLaunchSettings initFrom(IResource contextualResource) {
				ProjectLaunchSettings initFrom = super.initFrom(contextualResource);
				buildTargetName = "";
				return initFrom;
			}
		};
	}
	
	@Override
	protected void initializeBuildTargetField(BuildTargetLaunchSettings buildSettings) {
		//super.initializeBuildTargetField(buildSettings);
		
		String buildTargetName = buildSettings.buildTargetName;
		String buildConfiguration = LangCore.getBuildManager().getBuildConfigString(buildTargetName);
		String buildTypeName = LangCore.getBuildManager().getBuildTypeString(buildTargetName);
		goPackageField.setFieldValue(buildConfiguration);
		buildTypeField.setFieldValue(buildTypeName);
		if(buildTypeField.getFieldValue() == null) {
			buildTypeField.setFieldValue(buildTypeField.getComboOptions().get(0));
		}
	}
	
}