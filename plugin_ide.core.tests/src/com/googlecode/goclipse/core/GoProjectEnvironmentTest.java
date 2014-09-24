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

import java.nio.file.Path;

import melnorme.utilbox.tests.TestsWorkingDir;

import org.junit.Test;

import com.googlecode.goclipse.tooling.GoEnvironment;

public class GoProjectEnvironmentTest extends CommonGoCoreTest {
	
	public static final Path WORKING_DIR = TestsWorkingDir.getWorkingDirPath();
	public static final Path SAMPLE_GO_ROOT = WORKING_DIR.resolve("root");
	public static final Path SAMLPE_GO_PATH = WORKING_DIR.resolve("goPath");
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		
		GoEnvironmentPrefConstants.GO_ROOT.set(SAMPLE_GO_ROOT.toString());
//		GoEnvironmentPrefConstants.GO_ARCH.set("386");
//		GoEnvironmentPrefConstants.GO_OS.set("windows");
		GoEnvironmentPrefConstants.GO_PATH.set(SAMLPE_GO_PATH.toString());
		
		// Test that it works with null
		GoEnvironment goEnvironment = GoProjectEnvironment.getGoEnvironment(null); 
		
		assertEquals(goEnvironment.getGoRoot(), SAMPLE_GO_ROOT.toString());
		assertEquals(goEnvironment.getGoPathString(), SAMLPE_GO_PATH.toString());
		assertEquals(goEnvironment.getGoPathElements(), list(SAMLPE_GO_PATH.toString()));
	}
	
}
