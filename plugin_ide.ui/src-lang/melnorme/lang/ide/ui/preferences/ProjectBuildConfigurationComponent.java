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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.launch.BuildTargetSource;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.operations.build.BuildTargetData;
import melnorme.lang.ide.core.operations.build.ProjectBuildInfo;
import melnorme.lang.ide.core.utils.ProjectValidator;
import melnorme.lang.ide.ui.launch.BuildTargetField;
import melnorme.lang.ide.ui.preferences.common.IPreferencesEditor;
import melnorme.lang.ide.ui.utils.UIOperationsStatusHandler;
import melnorme.lang.tooling.data.Severity;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.AbstractCompositeWidget;
import melnorme.util.swt.components.IDisableableWidget;
import melnorme.utilbox.collections.Collection2;
import melnorme.utilbox.collections.HashMap2;
import melnorme.utilbox.core.CommonException;

public class ProjectBuildConfigurationComponent extends AbstractCompositeWidget 
	implements IPreferencesEditor {
	
	protected final BuildManager buildManager = LangCore.getBuildManager();
	
	protected final IProject project;
	protected final BuildTargetField buildTargetField;
	protected final BuildTargetEditor buildTargetEditor;
	
	protected final HashMap2<String, BuildTargetData> buildOptionsToChange = new HashMap2<>();
	
	public ProjectBuildConfigurationComponent(IProject project) {
		super(false);
		
		this.project = project;
		
		this.buildTargetField = init_createBuildTargetField();
		this.buildTargetEditor = init_createBuildTargetSettingsComponent();
		addSubComponent(buildTargetField);
		addSubComponent(buildTargetEditor);
		
		buildTargetField.addListener(true, (__) -> handleBuildTargetChanged());
		
		initialize();
	}
	
	protected BuildTargetField init_createBuildTargetField() {
		return new BuildTargetField();
	}
	protected BuildTargetEditor init_createBuildTargetSettingsComponent() {
		return new BuildTargetEditor(
			getBuildManager(),
			true,
			this::getDefaultBuildCommand, 
			this::getDefaultExecutablePath
		);
	}

	public BuildTargetField getBuildTargetField() {
		return buildTargetField;
	}
	
	public BuildTargetEditor getBuildTargetEditor() {
		return buildTargetEditor;
	}
	
	/* -----------------  ----------------- */
	
	protected BuildManager getBuildManager() {
		return buildManager;
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
		
		buildTargetField.setFieldOptions(
			buildInfo.getBuildTargets().map((buildTarget) -> buildTarget.getTargetName()));
	}
	
	protected void handleBuildTargetChanged() {
		String buildTargetName = getBuildTargetName();
		buildTargetEditor.setEnabled(buildTargetName != null);
		
		if(buildTargetName == null) {
			return;
		}
		
		buildTargetEditor.btData = buildOptionsToChange.get(buildTargetName);
		buildTargetEditor.inputChanged(buildTargetEditor.btData);
	}
	
	/* -----------------  ----------------- */
		
	protected final BuildTargetSource buildTargetSource = new BuildTargetSource() {
		@Override
		public String getProjectName(){
			return project == null ? null : project.getName();
		};
		
		@Override
		public String getBuildTargetName() {
			return ProjectBuildConfigurationComponent.this.getBuildTargetName();
		}
	};
	
	public BuildTarget getOriginalBuildTarget() throws CommonException {
		return buildTargetSource.getBuildTarget();
	}
	
	public String getDefaultBuildCommand() throws CommonException {
		return getOriginalBuildTarget().getDefaultBuildCommand();
	}
	
	public String getDefaultExecutablePath() throws CommonException {
		return getOriginalBuildTarget().getDefaultExecutablePath();
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
		super.createContents(topControl);
		
		SWTFactoryUtil.createPushButton2(topControl, 
			"Restore all targets to defaults", null,
			new GridData(GridData.HORIZONTAL_ALIGN_END),
			(e) -> loadDefaults()
		);
	}
	
	@Override
	protected GridData getLayoutData(IDisableableWidget subComponent) {
		return super.getLayoutData(subComponent);
	}
	
	/* ----------------- apply/restore ----------------- */
	
	@Override
	public boolean saveSettings() {
		if(project == null) {
			return false;
		}
		
		try {
			doSaveSettings();
		} catch(CommonException e) {
			return false;
		}
		updateWidgetFromInput();
		
		return true;
	}
	
	@Override
	public void doSaveSettings() throws CommonException {
		for(Entry<String, BuildTargetData> entry : buildOptionsToChange.entrySet()) {
			String targetName = entry.getKey();
			BuildTargetData value = entry.getValue();
			getBuildInfo().changeBuildTarget(targetName, value);
		}
	}
	
	@Override
	public void loadDefaults() {
		if(project == null) {
			return;
		}
		
		ProjectBuildInfo buildInfo;
		try {
			buildInfo = getBuildInfo();
		} catch(CommonException e) {
			UIOperationsStatusHandler.handleStatus(true, "Error loading defaults", e);
			return;
		}
		
		for (Entry<String, BuildTargetData> entry : buildOptionsToChange) {
			String targetName = entry.getKey();
			BuildTargetData newData = buildInfo.getDefaultBuildTarget(targetName).getDataCopy(); 
			entry.setValue(newData);
		}
		handleBuildTargetChanged();
	}
	
}