/*******************************************************************************
 * Copyright (c) 2013 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui.wizards;


import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.wizard.WizardPage;

import com.googlecode.goclipse.core.GoEnvironmentPrefs;
import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.tooling.GoSDKLocationValidator;
import com.googlecode.goclipse.ui.GoPluginImages;

import melnorme.lang.ide.ui.WizardMessages_Actual;
import melnorme.lang.ide.ui.dialogs.LangNewProjectWizard;
import melnorme.lang.ide.ui.dialogs.LangProjectWizardFirstPage;
import melnorme.lang.tooling.data.ValidationException;
import melnorme.utilbox.core.CommonException;

/**
 * Go New Project Wizard.
 */
public class GoProjectWizard extends LangNewProjectWizard {
	
	protected final GoProjectWizardFirstPage firstPage = new GoProjectWizardFirstPage();
	
	@Override
	public LangProjectWizardFirstPage getFirstPage() {
		return firstPage;
	}
	
	@Override
	public WizardPage getSecondPage() {
		return null;
	}
	
	@Override
	public void addPages() {
		addPage(firstPage);
	}
	
	@Override
	protected void configureCreatedProject(ProjectCreator_ForWizard projectCreator, IProgressMonitor monitor)
			throws CoreException, CommonException {
		IProject project = getProject();
		if(!GoProjectEnvironment.isProjectInsideGoPathSourceFolder(project)) {
			projectCreator.createFolder(project.getFolder("src"), monitor);
			projectCreator.createFolder(project.getFolder("bin"), monitor);
			projectCreator.createFolder(project.getFolder("pkg"), monitor);
		}
	}
	
}

class GoProjectWizardFirstPage extends LangProjectWizardFirstPage {
	
	public GoProjectWizardFirstPage() {
		setTitle(WizardMessages_Actual.LangNewProject_Page1_pageTitle);
		setDescription(WizardMessages_Actual.LangNewProject_Page1_pageDescription);
		
		setImageDescriptor(GoPluginImages.WIZARD_ICON.getDescriptor());
	}
	
	@Override
	protected void validatePreferences() throws ValidationException {
		 new GoSDKLocationValidator().getValidatedField(GoEnvironmentPrefs.GO_ROOT.get());
	}
	
}