/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.launch;


import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.operations.build.ValidatedBuildTarget;
import melnorme.lang.ide.core.utils.ProjectValidator;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.data.AbstractValidator;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public abstract class BuildTargetSource extends AbstractValidator {
	
	public BuildTargetSource() {
		super();
	}
	
	public ProjectValidator getProjectValidator() {
		return new ProjectValidator(LangCore.NATURE_ID);
	}
	
	public IProject getValidProject() throws CommonException {
		return getProjectValidator().getProject(getProjectName());
	}
	
	protected Location getProjectLocation() throws CommonException {
		return ResourceUtils.loc(getValidProject().getLocation());
	}
	
	protected BuildManager getBuildManager() {
		return LangCore.getBuildManager();
	}
	
	// can be null
	public abstract String getProjectName() throws CommonException;
	
	// can be null
	public abstract String getBuildTargetName();
	
	protected BuildTarget getOriginalBuildTarget() throws CommonException {
		return getBuildManager().getValidBuildTarget(
			getValidProject(), getBuildTargetName(), false, true);
	}
	
	public ValidatedBuildTarget getValidatedOriginalBuildTarget() throws CommonException {
		return getBuildManager().getValidatedBuildTarget(getValidProject(), getOriginalBuildTarget());
	}
	
}