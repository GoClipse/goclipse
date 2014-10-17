/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.core;

import static com.googlecode.goclipse.tooling.CommonGoToolingTest.SAMPLE_GOPATH_Entry;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;

import melnorme.utilbox.tests.TestsWorkingDir;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.junit.Test;

import com.googlecode.goclipse.tooling.CommonGoToolingTest;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.tooling.env.GoPath;
import com.googlecode.goclipse.tooling.env.GoRoot;

public class GoProjectEnvironmentTest extends CommonGoCoreTest {
	
	public static final Path WORKING_DIR = CommonGoToolingTest.TESTS_WORKDIR;
	public static final GoRoot SAMPLE_GO_ROOT = CommonGoToolingTest.SAMPLE_GO_ROOT;
	public static final GoPath SAMPLE_GO_PATH = CommonGoToolingTest.SAMPLE_GO_PATH;
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		
		GoEnvironmentPrefs.GO_ROOT.set(SAMPLE_GO_ROOT.asString());
		GoEnvironmentPrefs.GO_ARCH.set("386");
		GoEnvironmentPrefs.GO_OS.set("windows");
		GoEnvironmentPrefs.GO_PATH.set(SAMPLE_GOPATH_Entry.toString());
		TestsWorkingDir.deleteDir(SAMPLE_GOPATH_Entry);
		
		{
			// Test that it works with null
			GoEnvironment goEnvironment = GoProjectEnvironment.getGoEnvironment(null); 
			
			assertEquals(goEnvironment.getGoRoot().asString(), SAMPLE_GO_ROOT.asString());
			assertEquals(goEnvironment.getGoPathString(), SAMPLE_GO_PATH.asString());
			assertEquals(goEnvironment.getGoPathEntries(), list(SAMPLE_GOPATH_Entry.toString()));
		}
		
		// Test with sample project.
		try (SampleProject sampleProject = new SampleProject(getClass().getSimpleName())){
			IProject project = sampleProject.getProject();
			IPath location = project.getLocation();
			
			// Test that project location is added to effective GOPATH entries 
			checkEnvGoPath(project, list(location.toOSString(), SAMPLE_GOPATH_Entry.toString()));
			
			String goPathEntryOther = location.append("other").toOSString();
			String gopath = location.toOSString() + File.pathSeparator + goPathEntryOther;
			GoEnvironmentPrefs.GO_PATH.set(gopath);
			
			// Test GOPATH which already has project location. Also nested GOPATH entry.
			checkEnvGoPath(project, list(location.toOSString(), goPathEntryOther));
		}
		
		try (SampleProject sampleProject = new SampleProject(getClass().getSimpleName())){
			IProject project = sampleProject.getProject();
			
			GoEnvironmentPrefs.GO_PATH.set(SAMPLE_GO_PATH.asString());
			
			sampleProject.moveToLocation(SAMPLE_GOPATH_Entry.resolve("src/github.com/foo"));
			// Test that project location is not added, because project is in a Go source package
			checkEnvGoPath(project, list(SAMPLE_GOPATH_Entry.toString()));
			
			sampleProject.moveToLocation(SAMPLE_GOPATH_Entry.resolve("src/foo")); // Test 1 dir under 'src'
			checkEnvGoPath(project, list(SAMPLE_GOPATH_Entry.toString()));
			
			sampleProject.moveToLocation(SAMPLE_GOPATH_Entry.resolve("../temp"));
			TestsWorkingDir.deleteDir(SAMPLE_GOPATH_Entry);
			sampleProject.moveToLocation(SAMPLE_GOPATH_Entry.resolve("src")); // Test under 'src'
			checkEnvGoPath(project, list(SAMPLE_GOPATH_Entry.toString()));
		}
		
	}
	
	protected void checkEnvGoPath(IProject project, Collection<String> list) {
		GoEnvironment goEnv = GoProjectEnvironment.getGoEnvironment(project);
		assertEquals(goEnv.getGoPathEntries(), list);
	}
	
}