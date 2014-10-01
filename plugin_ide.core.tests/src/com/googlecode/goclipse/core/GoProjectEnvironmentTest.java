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
		
		GoEnvironmentPrefConstants.GO_ROOT.set(SAMPLE_GO_ROOT.toString());
		GoEnvironmentPrefConstants.GO_ARCH.set("386");
		GoEnvironmentPrefConstants.GO_OS.set("windows");
		GoEnvironmentPrefConstants.GO_PATH.set(SAMPLE_GO_PATH.toString());
		
		// Test that it works with null
		GoEnvironment goEnvironment = GoProjectEnvironment.getGoEnvironment(null); 
		
		assertEquals(goEnvironment.getGoRoot().asString(), SAMPLE_GO_ROOT.toString());
		assertEquals(goEnvironment.getGoPathString(), SAMPLE_GO_PATH.toString());
		assertEquals(goEnvironment.getGoPathElements(), list(SAMPLE_GO_PATH.toString()));
	}
	
}
