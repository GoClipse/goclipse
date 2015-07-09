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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.tooling.GoPackageName;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

import melnorme.lang.ide.ui.launch.MainLaunchConfigurationTab;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;
import melnorme.utilbox.misc.ArrayUtil;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.StringUtil;

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
	
	@Override
	protected void programPathField_setDefaults(IResource contextualResource, ILaunchConfigurationWorkingCopy config) {
		// TODO: figure out defaults for this field
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
					Location projectLocation = Location.create_fromValid(project.getLocation().toFile().toPath());
					GoPackageName projectGoPackage = 
							goEnv.getGoPath().findGoPackageForSourceFile(projectLocation.resolve_valid("dummy.go"));
					
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