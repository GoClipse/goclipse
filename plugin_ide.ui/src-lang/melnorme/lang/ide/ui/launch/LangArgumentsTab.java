/*******************************************************************************
 * Copyright (c) 2005, 2012 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     QNX Software Systems - Initial API and implementation
 *     IBM Corporation
 *     Bruno Medeiros - lang modifications
 *******************************************************************************/
package melnorme.lang.ide.ui.launch;

import melnorme.lang.ide.launching.LaunchConstants;
import melnorme.lang.ide.ui.LangImages;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.LangUIPlugin;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;


//BM: Original based on org.eclipse.cdt.launch.ui.CArgumentsTab
/**
 * A launch configuration tab that displays and edits program arguments,
 * and working directory launch configuration attributes.
 * <p>
 * This class may be instantiated. This class is not intended to be subclassed.
 * </p>
 */
public class LangArgumentsTab extends AbstractLaunchConfigurationTabExt {
	
	protected final LangArgumentsBlock2 argumentsBlock = new LangArgumentsBlock2(); 
	protected final LangWorkingDirectoryBlock workingDirectoryBlock = new LangWorkingDirectoryBlock();
	
	public LangArgumentsTab() {
		// TODO : add validation for fields above
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);
		GridLayout layout = new GridLayout(1, true);
		comp.setLayout(layout);
		comp.setFont(parent.getFont());
		
		GridData gd = new GridData(GridData.FILL_BOTH);
		comp.setLayoutData(gd);
		setHelpContextId();
		
		argumentsBlock.createComponent(comp, new GridData(GridData.FILL_BOTH));
		workingDirectoryBlock.createControl(comp);
	}
	
	protected void setHelpContextId() {
//		LangUIPlugin.getDefault().getWorkbench().getHelpSystem().setHelp(
//				getControl(), 
//				ICDTLaunchHelpContextIds.LAUNCH_CONFIGURATION_DIALOG_ARGUMNETS_TAB);
	}
	
	
	@Override
	public boolean isValid(ILaunchConfiguration config) {
		return workingDirectoryBlock.isValid(config);
	}
	
	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy config) {
		config.setAttribute(LaunchConstants.ATTR_PROGRAM_ARGUMENTS, (String) null);
		config.setAttribute(LaunchConstants.ATTR_WORKING_DIRECTORY, (String) null);
	}
	
	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			argumentsBlock.setFieldValue(configuration.getAttribute(LaunchConstants.ATTR_PROGRAM_ARGUMENTS, ""));
			workingDirectoryBlock.initializeFrom(configuration);
		}
		catch (CoreException ce) {
			setErrorMessage(LangUIMessages.getFormattedString(
					LangUIMessages.Launch_common_Exception_occurred_reading_configuration_EXCEPTION, 
					ce.getStatus().getMessage()));
			LangUIPlugin.logStatus(ce);
		}
	}
	
	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(LaunchConstants.ATTR_PROGRAM_ARGUMENTS, argumentsBlock.getFieldValue());
		workingDirectoryBlock.performApply(configuration);
	}
	
	@Override
	public String getId() {
		return null;
		//return TAB_ID;
	}
	
	@Override
	public String getName() {
		return LangUIMessages.LangArgumentsTab_Arguments;
	}
	
	@Override
	public void setLaunchConfigurationDialog(ILaunchConfigurationDialog dialog) {
		super.setLaunchConfigurationDialog(dialog);
		workingDirectoryBlock.setLaunchConfigurationDialog(dialog);
	}
	
	@Override
	public String getErrorMessage() {
		String m = super.getErrorMessage();
		if (m == null) {
			return workingDirectoryBlock.getErrorMessage();
		}
		return m;
	}
	
	@Override
	public String getMessage() {
		String m = super.getMessage();
		if (m == null) {
			return workingDirectoryBlock.getMessage();
		}
		return m;
	}
	
	@Override
	public Image getImage() {
		return LangImages.IMG_LAUNCHTAB_ARGUMENTS.getImage();
	}
	
	@Override
	protected void updateLaunchConfigurationDialog() {
		super.updateLaunchConfigurationDialog();
	}
	
}