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

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.launch.BuildTargetSettingsValidator;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.operations.build.BuildTarget.BuildTargetData;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.core.utils.ProjectValidator;
import melnorme.lang.ide.ui.launch.BuildTargetField;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;
import melnorme.lang.tooling.data.StatusLevel;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.AbstractComponent;
import melnorme.utilbox.collections.Collection2;
import melnorme.utilbox.collections.HashMap2;
import melnorme.utilbox.core.CommonException;

public abstract class LangProjectBuildConfigurationComponent extends AbstractComponent {
	
	protected final IProject project;
	protected final BuildTargetField buildTargetField = init_createBuildTargetField();
	protected final BuildTargetSettingsComponent buildTargetSettings = init_createBuildTargetSettingsComponent();
	
	protected final HashMap2<String, BuildTargetData> buildOptionsToChange2 = new HashMap2<>();
	protected BuildTargetData buildTargetData = new BuildTargetData();
	
	public LangProjectBuildConfigurationComponent(IProject project) {
		this.project = project;
		initialize();
	}
	
	protected BuildTargetField init_createBuildTargetField() {
		return new BuildTargetField();
	}
	protected BuildTargetSettingsComponent init_createBuildTargetSettingsComponent() {
		return new BuildTargetSettingsComponent(
			getValidator()::getDefaultBuildArguments, getValidator()::getDefaultArtifactPath);
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
		
		buildOptionsToChange2.clear();

		Collection2<BuildTarget> buildTargets = buildInfo.getBuildTargets();
		for(BuildTarget buildTarget : buildTargets) {
			buildOptionsToChange2.put(buildTarget.getTargetName(), buildTarget.getDataCopy());
		}
		
		initBindings();
		
		buildTargetField.setFieldOptions(
			buildInfo.getBuildTargets().map((buildTarget) -> buildTarget.getTargetName()));
	}
	
	protected void initBindings() {
		buildTargetField.addValueChangedListener(() -> handleBuildTargetChanged());
		
		buildTargetSettings.buildArgumentsField.addValueChangedListener(() -> 
			buildTargetData.buildArguments = buildTargetSettings.getEffectiveArgumentsValue());
		buildTargetSettings.programPathField.addValueChangedListener(() -> 
			buildTargetData.artifactPath = buildTargetSettings.getEffectiveProgramPathValue());
	}
	
	protected BuildTargetSettingsValidator getValidator() {
		return new BuildTargetSettingsValidator() {
			
			@Override
			public String getProjectName() throws CommonException {
				return project == null ? null : project.getName();
			};
			
			@Override
			public String getBuildTargetName() {
				return LangProjectBuildConfigurationComponent.this.getBuildTargetName();
			}
			
			@Override
			public String getBuildArguments() {
				return buildTargetSettings.getEffectiveArgumentsValue();
			}
			
			@Override
			public String getArtifactPath() {
				return buildTargetSettings.getEffectiveProgramPathValue();
			}
		};
	}
	
//	protected void doValidate() throws CommonException, CoreException {
//		getValidator().getValidExecutableLocation();
//	}
	
	protected void handleBuildTargetChanged() {
		String buildTargetName = getBuildTargetName();
		if(buildTargetName == null) {
			return;
		}
		buildTargetData = buildOptionsToChange2.get(buildTargetName);
		buildTargetSettings.inputChanged(buildTargetData);
	}
	
	protected void handleStatusException(CommonException ce) {
		UIOperationExceptionHandler.handleStatusMessage("Error", ce.toStatusException(StatusLevel.ERROR));
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
		
		buildTargetSettings.createComponent(topControl, 
			gdFillDefaults().grab(true, false).hint(200, SWT.DEFAULT).create());
		
		SWTFactoryUtil.createPushButton2(topControl, 
			"Restore all targets to defaults", null,
			new GridData(GridData.HORIZONTAL_ALIGN_END),
			(e) -> restoreDefaults()
		);
	}
	
	protected void setEnabled(boolean enabled) {
		buildTargetField.setEnabled(enabled);
		buildTargetSettings.setEnabled(enabled);
	}
	
	/* ----------------- ----------------- */
	
	@Override
	public void updateComponentFromInput() {
		setEnabled(getBuildTargetName() != null);
	}
	
	/* ----------------- apply/restore ----------------- */
	
	public boolean performOk() {
		if(project == null) {
			return false;
		}
		
		for(Entry<String, BuildTargetData> entry : buildOptionsToChange2.entrySet()) {
			
			try {
				BuildTarget buildTarget = 
						getBuildManager().getValidDefinedBuildTarget(getValidProject(), entry.getKey());
				
				getBuildInfo().changeBuildTarget(buildTarget, entry.getValue());
			} catch(CommonException e) {
				handleStatusException(e);
				return false;
			}
		}
		updateComponentFromInput();
		
		return true;
	}
	
	public void restoreDefaults() {
		if(project == null) {
			return;
		}
		
		for(BuildTargetData data : buildOptionsToChange2.values()) {
			data.buildArguments = null;
			data.artifactPath = null;
		}
		handleBuildTargetChanged();
	}
	
}