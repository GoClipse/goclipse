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

import static melnorme.lang.ide.core.operations.build.BuildTargetsSerializer_Test.bt;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.nio.file.Path;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.junit.Test;

import melnorme.lang.ide.core.BundleInfo;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.launch.CompositeBuildTargetSettings;
import melnorme.lang.ide.core.launch.LaunchMessages;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.IOperationConsoleHandler;
import melnorme.lang.ide.core.operations.build.BuildManager.BuildType;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.core.tests.BuildTestsHelper;
import melnorme.lang.ide.core.tests.SampleProject;
import melnorme.lang.tooling.bundle.BuildConfiguration;
import melnorme.lang.tooling.bundle.BuildTargetNameParser;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.tests.CommonTest;

public class BuildManager_Test extends CommonTest {
	
	protected final BuildManager buildMgr = new TestsBuildManager(LangCore.getBundleModel());

	protected String SEP = buildMgr.getBuildTargetNameParser().getNameSeparator();
	
	public static final BuildTargetData sampleBT_A = 
			bt("TargetA", true, null, null, null);
	public static final BuildTargetData sampleBT_B = 
			bt("TargetB", true, "B: build_args", "B: check_args", "B: exe_path");
	
	public final BuildTargetData sampleBT_STRICT = 
			bt("ConfigA"+SEP+"strict", true, "S: build_args", "S: check_args", "S: exe_path");
	
	protected final ArrayList2<BuildTargetData> DEFAULT_TARGETS = list(
		sampleBT_A,
		sampleBT_B,
		sampleBT_STRICT
	);
	
	public class TestsBuildManager extends BuildManager {
		public TestsBuildManager(LangBundleModel bundleModel) {
			super(bundleModel);
		}
		
		@Override
		public BuildTargetNameParser getBuildTargetNameParser() {
			return new BuildTargetNameParser() {
				@Override
				public String getNameSeparator() {
					return "#";
				};
			};
		}
		
		@Override
		protected Indexable<BuildType> getBuildTypes_do() {
			return list(
				new SampleBuildType("default"),
				new SampleStrictBuildType("strict")
			);
		}
		
		@Override
		protected ArrayList2<BuildTarget> getDefaultBuildTargets(IProject project, BundleInfo newBundleInfo) {
			Indexable<BuildTargetData> dEFAULT_TARGETS2 = DEFAULT_TARGETS;
			return createBuildTargets(project, dEFAULT_TARGETS2);
		}
	}
	
	public ArrayList2<BuildTarget> createBuildTargets(IProject project, Indexable<BuildTargetData> buildTargetsData) {
		try {
			return buildTargetsData.mapx((buildTargetData) -> {
				return buildMgr.createBuildTarget3(project, buildTargetData);
			});
		} catch(CommonException e) {
			throw assertFail();
		}
	}

	public class SampleStrictBuildType extends BuildType {
		public SampleStrictBuildType(String name) {
			super(name);
		}
		
		@Override
		protected void getDefaultBuildOptions(BuildTarget bt, ArrayList2<String> buildArgs)
				throws CommonException {
			buildArgs.add("build this");
		}
		
		@Override
		public CommonBuildTargetOperation getBuildOperation(BuildTarget bt,
				IOperationConsoleHandler opHandler, Path buildToolPath) throws CommonException, CoreException {
			return null;
		}
		
		@Override
		protected BuildConfiguration getValidBuildconfiguration(String buildConfigName, BundleInfo bundleInfo)
				throws CommonException {
			if(list("ConfigA", "ConfigB").contains(buildConfigName)) {
				return new BuildConfiguration(buildConfigName, null);
			}
			throw new CommonException(BuildManagerMessages.BuildConfig_NotFound(buildConfigName));
		}
	}

	public class SampleBuildType extends SampleStrictBuildType {
		
		public SampleBuildType(String name) {
			super(name);
		}
		
		@Override
		protected BuildConfiguration getValidBuildconfiguration(String buildConfigName,
				BundleInfo bundleInfo) throws CommonException {
			// Allow implicit configurations
			return new BuildConfiguration(buildConfigName, null);
		}
	}

	
	protected SampleProject sampleProject;
	protected IProject project;
	
	protected SampleProject initSampleProject() throws CoreException, CommonException {
		this.sampleProject = new SampleProject(getClass().getSimpleName());
		this.project = sampleProject.getProject();
		return sampleProject;
	}
	
	protected final BundleInfo bundleInfo = BuildTestsHelper.createSampleBundleInfoA("SampleBundle", null);
	
