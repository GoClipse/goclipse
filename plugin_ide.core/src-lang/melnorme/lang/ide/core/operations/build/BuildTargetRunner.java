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

import java.nio.file.Path;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.launch.LaunchMessages;
import melnorme.lang.ide.core.operations.OperationInfo;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.PathUtil;

public abstract class BuildTargetRunner {
	
	protected final IProject project;
	protected final BuildConfiguration buildConfiguration2;
	protected final String buildTypeName;
	protected final String buildOptions;
	
	public BuildTargetRunner(IProject project, BuildConfiguration buildConfig, String buildTypeName,
			String buildOptions) {
		this.project = project;
		this.buildConfiguration2 = assertNotNull(buildConfig);
		this.buildTypeName = buildTypeName;
		this.buildOptions = buildOptions;
	}
	
	public BuildManager getBuildManager() {
		return LangCore.getBuildManager();
	}
	
	public IProject getProject() {
		return project;
	}
	
	public BuildConfiguration getBuildConfiguration() {
		return buildConfiguration2;
	}
	
	public String getBuildConfigName() {
		return buildConfiguration2.getName();
	}
	
	public String getBuildOptions() {
		return buildOptions;
	}
	
	public String getBuildTypeName() {
		return buildTypeName;
	}
	
	public String getEffectiveBuildOptions() throws CommonException, CoreException {
		String buildOptions = getBuildOptions();
		if(buildOptions != null) {
			return buildOptions;
		}
		return getDefaultBuildOptions();
	}
	
	/* ----------------- Build Configuration ----------------- */
	
	public static class BuildConfiguration {
		
		protected final String name;
		protected final String artifactPath;
		
		public BuildConfiguration(String name, String artifactPath) {
			this.name = assertNotNull(name);
			this.artifactPath = artifactPath;
		}
		
		public String getName() {
			return name;
		}
		
		public String getArtifactPath() {
			return artifactPath;
		}
		
	}
	
	/* -----------------  ----------------- */
	
	public static abstract class BuildType {
		
		protected final String name;
		
		public BuildType(String name) {
			this.name = assertNotNull(name);
		}
		
		/* -----------------  ----------------- */
		
		public String getName() {
			return name;
		}
		
		public abstract String getDefaultBuildOptions(BuildTargetRunner buildTargetRunner)
				throws CommonException, CoreException;
		
		public String getArtifactPath(BuildTargetRunner buildTargetOp) 
				throws CommonException, CoreException {
			return buildTargetOp.getBuildConfiguration().getArtifactPath();
		}
		
	}
	
	/* -----------------  ----------------- */
	
	protected BuildType getBuildType() throws CommonException {
		return getBuildManager().getBuildType_NonNull(getBuildTypeName());
	}
	
	public String getDefaultBuildOptions() throws CommonException, CoreException {
		return getBuildType().getDefaultBuildOptions(this);
	}
	
	public String getArtifactPath() throws CommonException, CoreException {
		return getBuildType().getArtifactPath(this);
	}
	
	public Path getValidArtifactPath() throws CommonException, CoreException {
		return getValidArtifactPath(getArtifactPath());
	}
	
	public Path getValidArtifactPath(String artifactPathStr) throws CommonException {
		if(artifactPathStr == null || artifactPathStr.isEmpty()) {
			throw new CommonException(LaunchMessages.BuildTarget_NoArtifactPathSpecified);
		}
		return PathUtil.createPath(artifactPathStr);
	}
	
	public abstract CommonBuildTargetOperation getBuildOperation(OperationInfo parentOpInfo, Path buildToolPath, 
			boolean fullBuild);
	
}