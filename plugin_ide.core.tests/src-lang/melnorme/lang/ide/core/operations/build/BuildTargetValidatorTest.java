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

import org.eclipse.core.runtime.CoreException;
import org.junit.Test;

import melnorme.lang.ide.core.launch.BuildTargetSettingsValidator;
import melnorme.lang.ide.core.operations.build.BuildManager.BuildConfiguration;
import melnorme.lang.ide.core.project_model.AbstractBundleInfo;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.core.tests.SampleProject;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.tests.CommonTest;

public class BuildTargetValidatorTest extends CommonTest {
	
	protected class SampleBuildProject extends SampleProject {
		
		public SampleBuildProject(String name) throws CoreException {
			super(name);
			
			setSampleProjectInfo();
		}
		
		protected void setSampleProjectInfo() throws org.eclipse.core.runtime.CoreException {
			
			ArrayList2<BuildTarget> buildTargets = new ArrayList2<>();
			buildTargets.addElements(
				new BuildTarget("SampleTarget", true, null, null),
				new BuildTarget("SampleTarget2", true, "sample args", "sample path")
			);
			
			buildMgr.setProjectBuildInfo(project, 
				new ProjectBuildInfo(buildMgr, project, new AbstractBundleInfo() {
					@Override
					public Indexable<BuildConfiguration> getBuildConfigurations() {
						return new ArrayList2<>();
					}
				}, buildTargets)
			);
		}
	}
	
	protected final BuildManager buildMgr = BuildManager.getInstance();
	
	protected SampleBuildProject sampleProject;
	
	protected SampleBuildProject initSampleProject() throws CoreException {
		sampleProject = new SampleBuildProject(getClass().getSimpleName());
		return sampleProject;
	}
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		
		try(SampleProject sampleProj = initSampleProject()){
			
			BuildTargetSettingsValidator target1Validator = btsValidator("SampleTarget", null, null);
			assertEquals(target1Validator.getValidBuildTarget(), 
				new BuildTarget("SampleTarget", true, null, null));
			
			target1Validator.getArtifactPath();
			
			BuildTargetSettingsValidator target2Validator = getBuiltTargetSettingsValidator(
				sampleProj.getName(), "SampleTarget2", null, null);
			assertEquals(target2Validator.getValidBuildTarget(), 
				new BuildTarget("SampleTarget2", true, "sample args", "sample path"));
			
			assertEquals(btsValidator("SampleTarget", "ARGS", "EXEPATH").getValidBuildTarget(), 
				new BuildTarget("SampleTarget", true, "ARGS", "EXEPATH"));
			
			assertEquals(btsValidator("ImplicitTarget", "ARGS", "EXEPATH").getValidBuildTarget(), 
				new BuildTarget("ImplicitTarget", false, "ARGS", "EXEPATH"));
		}
		
	}
	
	protected BuildTargetSettingsValidator btsValidator(
			String buildTargetName, String buildArguments, String artifactPath) {
		return getBuiltTargetSettingsValidator(sampleProject.getName(), buildTargetName, buildArguments, artifactPath);
	}
	
	protected BuildTargetSettingsValidator getBuiltTargetSettingsValidator(
			String projectName, String buildTargetName, String buildArguments, String artifactPath) {
		BuildTargetSettingsValidator btProcessor = new BuildTargetSettingsValidator() {
			
			@Override
			public String getProjectName() throws CommonException {
				return projectName;
			}
			
			@Override
			public String getBuildTargetName() {
				return buildTargetName;
			}
			
			@Override
			public String getBuildArguments() {
				return buildArguments;
			}
			
			@Override
			public String getArtifactPath() {
				return artifactPath;
			}
		};
		return btProcessor;
	}
	
}