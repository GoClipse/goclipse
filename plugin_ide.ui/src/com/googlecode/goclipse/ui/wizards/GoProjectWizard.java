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
package com.googlecode.goclipse.ui.wizards;


import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.ui.WizardMessages_Actual;
import melnorme.lang.ide.ui.dialogs.LangNewProjectWizard;
import melnorme.lang.ide.ui.dialogs.LangProjectWizardFirstPage;
import melnorme.util.swt.SWTFactoryUtil;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.dialogs.PreferencesUtil;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.ui.GoPluginImages;
import com.googlecode.goclipse.ui.preferences.GoPreferencePage;

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
	protected ProjectCreator_ForWizard createProjectCreator() {
		return new GoProjectCreator();
	}
	
	public class GoProjectCreator extends ProjectCreator_ForWizard {
		
		public GoProjectCreator() {
			super(GoProjectWizard.this);
		}
		
		@Override
		protected void configureCreatedProject(IProgressMonitor monitor) throws CoreException {
			IProject project = getProject();
			
			if(!GoProjectEnvironment.isProjectInsideGoPath(project)) {
				ResourceUtils.createFolder(project.getFolder("src"), false, monitor);
				ResourceUtils.createFolder(project.getFolder("bin"), false, monitor);
				ResourceUtils.createFolder(project.getFolder("pkg"), false, monitor);
			}
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
	protected void createContents(Composite parent) {
		super.createContents(parent);
		
		Link link = SWTFactoryUtil.createLink(parent, SWT.NONE, 
			"<a>Configure Go preferences...</a>", 
			GridDataFactory.swtDefaults().span(1, 0).create());
		
		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openGoPreferencePage();
			}
		});
	}
	
	protected void openGoPreferencePage() {
		PreferenceDialog pref = PreferencesUtil.createPreferenceDialogOn(getShell(), GoPreferencePage.ID, null, null);
		
		if (pref != null) {
			pref.open();
		}
	}
	
}