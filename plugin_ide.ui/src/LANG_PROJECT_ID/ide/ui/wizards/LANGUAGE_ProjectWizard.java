/*******************************************************************************
 * Copyright (c) 2013, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package LANG_PROJECT_ID.ide.ui.wizards;


import melnorme.lang.ide.core.operations.SDKPreferences;
import melnorme.lang.ide.ui.WizardMessages_Actual;
import melnorme.lang.ide.ui.dialogs.LangNewProjectWizard;
import melnorme.lang.ide.ui.dialogs.LangProjectWizardFirstPage;
import melnorme.lang.tooling.data.AbstractValidator.ValidationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.wizard.WizardPage;

import LANG_PROJECT_ID.ide.core.operations.LANGUAGE_Builder.LANGUAGE_SDKLocationValidator;

/**
 * LANGUAGE New Project Wizard.
 */
public class LANGUAGE_ProjectWizard extends LangNewProjectWizard {
	
	protected final LANGUAGE_ProjectWizardFirstPage firstPage = new LANGUAGE_ProjectWizardFirstPage();
	
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
			throws CoreException {
		// TODO: LANG configureCreatedProject
		IFile mainModule = getProject().getFolder("src").getFile("main.lang");
		projectCreator.createFile(mainModule, "blah", true, monitor);
	}
	
}

class LANGUAGE_ProjectWizardFirstPage extends LangProjectWizardFirstPage {
	
	public LANGUAGE_ProjectWizardFirstPage() {
		setTitle(WizardMessages_Actual.LangNewProject_Page1_pageTitle);
		setDescription(WizardMessages_Actual.LangNewProject_Page1_pageDescription);
	}
	
	@Override
	protected void validatePreferences() throws ValidationException {
		new LANGUAGE_SDKLocationValidator().getValidatedField(SDKPreferences.SDK_PATH.get());
	}
	
}