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
	
	public static final Path TESTS_WORKDIR = TestsWorkingDir.getWorkingDirPath();
	
	public static final GoRoot SAMPLE_GO_ROOT = new GoRoot(TESTS_WORKDIR.resolve("goRoot").toString());
	
	public static final Path SAMPLE_GOPATH_Entry = TESTS_WORKDIR.resolve("goPath");
	public static final GoPath SAMPLE_GO_PATH = new GoPath(SAMPLE_GOPATH_Entry.toString());
	
	protected static final GoEnvironment SAMPLE_GOEnv_1 = new GoEnvironment(
		SAMPLE_GO_ROOT, 
		new GoArch("386"), new GoOs("windows"), 
		SAMPLE_GO_PATH
	);
	
}