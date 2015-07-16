/*******************************************************************************
 * Copyright (c) 2013, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * 		DLTK team -
 * 		Bruno Medeiros - rewrote class to remove DLTK dependencies and cleanup/simplify code. 
 *******************************************************************************/
package melnorme.lang.ide.ui.launch;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.launching.LaunchConstants;
import melnorme.lang.ide.ui.LangImages;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.fields.ProjectField;
import melnorme.lang.ide.ui.utils.WorkbenchUtils;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

public class CommonMainLaunchConfigurationTab extends AbstractLaunchConfigurationTabExt {
	
	protected static IWorkspaceRoot getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}
	
	protected String getNatureID() {
		return LangCore.NATURE_ID;
	}
	
	@Override
	public String getName() {
		return LangUIMessages.mainLaunchTab_title;
	}
	
	@Override
	public Image getImage() {
		return LangImages.IMG_LAUNCHTAB_MAIN.getImage();
	}
	
	public CommonMainLaunchConfigurationTab() {
	}
	
	protected final ProjectField projectField = new ProjectField() {
		@Override
		protected IProject[] getDialogChooseElements() throws CoreException {
			return EclipseUtils.getOpenedProjects(getNatureID());
		};
		
		@Override
		protected void handleFieldValueAndControlChanged() {
			updateLaunchConfigurationDialog();
		}
	}; 
	
	@Override
	public void createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);
		
		comp.setLayout(GridLayoutFactory.swtDefaults().spacing(5, 0).create());
		
		projectField.createComponent(comp, new GridData(GridData.FILL_HORIZONTAL));
		createVerticalSpacer(comp, 1);
		
		createCustomControls(comp);
		createVerticalSpacer(comp, 1);
		
		// PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(),
		// IScriptDebugHelpContextIds.LAUNCH_CONFIGURATION_DIALOG_MAIN_TAB);
	}
	
	@SuppressWarnings("unused")
	protected void createCustomControls(Composite composite) {
	}
	
	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy config) {
		IResource contextualResource = WorkbenchUtils.getContextResource();
		setDefaults(contextualResource, config);
	}
	
	protected void setDefaults(IResource contextualResource, ILaunchConfigurationWorkingCopy config) {
		if(contextualResource == null) {
			return;
		}
		IProject project = contextualResource.getProject();
		if (project != null && isValidProject(project)) {
			config.setAttribute(LaunchConstants.ATTR_PROJECT_NAME, project.getName());
		}
	}
	
	@Override
	public void initializeFrom(ILaunchConfiguration config) {
		String projectName = getConfigAttribute(config, LaunchConstants.ATTR_PROJECT_NAME, (String) "");
		projectField.setFieldValue(projectName);
	}
	
	protected final String getProjectName() {
		return projectField.getFieldValue();
	}

	protected IProject getProject() {
		String projectName = getProjectName();
		if(!Path.ROOT.isValidSegment(projectName)) 
			return null;
		return getWorkspaceRoot().getProject(projectName);
	}
	
	/* ---------- save/apply ---------- */
	
	@Override
	public final void performApply(ILaunchConfigurationWorkingCopy config) {
		doPerformApply(config);
		try {
			mapResources(config);
		} catch (CoreException ce) {
			LangUIPlugin.logStatus(ce);
		}
	}
	
	protected void doPerformApply(ILaunchConfigurationWorkingCopy config) {
		String projectName = getProjectName();
		config.setAttribute(LaunchConstants.ATTR_PROJECT_NAME, projectName);
	}
	
	protected void mapResources(ILaunchConfigurationWorkingCopy config) throws CoreException {
		IResource resource = getProject();
		if (resource == null) {
			config.setMappedResources(null);
		} else {
			config.setMappedResources(new IResource[] { resource });
		}
	}
	
	protected IResource getMappedResource() throws CoreException {
		IProject project = getProject();
		if (project != null && project.exists()) {
			return project;
		}
		return null;
	}
	
	/* ---------- validation ---------- */
	
	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		setErrorMessage(null);
		setMessage(null);
		doValidate();
		return getErrorMessage() == null;
	}
	
	protected void doValidate() {
		validateProject();
	}
	
	protected void validateProject() {
		String projectName = getProjectName();
		if (projectName.length() == 0) {
			setErrorMessage(LangUIMessages.error_selectProject);
			return;
		}
		
		if (!isValidProject(getProject())) {
			setErrorMessage(LangUIMessages.error_notAValidProject);
			return;
		}
		
		return;
	}
	
	protected boolean isValidProject(IProject project) {
		if (project != null && project.exists()) {
			try {
				return project.hasNature(getNatureID());
			} catch (CoreException ce) {
				LangUIPlugin.logStatus(ce);
			}
		}
		return false;
	}
	
}