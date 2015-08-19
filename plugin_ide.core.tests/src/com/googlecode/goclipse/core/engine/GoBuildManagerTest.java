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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.googlecode.goclipse.core.CommonGoCoreTest;
import com.googlecode.goclipse.core.operations.GoBuildManager;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.operations.build.ValidatedBuildTarget;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.MiscUtil;

public class GoBuildManagerTest extends CommonGoCoreTest {
	
	public final static String PROJ_NAME = GoBuildManagerTest.class.getSimpleName();
	
	protected static IProject project;
	
	@BeforeClass
	public static void classSetup() throws CoreException {
		project = createLangProject(PROJ_NAME, true);
	}

	@AfterClass
	public static void classTeardown() throws CoreException {
		ResourceUtils.tryDeleteProject(PROJ_NAME);
	}
	
	protected static Location getProjectLocation() {
		return ResourceUtils.loc(project.getLocation());
	}
	
	protected BuildManager getBuildManager() {
		return LangCore.getBuildManager();
	}
	
	/* -----------------  ----------------- */
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		BuildManager buildMgr = LangCore.getBuildManager();
		
		ProjectBuildInfo pbi = buildMgr.getBuildInfo(project);
		assertTrue(pbi != null);
		assertTrue(pbi.getBuildTargets().size() == GoBuildManager.BUILD_TYPES.size());
		
		testGetBuildTargetFor(pbi, "go_package", "go_package", "build");
		testGetBuildTargetFor(pbi, "go_package #build", "go_package", "build");
		testGetBuildTargetFor(pbi, "foo/go_package #build", "foo/go_package", "build", "go_package");
		
		testGetBuildTargetFor(pbi, "go_foo #"+BUILD_TYPE_BuildTests, "go_foo", BUILD_TYPE_BuildTests, "go_foo.test");
		testGetBuildTargetFor(pbi, "go_foo #"+BUILD_TYPE_RunTests, "go_foo", BUILD_TYPE_RunTests, null);
	}
	
	protected BuildTarget testGetBuildTargetFor(ProjectBuildInfo buildInfo, String targetName, String buildConfig, 
			String buildType) throws CommonException, CoreException {
		return testGetBuildTargetFor(buildInfo, targetName, buildConfig, buildType, buildConfig);
	}
	
	protected BuildTarget testGetBuildTargetFor(ProjectBuildInfo buildInfo, String targetName, String goPackageName, 
			String buildType, String relArtifactPath) throws CommonException, CoreException {
		BuildTarget buildTarget = buildInfo.getBuildTargetFor(targetName);
		assertAreEqual(buildTarget.getTargetName(), targetName);

		ValidatedBuildTarget bt = getBuildManager().getValidatedBuildTarget(project, buildTarget);
		assertAreEqual(bt.getBuildConfigName(), goPackageName);
		assertAreEqual(bt.getBuildTypeName(), buildType);
		
		if(relArtifactPath == null) {
			verifyThrows(() -> bt.getDefaultArtifactPaths(), CommonException.class);
		} else {
			Location binLocation = getProjectLocation().resolve("bin");
			assertAreEqual(
				bt.getDefaultArtifactPaths(), 
				new ArrayList2<>(binLocation.resolve(relArtifactPath + MiscUtil.getExecutableSuffix()).toString())
			);
			
		}
		
		return buildTarget;
	}
	
}