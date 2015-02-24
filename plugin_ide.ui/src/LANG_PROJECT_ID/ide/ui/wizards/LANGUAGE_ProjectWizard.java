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


import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import melnorme.lang.ide.core.bundlemodel.SDKPreferences;
import melnorme.lang.ide.ui.WizardMessages_Actual;
import melnorme.lang.ide.ui.dialogs.LangNewProjectWizard;
import melnorme.lang.ide.ui.dialogs.LangProjectWizardFirstPage;
import melnorme.lang.tooling.data.AbstractValidator.ValidationException;

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
	protected ProjectCreator_ForWizard createProjectCreator() {
		return new LANGUAGE_ProjectCreator();
	}
	
	public class LANGUAGE_ProjectCreator extends ProjectCreator_ForWizard {
		
		public LANGUAGE_ProjectCreator() {
			super(LANGUAGE_ProjectWizard.this);
		}
		
		@Override
		protected void configureCreatedProject(IProgressMonitor monitor) throws CoreException {
			// TODO:
		}

		@Override
		protected String getHelloWorldContents() {
			throw assertFail();
		}

		@Override
		protected String getDefaultManifestFileContents() {
			throw assertFail();
		}
		
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