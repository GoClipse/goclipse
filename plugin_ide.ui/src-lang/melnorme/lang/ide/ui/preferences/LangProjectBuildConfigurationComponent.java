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
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.core.utils.ProjectValidator;
import melnorme.lang.ide.ui.fields.ArgumentsGroupField;
import melnorme.lang.ide.ui.launch.BuildTargetField;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;
import melnorme.lang.tooling.data.StatusLevel;
import melnorme.util.swt.components.AbstractComponent;
import melnorme.utilbox.collections.Collection2;
import melnorme.utilbox.collections.HashMap2;
import melnorme.utilbox.core.CommonException;

public abstract class LangProjectBuildConfigurationComponent extends AbstractComponent {
	
	protected final IProject project;
	protected final BuildTargetField buildTargetField = init_createBuildTargetField();
	protected final ArgumentsGroupField buildOptionsField = init_createArgumentsField();
	
	protected final HashMap2<BuildTarget, String> buildOptionsToChange = new HashMap2<>();
	protected BuildTarget currentBuildTarget;
	
	public LangProjectBuildConfigurationComponent(IProject project) {
		this.project = project;
		initBindings();
	}
	
	protected BuildTargetField init_createBuildTargetField() {
		return new BuildTargetField();
	}
	protected ArgumentsGroupField init_createArgumentsField() {
		return new ArgumentsGroupField(init_getArgumentsField_Label());
	}
	
	protected abstract String init_getArgumentsField_Label();
	
	/* -----------------  ----------------- */
	
	protected BuildManager getBuildManager() {
		return LangCore.getBuildManager();
	}
	
	protected String getBuildTargetName() {
		return buildTargetField.getFieldValue();
	}
	
	protected void initBindings() {
		buildTargetField.addValueChangedListener(() -> handleBuildTargetChanged());
		buildOptionsField.addValueChangedListener(() -> handleBuildOptionsChanged());
	}
	
	protected IProject getValidProject() throws CommonException {
		if(project == null) {
			throw new CommonException(new ProjectValidator().msg_ProjectNotSpecified());
		}
		return project;
	}
	
	protected ProjectBuildInfo getBuildInfo() throws CommonException {
		return getBuildManager().getValidBuildInfo(project);
	}
	
	protected BuildTarget getSelectedBuildTarget() throws CommonException {
		return getBuildManager().getValidDefinedBuildTarget(getValidProject(), getBuildTargetName());
	}
	
	protected void handleBuildTargetChanged() {
		try {
			currentBuildTarget = getSelectedBuildTarget();
			
			String buildOptions = buildOptionsToChange.get(currentBuildTarget); 
			if(buildOptions == null) {
				buildOptions = currentBuildTarget.getBuildOptions();
				buildOptionsToChange.put(currentBuildTarget, buildOptions);
			}
			
			buildOptionsField.setEnabled(true);
			buildOptionsField.setFieldValue(buildOptions);
		} catch(CommonException e) {
			buildOptionsField.setEnabled(false);
			handleStatusException(e);
		}
	}
	
	protected void handleBuildOptionsChanged() {
		if(currentBuildTarget != null) {
			buildOptionsToChange.put(currentBuildTarget, buildOptionsField.getFieldValue());
		}
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
		buildOptionsField.createComponent(topControl,
			gdFillDefaults().grab(true, false).hint(200, SWT.DEFAULT).create());
	}
	
	/* ----------------- ----------------- */
	
	@Override
	public void updateComponentFromInput() {
		if(project == null) {
			return;
		}
		
		buildOptionsToChange.clear();
		
		try {
			ProjectBuildInfo buildInfo = getBuildInfo();
			buildTargetField.setFieldOptions(
				buildInfo.getBuildTargets().map((buildTarget) -> buildTarget.getTargetName()));
			setEnabled(true);
		} catch(CommonException ce) {
			handleStatusException(ce);
			setEnabled(false);
		}
	}
	
	protected void setEnabled(boolean enabled) {
		buildTargetField.setEnabled(enabled);
		buildOptionsField.setEnabled(enabled);
	}
	
	
	/* ----------------- apply/restore ----------------- */
	
	public boolean performOk() {
		if(project == null) {
			return false;
		}
		
		for(Entry<BuildTarget, String> entry : buildOptionsToChange.entrySet()) {
			BuildTarget buildTarget = entry.getKey();
			
			try {
				getBuildInfo().changeOptions(buildTarget, entry.getValue());
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
		
		buildOptionsToChange.clear();
		try {
			Collection2<BuildTarget> buildTargets = getBuildInfo().getBuildTargets();
			for(BuildTarget buildTarget : buildTargets) {
				buildOptionsToChange.put(buildTarget, null);
			}
		} catch(CommonException e) {
			return;
		}
		handleBuildTargetChanged();
	}
	
}