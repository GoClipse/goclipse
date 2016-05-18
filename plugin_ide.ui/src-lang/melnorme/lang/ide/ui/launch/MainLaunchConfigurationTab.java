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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.launch.BuildTargetLaunchCreator;
import melnorme.lang.ide.core.launch.BuildTargetSource;
import melnorme.lang.ide.core.launch.CompositeBuildTargetSettings;
import melnorme.lang.ide.core.launch.ProjectLaunchSettings;
import melnorme.lang.ide.core.operations.ToolManager;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.operations.build.BuildTargetData;
import melnorme.lang.ide.core.operations.build.ProjectBuildInfo;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.build.BuildTargetEditor;
import melnorme.lang.tooling.commands.CommandInvocation;
import melnorme.utilbox.collections.Collection2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.status.StatusException;

/**
 * A main LaunchConfiguration with project selection field, 
 * and build target editor (including program path selection field).
 */
public abstract class MainLaunchConfigurationTab extends ProjectBasedLaunchConfigurationTab {
	
	protected final BuildManager buildManager = LangCore.getBuildManager();
	
	protected final BuildTargetSource buildTargetSource = init_buildTargetSource();
	protected final BuildTargetField buildTargetField = init_createBuildTargetField();
	protected final BuildTargetEditor buildTargetEditor = init_BuildTargetSettingsComponent();
	
	public MainLaunchConfigurationTab() {
		this(true);
	}
	
	public MainLaunchConfigurationTab(boolean initialize) {
		if(initialize) {
			initBindings();
		}
	}
	
	protected BuildTargetSource init_buildTargetSource() {
		return new BuildTargetSource() {
			@Override
			public String getProjectName() {
				return MainLaunchConfigurationTab.this.getProjectName();
			}
			
			@Override
			public String getBuildTargetName() {
				return MainLaunchConfigurationTab.this.getBuildTargetName();
			}
		};
	}
	
	protected BuildTargetField init_createBuildTargetField() {
		return new BuildTargetField();
	}
	
	protected BuildManager getBuildManager() {
		return buildManager;
	}
	
	protected ToolManager getToolManager() {
		return getBuildManager().getToolManager();
	}
	
	protected String getProgramPathString() {
		return buildTargetEditor.programPathField.getFieldValue();
	}
	
	protected String getBuildTargetName() {
		return buildTargetField.getFieldValue();
	}
	
	protected boolean isDefaultProgramPath() {
		return buildTargetEditor.programPathField.isUseDefault();
	}
	
	protected BuildTargetEditor init_BuildTargetSettingsComponent() {
		BuildTargetEditor component = new BuildTargetEditor(
			getBuildManager(),
			false,
			this::getDefaultBuildTargetBuildCommand,
			this::getDefaultProgramPath
		);
		component.programPathField.setLabelText(LangUIMessages.LaunchTab_ProgramPathField_title);
		
		component.buildCommandField.getEnablementField().setLabelText(
			LangUIMessages.LaunchTab_Fields_useBuildTargetSettings);
		component.programPathField.getUseDefaultField().setLabelText(
			LangUIMessages.LaunchTab_Fields_useBuildTargetSettings);
		return component;
	}
	
	/* -----------------  ----------------- */
	
	protected CommandInvocation getDefaultBuildTargetBuildCommand() throws CommonException {
		return buildTargetSource.getBuildTarget().getEffectiveBuildCommand2();
	}
	
	protected String getDefaultProgramPath() throws CommonException {
		return buildTargetSource.getBuildTarget().getEffectiveValidExecutablePath();
	}
	
	protected CompositeBuildTargetSettings getBuildTargetSettings() throws CommonException {
		return new CompositeBuildTargetSettings(buildTargetSource) {
			@Override
			public CommandInvocation getBuildCommand() {
				return buildTargetEditor.getEffectiveBuildCommand();
			}
			
			@Override
			public String getExecutablePath() {
				return buildTargetEditor.getEffectiveProgramPathValue();
			}
		};
	}
	
	@Override
	protected void doValidate() throws StatusException {
		super.doValidate();
		
		buildTargetField.validate();
		buildTargetEditor.validate();
	}
	
	/* ----------------- bindings ----------------- */
	
	protected void initBindings() {
		projectField.addChangeListener(this::projectFieldChanged);
		buildTargetField.addChangeListener(this::buildTargetFieldChanged);
		
		buildTargetEditor.buildCommandField.field().addChangeListener(this::updateLaunchConfigurationDialog);
		buildTargetEditor.programPathField.addChangeListener(this::updateLaunchConfigurationDialog);
	}
	
	public void projectFieldChanged() {
		IProject project = getValidProjectOrNull();
		if(project != null) {
			ProjectBuildInfo buildInfo = getBuildManager().getBuildInfo(project);
			if(buildInfo != null) {
				
				Collection2<BuildTarget> buildTargets = buildInfo.getBuildTargets();
				buildTargetField.setFieldOptions(
					buildTargets.map((buildTarget) -> buildTarget.getTargetName()));
			}
		}
		
		updateLaunchConfigurationDialog();
	}
	
	public void buildTargetFieldChanged() {
		IProject project = getValidProjectOrNull();
		if(project != null) {
			buildTargetEditor.setInput(getBuildTargetData());
			updateLaunchConfigurationDialog();
		}
	}
	
	protected BuildTargetData getBuildTargetData() {
		return new BuildTargetData(getBuildTargetName(), true, true, 
			buildTargetEditor.buildCommandField.getEffectiveFieldValue(), 
			buildTargetEditor.programPathField.getEffectiveFieldValue());
	}
	
	/* ----------------- Control creation ----------------- */
	
	@Override
	protected void createCustomControls(Composite parent) {
		buildTargetField.createComponent(parent, new GridData(GridData.FILL_HORIZONTAL));
		buildTargetEditor.createComponent(parent, new GridData(GridData.FILL_BOTH));
	}
	
	/* ----------------- Apply/Revert/Defaults ----------------- */
	
	@Override
	protected ProjectLaunchSettings getDefaultProjectLaunchSettings() {
		return new BuildTargetLaunchCreator();
	}
	
	@Override
	protected BuildTargetLaunchCreator doInitializeFrom_createSettings(ILaunchConfiguration config) 
			throws CommonException {
		try {
			return new BuildTargetLaunchCreator(config);
		} catch(CoreException e) {
			throw LangCore.createCommonException(e);
		}
	}
	
	@Override
	public void doInitializeFrom(ILaunchConfiguration config) throws CommonException {
		BuildTargetLaunchCreator buildSettings = doInitializeFrom_createSettings(config);
		
		super.initializeFrom(buildSettings);
		initializeBuildTargetField(buildSettings);
		initializeBuildTargetSettings(buildSettings);
	}
	
	protected void initializeBuildTargetField(BuildTargetLaunchCreator btLaunchCreator) {
		buildTargetField.setFieldValue(btLaunchCreator.getTargetName());
	}
	
	protected void initializeBuildTargetSettings(BuildTargetLaunchCreator btLaunchCreator) {
		buildTargetEditor.buildCommandField.setEffectiveFieldValue(btLaunchCreator.getBuildCommand());
		buildTargetEditor.programPathField.setEffectiveFieldValue(btLaunchCreator.getExecutablePath());
	}
	
	@Override
	protected ProjectLaunchSettings getLaunchSettingsFromTab() {
		BuildTargetData btData = getBuildTargetData();
		return new BuildTargetLaunchCreator(
			getProjectName(),
			btData
		);
	}
	
}