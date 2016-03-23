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

import java.nio.file.Path;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.junit.Test;

import melnorme.lang.ide.core.BundleInfo;
import melnorme.lang.ide.core.launch.BuildTargetValidator;
import melnorme.lang.ide.core.launch.LaunchMessages;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.IOperationConsoleHandler;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.core.tests.BuildTestsHelper;
import melnorme.lang.ide.core.tests.SampleProject;
import melnorme.lang.tooling.bundle.BuildConfiguration;
import melnorme.utilbox.collections.ArrayList2;
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
				new BuildTarget("SampleTarget", true, null, null, null),
				new BuildTarget("SampleTarget2", true, "sample args", "--check_args", "sample path")
			);
			
			BundleInfo bundleInfo = BuildTestsHelper.createSampleBundleInfoA("foo", null);
			buildMgr.setProjectBuildInfo(project, new ProjectBuildInfo(buildMgr, project, bundleInfo, buildTargets));
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
			
			BuildTargetValidator target1Validator = btsValidator("SampleTarget", null, null);
			assertEquals(target1Validator.getValidBuildTarget(), 
				new BuildTarget("SampleTarget", true, null, null, null));
			
			target1Validator.getExecutablePath();
			
			BuildTargetValidator target2Validator = getBuiltTargetSettingsValidator(
				sampleProj.getName(), "SampleTarget2", null, null);
			assertEquals(target2Validator.getValidBuildTarget(), 
				new BuildTarget("SampleTarget2", true, "sample args", "--check_args", "sample path"));
			
			assertEquals(btsValidator("SampleTarget", "ARGS", "EXEPATH").getValidBuildTarget(), 
				new BuildTarget("SampleTarget", true, "ARGS", null, "EXEPATH"));
			
			assertEquals(btsValidator("ImplicitTarget", "ARGS", "EXEPATH").getValidBuildTarget(), 
				new BuildTarget("ImplicitTarget", false, "ARGS", null, "EXEPATH"));
		}
		
	}
	
	protected BuildTargetValidator btsValidator(
			String buildTargetName, String buildArguments, String artifactPath) {
		return getBuiltTargetSettingsValidator(sampleProject.getName(), buildTargetName, buildArguments, artifactPath);
	}
	
	protected BuildTargetValidator getBuiltTargetSettingsValidator(
			String projectName, String buildTargetName, String buildArguments, String artifactPath) {
		BuildTargetValidator btProcessor = new BuildTargetValidator() {
			
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
			public String getExecutablePath() {
				return artifactPath;
			}
		};
		return btProcessor;
	}
	
	/* -----------------  ----------------- */
	
	@Test
	public void testType() throws Exception { testType$(); }
	public void testType$() throws Exception {
		
		BuildManager.BuildType buildType = new BuildManager.BuildType("default") {
			
			@Override
			protected void getDefaultBuildOptions(ValidatedBuildTarget vbt, ArrayList2<String> buildArgs) {
			}
			
			@Override
			protected BuildConfiguration getValidBuildconfiguration(String buildConfigName, ProjectBuildInfo buildInfo)
					throws CommonException {
				return new BuildConfiguration(buildConfigName, null);
			}
			
			@Override
			public CommonBuildTargetOperation getBuildOperation(ValidatedBuildTarget validatedBuildTarget, 
					IOperationConsoleHandler opHandler, Path buildToolPath) throws CommonException, CoreException {
				return null;
			}
			
		};
		
		
		try(SampleProject sampleProj = initSampleProject()){
			
			IProject project = sampleProj.getProject();
			
			BuildTarget targetA = new BuildTarget("SampleTarget", true, null, null, null);
			ValidatedBuildTarget validatedTargetA = buildType.getValidatedBuildTarget(project, targetA, "");
			verifyThrows(() -> validatedTargetA.getEffectiveValidExecutablePath(), CommonException.class, 
				LaunchMessages.MSG_BuildTarget_NoExecutableAvailable());
			
			BuildTarget target2 = new BuildTarget("SampleTarget2", true, "sample args", "-check", "sample path");
			ValidatedBuildTarget validatedTarget2 = buildType.getValidatedBuildTarget(project, target2, "");
			
			assertAreEqual(validatedTarget2.getEffectiveValidExecutablePath(), "sample path");
			
		}
	}
	
}