	/* -----------------  ----------------- */
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		
		try(SampleProject sampleProj = initSampleProject()){
			
			buildMgr.loadProjectBuildInfo(project, bundleInfo);
			
			ProjectBuildInfo buildInfo = buildMgr.getBuildInfo(project);
			assertNotNull(buildInfo);
			checkBuildTargets(buildInfo.getBuildTargets().toArrayList(), list(
				sampleBT_A,
				sampleBT_B,
				sampleBT_STRICT)
			);

			assertEquals(
				buildMgr.getValidBuildTarget(project, "TargetA", true).getData(),
				sampleBT_A);
			verifyThrows(
				() -> buildMgr.getValidBuildTarget(project, "TargetA#default", true).getData(),
				CommonException.class,
				LaunchMessages.BuildTarget_NotFound);
			
			verifyThrows(
				() -> buildMgr.getValidBuildTarget(project, "TargetA"+SEP+"bad_config", false).getData(),
				CommonException.class,
				"No such build type: `bad_config`"); // Build Type not found
			
			assertEquals(
				buildMgr.getValidBuildTarget(project, "ImplicitTarget"+SEP+"default", false).getData(),
				bt("ImplicitTarget"+SEP+"default", false, null, null, null));
			
			verifyThrows(
				() -> buildMgr.getValidBuildTarget(project, "ImplicitTarget"+SEP+"strict", false).getData(),
				CommonException.class,
				"Build configuration `ImplicitTarget` not found"); // Config not found
			
			
			testSaveLoadProjectInfo();
		}
		
	}
	
	protected void testSaveLoadProjectInfo() throws CommonException {
		
		SampleStrictBuildType buildType = new SampleStrictBuildType("default");
		BuildConfiguration buildConfig = new BuildConfiguration("configA", null);
		
		BuildTarget btA = new BuildTarget(project, bundleInfo, bt("TargetA", false, "new1", "new2", "new3"), 
			buildType, buildConfig);
		BuildTarget btNonExistentButValid = new BuildTarget(project, bundleInfo, 
			new BuildTargetData("TargetA" + SEP + "default", true), 
			buildType, buildConfig);
		BuildTarget btNonExistent = new BuildTarget(project, bundleInfo, 
			new BuildTargetData("TargetA" + SEP + "NonExistentType", true), 
			buildType, buildConfig);
		
		ProjectBuildInfo newProjectBuildInfo = new ProjectBuildInfo(buildMgr, project, bundleInfo, 
			new ArrayList2<>(btA, btNonExistentButValid, btNonExistent));
		buildMgr.setProjectBuildInfoAndSave(project, newProjectBuildInfo);
		
		buildMgr.getBuildModel().removeProjectInfo(project);
		assertTrue(buildMgr.getBuildModel().getProjectInfo(project) == null);
		buildMgr.loadProjectBuildInfo(project, bundleInfo);
		
		ProjectBuildInfo buildInfo = buildMgr.getBuildInfo(project);
		checkBuildTargets(buildInfo.getBuildTargets().toArrayList(), list(
			bt("TargetA", false, "new1", "new2", "new3"), // Ensure TargetA uses previous settings
			sampleBT_B,
			sampleBT_STRICT)
		);
		
	}
	
	public void checkBuildTargets(Indexable<BuildTarget> buildTargets, Indexable<BuildTargetDataView> expectedSettings) {
		assertTrue(buildTargets.size() == expectedSettings.size());
		for(int ix = 0; ix < buildTargets.size(); ix++) {
			BuildTargetDataView expectedData = expectedSettings.get(ix);
			assertTrue(buildTargets.get(ix).getData().equals(expectedData));
		}
	}
	
	/* -----------------  ----------------- */
	
	@Test
	public void test_compositeBuildTargetSettings() throws Exception { test_compositeBuildTargetSettings$(); }
	public void test_compositeBuildTargetSettings$() throws Exception {
		
		try(SampleProject sampleProj = initSampleProject()){
			
			assertEquals(
				btSettings("TargetA", null, null).getValidBuildTarget().getData(), 
				bt("TargetA", true, null, null, null));
			
			assertEquals(
				btSettings("TargetB", null, null).getValidBuildTarget().getData(), 
				bt("TargetB", true, "B: build_args", "B: check_args", "B: exe_path"));
			
			assertEquals(
				btSettings("TargetB", "ARGS", "EXEPATH").getValidBuildTarget().getData(), 
				bt("TargetB", true, "ARGS", "B: check_args", "EXEPATH"));
			
			assertEquals(
				btSettings("ImplicitTarget", "ARGS", "EXEPATH").getValidBuildTarget().getData(), 
				bt("ImplicitTarget", false, "ARGS", null, "EXEPATH"));
		}
		
	}
	
	protected CompositeBuildTargetSettings btSettings(
			String buildTargetName, String buildArguments, String artifactPath) {
		return getBuiltTargetSettingsValidator(sampleProject.getName(), buildTargetName, buildArguments, artifactPath);
	}
	
	protected CompositeBuildTargetSettings getBuiltTargetSettingsValidator(
			String projectName, String buildTargetName, String buildArguments, String artifactPath) {
		CompositeBuildTargetSettings btSettings = new CompositeBuildTargetSettings() {
			
			@Override
			protected BuildManager getBuildManager() {
				return buildMgr;
			}
			
			@Override
			public String getProjectName() {
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
		return btSettings;
	}
	
	/* -----------------  ----------------- */
	
	@Test
	public void testBuildType() throws Exception { testBuildType$(); }
	public void testBuildType$() throws Exception {
		
		BuildManager.BuildType buildType = new SampleBuildType("default");
		
		try(SampleProject sampleProj = initSampleProject()){
			
			IProject project = sampleProj.getProject();

			ProjectBuildInfo buildInfo = buildMgr.getValidBuildInfo(project);
			BundleInfo bundleInfo = buildInfo.getBundleInfo();

			BuildTargetData targetA = bt("SampleTarget", true, null, null, null);
			BuildTarget buildTargetA = BuildTarget.create(project, bundleInfo, targetA, buildType, "");
			verifyThrows(() -> buildTargetA.getEffectiveValidExecutablePath(), CommonException.class, 
				LaunchMessages.MSG_BuildTarget_NoExecutableAvailable());
			
			BuildTargetData target2 = bt("SampleTarget2", true, "sample args", "-check", "sample path");
			BuildTarget buildTarget2 = BuildTarget.create(project, bundleInfo, target2, buildType, "");
			
			assertAreEqual(buildTarget2.getEffectiveValidExecutablePath(), "sample path");
			
		}
	}
}