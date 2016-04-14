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
package com.googlecode.goclipse.core.engine;

import static com.googlecode.goclipse.core.operations.GoBuildManager.BUILD_TYPE_BuildTests;
import static com.googlecode.goclipse.core.operations.GoBuildManager.BUILD_TYPE_RunTests;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.googlecode.goclipse.core.CommonGoCoreTest;
import com.googlecode.goclipse.core.GoEnvironmentPrefs;
import com.googlecode.goclipse.core.operations.GoBuildManager;
import com.googlecode.goclipse.tooling.CommonGoToolingTest;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.NullOperationMonitor;
import melnorme.lang.ide.core.operations.ToolManager;
import melnorme.lang.ide.core.operations.ToolchainPreferences;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.operations.build.CommonBuildTargetOperation;
import melnorme.lang.ide.core.operations.build.ProjectBuildInfo;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.tests.TestsWorkingDir;

public class GoBuildManagerTest extends CommonGoCoreTest {
	
	public final static String PROJ_NAME = GoBuildManagerTest.class.getSimpleName();
	
	public static final Location TESTS_GO_WORKSPACE = TestsWorkingDir.getWorkingDir("TestsGoWorkspace");
	
	protected GoBuildManager buildMgr = LangCore.getBuildManager();
	protected IProject project;
	protected IProject project2;
	
	@Before
	public void classSetup() throws CoreException, CommonException {
		project = createLangProject(PROJ_NAME, true);
		
		project2 = ResourceUtils.createAndOpenProject(PROJ_NAME + "_SubGOPATHEntry", 
			epath(TESTS_GO_WORKSPACE.resolve_fromValid("src/package")), true, null);
		setupLangProject(project2, false);
		
		GoEnvironmentPrefs.GO_ROOT.setValue(project, CommonGoToolingTest.MOCK_GOROOT.toString());
		GoEnvironmentPrefs.GO_ROOT.setValue(project2, CommonGoToolingTest.MOCK_GOROOT.toString());
		ToolchainPreferences.USE_PROJECT_SETTINGS.setValue(project, true);
		ToolchainPreferences.USE_PROJECT_SETTINGS.setValue(project2, true);
	}

	@After
	public void classTeardown() throws CoreException {
		ResourceUtils.tryDeleteProject(PROJ_NAME);
	}
	
	protected Location getProjectLocation() {
		return ResourceUtils.loc(project.getLocation());
	}
	
	protected ToolManager getToolManager() {
		return buildMgr.getToolManager();
	}
	
	/* -----------------  ----------------- */
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		
		ProjectBuildInfo pbi = buildMgr.getBuildInfo(project);
		assertTrue(pbi != null);
		assertTrue(pbi.getBuildTargets().size() == GoBuildManager.BUILD_TYPES.size());
		
		testGetBuildTargetFor(pbi, "build:go_package", "go_package", "build");
		testGetBuildTargetFor(pbi, "build:foo/go_package", "foo/go_package", "build", "go_package");
		testGetBuildTargetFor(pbi, BUILD_TYPE_BuildTests+":go_foo", "go_foo", BUILD_TYPE_BuildTests, "go_foo.test");
		testGetBuildTargetFor(pbi, BUILD_TYPE_RunTests+":go_foo", "go_foo", BUILD_TYPE_RunTests, null);
		
		// Test without separator
		testGetBuildTargetFor(pbi, "go_package", "go_package", "build");
		
		testBuildOperation();
	}
	
	protected BuildTarget testGetBuildTargetFor(ProjectBuildInfo buildInfo, String targetName, String buildConfig, 
			String buildType) throws CommonException, CoreException {
		return testGetBuildTargetFor(buildInfo, targetName, buildConfig, buildType, buildConfig);
	}
	
	protected BuildTarget testGetBuildTargetFor(ProjectBuildInfo buildInfo, String targetName, String goPackageName, 
			String buildType, String relArtifactPath) throws CommonException, CoreException {
		BuildTarget bt = buildMgr.getBuildTarget_x(buildInfo, targetName, false, true);
		assertAreEqual(bt.getTargetName(), targetName);

		assertAreEqual(bt.getBuildConfigName(), goPackageName);
		assertAreEqual(bt.getBuildTypeName(), buildType);
		
		if(relArtifactPath == null) {
			verifyThrows(() -> bt.getEffectiveValidExecutablePath(), CommonException.class);
		} else {
			Location binLocation = getProjectLocation().resolve("bin");
			assertAreEqual(
				bt.getEffectiveValidExecutablePath(), 
				binLocation.resolve(relArtifactPath + MiscUtil.getExecutableSuffix()).toString()
			);
			
		}
		
		return bt;
	}
	
	protected void testBuildOperation() throws CommonException, OperationCancellation {
		BuildTarget bt = buildMgr.getBuildInfo(project).getBuildTargets().iterator().next();
		
		GoEnvironmentPrefs.GO_ROOT.setValue(project, "");
		
		// Test GOROOT validation
		verifyThrows(() -> getBuildOperation(bt), null, "GOROOT is empty");
		
		// setup GOROOT 
		GoEnvironmentPrefs.GO_ROOT.setValue(project, CommonGoToolingTest.MOCK_GOROOT.toString());
		
		verifyThrows(() -> getBuildOperation(bt), null, "location does not contain a `src`");
		// setup src dir
		createSourceDir(getProjectLocation());
		
		// Test operation working directory (for projects that are a Go-workspace)
		assertEquals(
			getBuildOperation(bt).getToolProcessBuilder().directory().toString(), 
			srcLoc(getProjectLocation()).toString());
		
		/* ----------------- setup a project not CONTAINED in a Go-workspace 'src' entry ----------------- */
		Location projectParentLoc = getProjectLocation().resolve_fromValid("..");
		GoEnvironmentPrefs.GO_PATH.setValue(project, projectParentLoc.toString());
		GoEnvironmentPrefs.APPEND_PROJECT_LOC_TO_GOPATH.setValue(project, false);
//		createSourceDir(projectParentLoc);
		
		verifyThrows(() -> getBuildOperation(bt), null, "Project location is not part of the GOPATH");

		
		/* -----------------  ----------------- */
		GoEnvironmentPrefs.GO_PATH.setValue(project2, TESTS_GO_WORKSPACE.toString());
		GoEnvironmentPrefs.APPEND_PROJECT_LOC_TO_GOPATH.setValue(project2, false);
		BuildTarget bt2 = buildMgr.getBuildInfo(project2).getBuildTargets().iterator().next();
		
		// Test operation working directory
		assertEquals(
			getBuildOperation(bt2).getToolProcessBuilder().directory().toString(), 
			ResourceUtils.loc(project2.getLocation()).toString());
	}
	
	protected void createSourceDir(Location projectLoc) {
		assertTrue(srcLoc(projectLoc).toFile().mkdir());
	}
	
	protected Location srcLoc(Location projectLoc) {
		return projectLoc.resolve_fromValid("src");
	}
	
	protected CommonBuildTargetOperation getBuildOperation(BuildTarget bt) throws CommonException {
		return bt.getBuildOperation(getToolManager(), new NullOperationMonitor());
	}
	
}