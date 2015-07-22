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
import com.googlecode.goclipse.tooling.GoPackageName;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

import melnorme.lang.ide.launching.LaunchConstants;
import melnorme.lang.ide.ui.fields.ProjectRelativePathField;
import melnorme.lang.ide.ui.launch.MainLaunchConfigurationTab;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;
import melnorme.lang.tooling.data.StatusException;
import melnorme.util.swt.components.fields.ButtonTextField;
import melnorme.util.swt.components.fields.CheckBoxField;
import melnorme.util.swt.components.fields.EnablementButtonTextField;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.ArrayUtil;

/**
 * Go Launch config tab uses the BuildTarget field in a different way.
 * In Go, the available targets are not predetermined by the Build model,
 * rather the user can specify its own Go Package path as a build target. 
 */
public class GoMainLaunchConfigurationTab extends MainLaunchConfigurationTab {
	
	protected final ButtonTextField goPackageField = createGoPackageField();
	
	public GoMainLaunchConfigurationTab() {
		super(false);
		initBindings();
	}
	
	protected ButtonTextField createGoPackageField() {
		return new EnablementButtonTextField("Go package to build:", null, "Select...") {
			@Override
			protected String getDefaultFieldValue() throws CommonException {
				return null;
			}
			
			@Override
			protected String getNewValueFromButtonSelection() throws StatusException {
				return GoMainLaunchConfigurationTab.this.openProgramPathDialog(validateProject());
			}
			
		};
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
	protected void createCustomControls(Composite parent) {
		goPackageField.createComponent(parent, new GridData(GridData.FILL_HORIZONTAL));
//		buildTargetField.createComponent(parent, new GridData(GridData.FILL_HORIZONTAL));
		programPathField.createComponent(parent, new GridData(GridData.FILL_HORIZONTAL));
	}
	
	@Override
	protected void initBindings() {
		super.initBindings();
		goPackageField.addValueChangedListener(this::buildTargetFieldChanged);
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public void initializeFrom(ILaunchConfiguration config) {
		super.initializeFrom(config);
		goPackageField.setFieldValue(getConfigAttribute(config, LaunchConstants.ATTR_BUILD_TARGET, ""));
	}
	
	@Override
	protected String getBuildTargetName() {
		return goPackageField.getFieldValue();
	}
	
	/* -----------------  ----------------- */
	
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
	
}