/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.preferences;

import java.util.Map.Entry;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.osgi.service.prefs.BackingStoreException;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.launch.BuildTargetSource;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.operations.build.BuildTarget.BuildTargetData;
import melnorme.lang.ide.core.operations.build.ValidatedBuildTarget;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.core.utils.ProjectValidator;
import melnorme.lang.ide.ui.launch.BuildTargetField;
import melnorme.lang.ide.ui.preferences.common.IPreferencesEditor;
import melnorme.lang.ide.ui.utils.UIOperationsStatusHandler;
import melnorme.lang.tooling.data.Severity;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.AbstractDisableableWidget;
import melnorme.utilbox.collections.Collection2;
import melnorme.utilbox.collections.HashMap2;
import melnorme.utilbox.core.CommonException;

public class ProjectBuildConfigurationComponent extends AbstractDisableableWidget 
	implements IPreferencesEditor {
	
	protected final IProject project;
	protected final BuildTargetField buildTargetField = init_createBuildTargetField();
	protected final BuildTargetSettingsComponent buildTargetSettingsComponent 
		= init_createBuildTargetSettingsComponent();
	
	protected final HashMap2<String, BuildTargetData> buildOptionsToChange = new HashMap2<>();
	
	public ProjectBuildConfigurationComponent(IProject project) {
		this.project = project;
		initialize();
	}
	
	protected BuildTargetField init_createBuildTargetField() {
		return new BuildTargetField();
	}
	protected BuildTargetSettingsComponent init_createBuildTargetSettingsComponent() {
		return new BuildTargetSettingsComponent(
			this::getDefaultBuildArguments, 
			this::getDefaultCheckArguments,
			this::getDefaultExecutablePath
		);
	}

	public BuildTargetField getBuildTargetField() {
		return buildTargetField;
	}
	
	public BuildTargetSettingsComponent getBuildTargetSettings() {
		return buildTargetSettingsComponent;
	}
	
	/* -----------------  ----------------- */
	
	protected BuildManager getBuildManager() {
		return LangCore.getBuildManager();
	}
	
	public IProject getValidProject() throws CommonException {
		if(project == null) {
			throw new CommonException(new ProjectValidator().msg_ProjectNotSpecified());
		}
		return project;
	}
	
	public String getBuildTargetName() {
		return buildTargetField.getFieldValue();
	}
	
	protected ProjectBuildInfo getBuildInfo() throws CommonException {
		return getBuildManager().getValidBuildInfo(getValidProject());
	}
	
	/* -----------------  ----------------- */
	
	protected void initialize() {
		ProjectBuildInfo buildInfo;
		try {
			buildInfo = getBuildInfo();
		} catch(CommonException ce) {
			handleStatusException(ce);
			return;
		}
		
		buildOptionsToChange.clear();

		Collection2<BuildTarget> buildTargets = buildInfo.getBuildTargets();
		for(BuildTarget buildTarget : buildTargets) {
			buildOptionsToChange.put(buildTarget.getTargetName(), buildTarget.getDataCopy());
		}
		
		buildTargetField.addListener(() -> handleBuildTargetChanged());
		
		buildTargetField.setFieldOptions(
			buildInfo.getBuildTargets().map((buildTarget) -> buildTarget.getTargetName()));
	}
	
	protected void handleBuildTargetChanged() {
		String buildTargetName = getBuildTargetName();
		if(buildTargetName == null) {
			return;
		}
		
		buildTargetSettingsComponent.buildTargetData = buildOptionsToChange.get(buildTargetName);
		buildTargetSettingsComponent.inputChanged(buildTargetSettingsComponent.buildTargetData);
	}
	
	/* -----------------  ----------------- */
		
	public ValidatedBuildTarget getOriginalBuildTarget() throws CommonException {
		return getValidator().getValidatedOriginalBuildTarget();
	}
	
	public String getDefaultBuildArguments() throws CommonException {
		return getOriginalBuildTarget().getDefaultBuildArguments();
	}
	
	public String getDefaultCheckArguments() throws CommonException {
		return getOriginalBuildTarget().getDefaultCheckArguments();
	}
	
	public String getDefaultExecutablePath() throws CommonException {
		return getOriginalBuildTarget().getDefaultExecutablePath();
	}
	
	protected BuildTargetSource getValidator() {
		return new BuildTargetSource() {
			
			@Override
			public String getProjectName() throws CommonException {
				return project == null ? null : project.getName();
			};
			
			@Override
			public String getBuildTargetName() {
				return ProjectBuildConfigurationComponent.this.getBuildTargetName();
			}
			
		};
	}
	
	protected void handleStatusException(CommonException ce) {
		UIOperationsStatusHandler.handleStatus(true, "Error", ce.toStatusException(Severity.ERROR));
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public int getPreferredLayoutColumns() {
		return 1;
	}
	
	@Override
	protected void createContents(Composite topControl) {
		buildTargetField.createComponent(topControl,
			gdFillDefaults().grab(true, false).hint(150, SWT.DEFAULT).create());
		
		buildTargetSettingsComponent.createComponent(topControl, 
			gdFillDefaults().grab(true, false).hint(200, SWT.DEFAULT).create());
		
		SWTFactoryUtil.createPushButton2(topControl, 
			"Restore all targets to defaults", null,
			new GridData(GridData.HORIZONTAL_ALIGN_END),
			(e) -> loadDefaults()
		);
	}
	
	@Override
	protected void doSetEnabled(boolean enabled) {
		buildTargetField.setEnabled(enabled);
		buildTargetSettingsComponent.setEnabled(enabled && getBuildTargetName() != null);
	}
	
	/* ----------------- apply/restore ----------------- */
	
	@Override
	public boolean saveSettings() {
		if(project == null) {
			return false;
		}
		
		try {
			doSaveSettings();
		} catch(BackingStoreException e) {
			return false;
		}
		updateComponentFromInput();
		
		return true;
	}
	
	@Override
	public void doSaveSettings() throws BackingStoreException {
		try {
			for(Entry<String, BuildTargetData> entry : buildOptionsToChange.entrySet()) {
				
				BuildTarget buildTarget = 
						getBuildManager().getValidDefinedBuildTarget(getValidProject(), entry.getKey());
				
				getBuildInfo().changeBuildTarget(buildTarget, entry.getValue());
			}
		} catch(CommonException e) {
			throw new BackingStoreException("Error saving build target settings.", e);
		}
	}
	
	@Override
	public void loadDefaults() {
		if(project == null) {
			return;
		}
		
		for(BuildTargetData data : buildOptionsToChange.values()) {
			data.buildArguments = null;
			data.executablePath = null;
		}
		handleBuildTargetChanged();
	}
	
}