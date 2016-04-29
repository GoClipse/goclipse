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

import static melnorme.lang.ide.core.LangCore_Actual.VAR_NAME_SdkToolPath;
import static melnorme.lang.ide.core.operations.build.BuildTargetsSerializer_Test.bt;
import static melnorme.lang.ide.core.operations.build.BuildTargetsSerializer_Test.cmd;
import static melnorme.lang.ide.core.operations.build.VariablesResolver.variableRefString;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.junit.Test;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.launch.BuildTargetSource;
import melnorme.lang.ide.core.launch.CompositeBuildTargetSettings;
import melnorme.lang.ide.core.launch.LaunchMessages;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.NullOperationMonitor;
import melnorme.lang.ide.core.operations.ToolManager;
import melnorme.lang.ide.core.operations.build.BuildManager_Test.TestsBuildManager.SampleStrictBuildType;
import melnorme.lang.ide.core.operations.build.BuildTargetOperation.BuildOperationParameters;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.lang.ide.core.tests.CoreTestWithProject;
import melnorme.lang.tests.ToolingTests_Actual;
import melnorme.lang.tooling.bundle.BuildConfigMessages;
import melnorme.lang.tooling.bundle.BuildConfiguration;
import melnorme.lang.tooling.bundle.BuildTargetNameParser;
import melnorme.lang.tooling.bundle.BundleInfo;
import melnorme.lang.tooling.data.StatusException;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public class BuildManager_Test extends CoreTestWithProject {
	
	public static final BuildTargetData sampleBT_A = 
			bt("TargetA", true, false, null, null);
	public static final BuildTargetData sampleBT_B = 
			bt("TargetB", false, true, "B: build_args", "B: exe_path");
	public static final BuildTargetData sampleBT_STRICT = 
			bt("ConfigA#strict", true, true, "S: build_args", "S: exe_path");
	
	public static final ArrayList2<BuildTargetData> DEFAULT_TARGETS = list(
		sampleBT_A,
		sampleBT_B,
		sampleBT_STRICT
	);
	
	protected final TestsBuildManager buildMgr = new TestsBuildManager(LangCore.getBundleModel());
	
	public static class TestsBuildManager extends BuildManager {
		
		public TestsBuildManager(LangBundleModel bundleModel) {
			super(bundleModel, LangCore.getToolManager());
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
			return createBuildTargets(project, DEFAULT_TARGETS);
		}
		
		public ArrayList2<BuildTarget> createBuildTargets(IProject project, Indexable<BuildTargetData> buildTargetsData) {
			try {
				return buildTargetsData.mapx((buildTargetData) -> {
					return createBuildTarget(project, buildTargetData);
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
			public String getDefaultCommandArguments(BuildTarget bt) throws CommonException {
				return "default: build_args";
			}
			
			@Override
			public BuildTargetOperation getBuildOperation(BuildOperationParameters buildOpParams)
					throws CommonException {
				return new BuildTargetOperation(buildOpParams) {
					@Override
					protected void processBuildOutput(ExternalProcessResult processResult, IProgressMonitor pm)
							throws CommonException, OperationCancellation {
					}
				};
			}
			
			@Override
			protected BuildConfiguration getValidBuildconfiguration(String buildConfigName, BundleInfo bundleInfo)
					throws CommonException {
				if(list("ConfigA", "ConfigB").contains(buildConfigName)) {
					return new BuildConfiguration(buildConfigName, null);
				}
				throw new CommonException(BuildConfigMessages.BuildConfig_NotFound(buildConfigName));
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
		
	}
	
	protected String SEP = buildMgr.getBuildTargetNameParser().getNameSeparator();
	
	protected final BundleInfo bundleInfo = ToolingTests_Actual.createSampleBundleInfoA("SampleBundle", null);
	
	/* -----------------  ----------------- */
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		initSampleProject();
		
		buildMgr.bundleProjectAdded(project, bundleInfo);
		
		ProjectBuildInfo buildInfo = buildMgr.getBuildInfo(project);
		assertNotNull(buildInfo);
		checkBuildTargets(buildInfo.getBuildTargets().toArrayList(), list(
			sampleBT_A,
			sampleBT_B,
			sampleBT_STRICT)
		);

		assertEquals(
			buildMgr.getBuildTarget(project, "TargetA", true).getData(),
			sampleBT_A);
		assertEquals(
			buildMgr.getBuildTarget(project, "TargetB", true).getData(),
			sampleBT_B);
		verifyThrows(
			() -> buildMgr.getBuildTarget(project, "TargetA#default", true).getData(),
			CommonException.class,
			LaunchMessages.BuildTarget_NotFound);
		
		verifyThrows(
			() -> buildMgr.getBuildTarget(project, "TargetA"+SEP+"bad_config", false).getData(),
			CommonException.class,
			"No such build type: `bad_config`"); // Build Type not found
		
		assertEquals(
			buildMgr.getBuildTarget(project, "ImplicitTarget"+SEP+"default", false).getData(),
			bt("ImplicitTarget"+SEP+"default", false, false, null, null));
		
		verifyThrows(
			() -> buildMgr.getBuildTarget(project, "ImplicitTarget"+SEP+"strict", false).getData(),
			CommonException.class,
			"Build configuration `ImplicitTarget` not found"); // Config not found
			
		
		test_compositeBuildTargetSettings();
		test_BuildType();
		
		testBuildOperation();
	}
	
	public void checkBuildTargets(Indexable<BuildTarget> buildTargets, Indexable<BuildTargetDataView> expectedSettings) {
		assertTrue(buildTargets.size() == expectedSettings.size());
		for(int ix = 0; ix < buildTargets.size(); ix++) {
			BuildTargetDataView expectedData = expectedSettings.get(ix);
			assertTrue(buildTargets.get(ix).getData().equals(expectedData));
		}
	}
	
	/* -----------------  ----------------- */
	
	public void test_compositeBuildTargetSettings() throws Exception {
		
		assertEquals(
			btSettings("TargetA", null, null).getValidBuildTarget().getData(), 
			bt("TargetA", true, false, null, null));
		
		assertEquals(
			btSettings("TargetB", null, null).getValidBuildTarget().getData(), 
			bt("TargetB", false, true, "B: build_args", "B: exe_path"));
		
		assertEquals(
			btSettings("TargetB", "ARGS", "EXEPATH").getValidBuildTarget().getData(), 
			bt("TargetB", false, true, "ARGS", "EXEPATH"));
		
		assertEquals(
			btSettings("ImplicitTarget", "ARGS", "EXEPATH").getValidBuildTarget().getData(), 
			bt("ImplicitTarget", false, false, "ARGS", "EXEPATH"));
		
	}
	
	protected CompositeBuildTargetSettings btSettings(
			String buildTargetName, String buildArguments, String artifactPath) {
		return getBuiltTargetSettingsValidator(project.getName(), buildTargetName, cmd(buildArguments), artifactPath);
	}
	
	protected CompositeBuildTargetSettings getBuiltTargetSettingsValidator(
			String projectName, String buildTargetName, CommandInvocation buildCommand, String artifactPath) {
		
		BuildTargetSource buildTargetSource = new BuildTargetSource() {
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
		};
		
		CompositeBuildTargetSettings btSettings = new CompositeBuildTargetSettings(buildTargetSource) {
			@Override
			public CommandInvocation getBuildCommand() {
				return buildCommand;
			}
			
			@Override
			public String getExecutablePath() {
				return artifactPath;
			}
		};
		return btSettings;
	}
	
	/* -----------------  ----------------- */
	
	public void test_BuildType() throws Exception {
		BuildManager.BuildType buildType = buildMgr.new SampleBuildType("default");
		
		ProjectBuildInfo buildInfo = buildMgr.getValidBuildInfo(project);
		BundleInfo bundleInfo = buildInfo.getBundleInfo();
		
		BuildTargetData targetA = bt("SampleTarget", true, true, null, null);
		BuildTarget buildTargetA = BuildTarget.create(project, bundleInfo, targetA, buildType, "");
		verifyThrows(() -> buildTargetA.getEffectiveValidExecutablePath(), CommonException.class, 
			LaunchMessages.MSG_BuildTarget_NoExecutableAvailable());
		
		BuildTargetData target2 = bt("SampleTarget2", true, true, "sample args", "sample path");
		BuildTarget buildTarget2 = BuildTarget.create(project, bundleInfo, target2, buildType, "");
		
		assertAreEqual(buildTarget2.getEffectiveValidExecutablePath(), "sample path");
			
	}
	
	protected NullOperationMonitor opMonitor = new NullOperationMonitor();
	
	protected void testBuildOperation() throws CommonException, StatusException {
		ToolManager toolMgr = buildMgr.getToolManager();
		ProjectBuildInfo buildInfo = buildMgr.getBuildInfo(project);
		
		BuildTarget btA = buildMgr.getBuildTarget(project, "TargetA", true);
		assertTrue(btA.getData().getBuildCommand() == null);
		
		assertAreEqual(
			btA.getBuildOperation(toolMgr, opMonitor).getEffectiveProccessCommandLine(), 
			list(strSDKTool(), "default:", "build_args")
		);
		
		BuildTarget btB = buildMgr.getBuildTarget(project, "TargetB", true);
		assertTrue(btB.getData().getBuildCommand() != null);
		
		assertAreEqual(
			btB.getBuildOperation(toolMgr, opMonitor).getEffectiveProccessCommandLine(), 
			list("B:", "build_args")
		);
		
		testBuildOperation_Vars(buildInfo, btB);
		
		// Test invalid command - empty
		verifyThrows(
			() -> getBuildOperation(buildInfo, btB, ""), 
			
			CommonException.class, "No command specified"
		);
		
	}
	
	protected void testBuildOperation_Vars(ProjectBuildInfo buildInfo, BuildTarget btB) throws CommonException {
		
		// Test var resolution - SDK tool var
		assertAreEqual(
			getBuildOperation(buildInfo, btB, variableRefString(VAR_NAME_SdkToolPath) + " build"),
			
			list(strSDKTool(), "build")
		);
		
		// Test var resolution - undefined var
		verifyThrows(
			() -> getBuildOperation(buildInfo, btB, "${XXX_NON_EXISTANT_VAR} build"), 
			
			CommonException.class, "undefined variable XXX_NON_EXISTANT_VAR"
		);
		
		// Test var resolution - invalid arg
		verifyThrows(
			() -> getBuildOperation(buildInfo, btB, "${" + LangCore_Actual.VAR_NAME_SdkToolPath + ":arg}" + " build"), 
			
			CommonException.class, LangCore_Actual.VAR_NAME_SdkToolPath + " does not accept arguments"
		);
		
	}
	
	protected Iterable<String> getBuildOperation(ProjectBuildInfo buildInfo, BuildTarget btB, String buildArguments)
			throws CommonException {
		BuildTargetData dataCopy = btB.getDataCopy();
		dataCopy.buildCommand = new CommandInvocation(buildArguments);
		BuildTarget newBuildTarget = buildInfo.buildMgr.createBuildTarget(buildInfo.project, dataCopy);
		
		ToolManager toolMgr = buildInfo.buildMgr.getToolManager();
		BuildTargetOperation buildOperation = newBuildTarget.getBuildOperation(toolMgr, opMonitor);
		return buildOperation.getConfiguredProcessBuilder().command();
	}
	
	/* -----------------  ----------------- */
	
	@Test
	public void test_SaveLoadProjectInfo() throws Exception { test_SaveLoadProjectInfo$(); }
	public void test_SaveLoadProjectInfo$() throws Exception {
		initSampleProject();
		
		SampleStrictBuildType buildType = buildMgr.new SampleStrictBuildType("default");
		BuildConfiguration buildConfig = new BuildConfiguration("configA", null);
		
		BuildTarget btA = new BuildTarget(project, bundleInfo, bt("TargetA", false, true, "new1", "new3"), 
			buildType, buildConfig);
		BuildTarget btNonExistentButValid = new BuildTarget(project, bundleInfo, 
			new BuildTargetData("TargetA" + SEP + "default", true, false), 
			buildType, buildConfig);
		BuildTarget btNonExistent = new BuildTarget(project, bundleInfo, 
			new BuildTargetData("TargetA" + SEP + "NonExistentType", false, true), 
			buildType, buildConfig);
		
		ProjectBuildInfo newProjectBuildInfo = new ProjectBuildInfo(buildMgr, project, bundleInfo, 
			new ArrayList2<>(btA, btNonExistentButValid, btNonExistent));
		buildMgr.setProjectBuildInfo(project, newProjectBuildInfo);
		buildMgr.saveProjectInfo(project);
		
		buildMgr.getBuildModel().removeProjectInfo(project);
		assertTrue(buildMgr.getBuildModel().getProjectInfo(project) == null);
		buildMgr.loadProjectBuildInfo(project, bundleInfo);
		
		ProjectBuildInfo buildInfo = buildMgr.getBuildInfo(project);
		checkBuildTargets(buildInfo.getBuildTargets().toArrayList(), list(
			bt("TargetA", false, true, "new1", "new3"), // Ensure TargetA uses previous settings
			sampleBT_B,
			sampleBT_STRICT)
		);
		
	}
	
}