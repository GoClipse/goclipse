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

import java.util.ArrayList;
import java.util.Stack;

import melnorme.lang.ide.launching.LaunchConstants;
import melnorme.lang.ide.ui.launch.LangArgumentsBlock2;
import melnorme.lang.ide.ui.launch.LangWorkingDirectoryBlock;
import melnorme.lang.ide.ui.launch.MainLaunchConfigurationTab;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.WorkingDirectoryBlock;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.ui.GoUIPlugin;
import com.googlecode.goclipse.ui.dialogs.ResourceListSelectionDialog;

public class GoLaunchConfigurationTab extends MainLaunchConfigurationTab {
	
	public GoLaunchConfigurationTab() {
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
	protected void openProgramPathDialog(IProject project) {
		// TODO: this should be refactored to shows Go packages
		// and after even better: only main packages
		try {
			String[] pathRoots = Environment.getSourceFoldersAsStringArray(project);
			
			ArrayList<IResource> resources = new ArrayList<IResource>();
			
			// load stack
			Stack<IFolder> stack = new Stack<IFolder>();
			for (String path : pathRoots) {
				IResource res = project.findMember(path);
				if (res.getType() == IResource.FOLDER) {
					stack.push((IFolder) res);
				}
			}
			
			// walk resource tree
			while (!stack.isEmpty()) {
				IFolder folder = stack.pop();
				for (IResource resource : folder.members()) {
					if (resource.getType() == IResource.FILE && resource.getName().endsWith(".go")) {
						resources.add(resource);
					} else if (resource.getType() == IResource.FOLDER) {
						stack.push((IFolder) resource);
					}
				}
			}
			
			ResourceListSelectionDialog dialog = new ResourceListSelectionDialog(getShell(),
					resources.toArray(new IResource[resources.size()]));
			
			dialog.setStartingPattern("?");
			dialog.setTitle("Go Main File Selection:");
			
			dialog.open();
			Object[] objs = dialog.getResult();
			if (objs != null && objs.length > 0) {
				String programPath = ((IResource) objs[0]).getProjectRelativePath().toString();
				programPathField.setFieldValue(programPath);
			}
		} catch (CoreException e) {
			GoUIPlugin.logStatus(e);
		}
	}
	
}