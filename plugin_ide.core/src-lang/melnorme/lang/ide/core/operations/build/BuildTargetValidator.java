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
package melnorme.lang.ide.core.operations.build;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.build.BuildManager.BuildConfiguration;
import melnorme.lang.ide.core.operations.build.BuildManager.BuildType;
import melnorme.lang.tooling.data.AbstractValidator2;
import melnorme.utilbox.core.CommonException;

public class BuildTargetValidator extends AbstractValidator2 {
	
	public final BuildManager buildMgr = LangCore.getBuildManager();
	
	protected final IProject project;
	protected final String buildConfigName;
	protected final String buildTypeName;
	protected final String buildArguments;
	
	public BuildTargetValidator(IProject project, String buildConfigName, String buildTypeName,
			String buildArguments) {
		this.project = project;
		this.buildConfigName = assertNotNull(buildConfigName);
		this.buildTypeName = assertNotNull(buildTypeName);
		this.buildArguments = buildArguments;
	}
	
	public BuildManager getBuildManager() {
		return buildMgr;
	}
	
	public IProject getProject() {
		return project;
	}
	
	public String getBuildConfigName() {
		return buildConfigName;
	}
	
	public String getBuildTypeName() {
		return buildTypeName;
	}
	
	public String getBuildArguments() {
		return buildArguments;
	}
	
	public BuildConfiguration getBuildConfiguration() throws CommonException {
		return buildMgr.getValidBuildConfiguration(project, buildConfigName);
	}
	
	public BuildType getBuildType() throws CommonException {
		return buildMgr.getBuildType_NonNull(buildTypeName);
	}
	
	public String getEffectiveBuildArguments() throws CommonException, CoreException {
		String buildOptions = getBuildArguments();
		if(buildOptions != null) {
			return buildOptions;
		}
		return getDefaultBuildArguments();
	}
	
	public String getDefaultBuildArguments() throws CommonException {
		return getBuildType().getDefaultBuildOptions(this);
	}
	
	/* -----------------  ----------------- */
	
	public String getArtifactPath() throws CommonException {
		return getBuildType().getArtifactPath(this);
	}
	
}