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
package com.googlecode.goclipse.tooling;

import java.nio.file.Path;

import melnorme.utilbox.tests.CommonTestExt;
import melnorme.utilbox.tests.TestsWorkingDir;

public class CommonGoToolingTest extends CommonTestExt {
	
	public static final Path TESTS_DIR = TestsWorkingDir.getWorkingDirPath();
	
	public static final Path SAMPLE_GO_ROOT = TESTS_DIR.resolve("root");
	public static final Path SAMPLE_GO_PATH = TESTS_DIR.resolve("goPath");
	
	protected static final GoEnvironment SAMPLE_GOEnv_1 = new GoEnvironment(
		SAMPLE_GO_ROOT.toString(), 
		"386", "windows", 
		SAMPLE_GO_PATH.toString()
	);
	
}