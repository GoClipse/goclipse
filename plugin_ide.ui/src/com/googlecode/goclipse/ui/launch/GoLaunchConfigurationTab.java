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

import java.nio.file.Path;
import java.util.Collection;

import melnorme.lang.ide.launching.LaunchConstants;
import melnorme.lang.ide.ui.launch.LangArgumentsBlock2;
import melnorme.lang.ide.ui.launch.LangWorkingDirectoryBlock;
import melnorme.lang.ide.ui.launch.MainLaunchConfigurationTab;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;
import melnorme.utilbox.misc.ArrayUtil;
import melnorme.utilbox.misc.StringUtil;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.WorkingDirectoryBlock;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.tooling.GoPackageName;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

public class GoLaunchConfigurationTab extends MainLaunchConfigurationTab {
	
	public GoLaunchConfigurationTab() {
	}
	
	@Override
	protected Launch_ProgramPathField createProgramPathField() {
		return new Launch_ProgramPathField() {
			@Override
			protected String getGroupLabel() {
				return "Go main package (path relative to project)";
			}
		};
	}
	
	protected final LangArgumentsBlock2 argumentsBlock = new LangArgumentsBlock2() {
		@Override
		protected void fieldValueChanged() {
			updateLaunchConfigurationDialog();
		};
	};
	protected final WorkingDirectoryBlock workingDirectoryBlock = new LangWorkingDirectoryBlock();
	
	@Override
	protected void createCustomControls(Composite parent) {
		super.createCustomControls(parent);
		
		argumentsBlock.createComponent(parent, new GridData(GridData.FILL_BOTH));
		workingDirectoryBlock.createControl(parent);
	}
	
	@Override
	public void setLaunchConfigurationDialog(ILaunchConfigurationDialog dialog) {
		super.setLaunchConfigurationDialog(dialog);
		workingDirectoryBlock.setLaunchConfigurationDialog(dialog);
	}
	
	@Override
	protected void setDefaults(IResource contextualResource, ILaunchConfigurationWorkingCopy config) {
		super.setDefaults(contextualResource, config);
		
		config.setAttribute(LaunchConstants.ATTR_PROGRAM_ARGUMENTS, "");
		workingDirectoryBlock.setDefaults(config);
	}
	
	@Override
	protected void programPathField_setDefaults(IResource contextualResource, ILaunchConfigurationWorkingCopy config) {
		// TODO: figure out defaults for this field
	}
	
	@Override
	public void initializeFrom(ILaunchConfiguration config) {
		super.initializeFrom(config);
		
		String programargs = getConfigAttribute(config, LaunchConstants.ATTR_PROGRAM_ARGUMENTS, "");
		
		argumentsBlock.setFieldValue(programargs);
		workingDirectoryBlock.initializeFrom(config);
	}
	
	@Override
	protected void doPerformApply(ILaunchConfigurationWorkingCopy config) {
		super.doPerformApply(config);
		
		config.setAttribute(LaunchConstants.ATTR_PROGRAM_ARGUMENTS, argumentsBlock.getFieldValue());
		workingDirectoryBlock.performApply(config);
	}
	
	
	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		return super.isValid(launchConfig) && workingDirectoryBlock.isValid(launchConfig);
	}
	
	@Override
	public String getErrorMessage() {
		if (super.getErrorMessage() != null) {
			return super.getErrorMessage();
		} else {
			return workingDirectoryBlock.getErrorMessage();
		}
	}
	
	@Override
	protected void doValidate() {
 		super.doValidate();
	}
	
	@Override
	protected void validateProgramPath() {
		// Ignore validation
		//super.validateProgramPath();
	}
	
	@Override
	protected void openProgramPathDialog(IProject project) {
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
				String packageResourcePath = goPackageName.getFullNameAsString();
				
				if(!GoProjectEnvironment.isProjectInsideGoPath(project, goEnv.getGoPath())) {
					packageResourcePath = "src/" + packageResourcePath;
				} else {
					Path projectLocation = project.getLocation().toFile().toPath();
					GoPackageName projectGoPackage = 
							goEnv.getGoPath().findGoPackageForSourceFile(projectLocation.resolve("dummy.go"));
					
					// snip project base name.
					packageResourcePath = StringUtil.segmentAfterMatch(packageResourcePath, 
						projectGoPackage.getFullNameAsString() + "/");
					
				}
				// check extension
				programPathField.setFieldValue(packageResourcePath);
			}
			
		} catch (CoreException ce) {
			UIOperationExceptionHandler.handleOperationStatus("Error selecting package from dialog: ", ce);
		}
	}
	
}