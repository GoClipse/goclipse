/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.tooling;

import static melnorme.lang.tests.LangToolingTestResources.getTestResourceLoc;
import melnorme.lang.tests.CommonToolingTest;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.tests.TestsWorkingDir;

import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.tooling.env.GoPath;
import com.googlecode.goclipse.tooling.env.GoRoot;

public class CommonGoToolingTest extends CommonToolingTest {
	
	public static final Location TESTS_WORKDIR = TestsWorkingDir.getWorkingDir();
	
	public static final GoRoot SAMPLE_GO_ROOT = new GoRoot(TESTS_WORKDIR.resolve_valid("goRoot").toString());
	
	public static final Location SAMPLE_GOPATH_Entry = TESTS_WORKDIR.resolve_valid("goPathEntry");
	public static final GoPath SAMPLE_GO_PATH = new GoPath(SAMPLE_GOPATH_Entry.toString());
	
	protected static final GoEnvironment SAMPLE_GOEnv_1 = new GoEnvironment(
		SAMPLE_GO_ROOT, 
		SAMPLE_GO_PATH
	);
	
	public static final Location TR_SAMPLE_GOPATH_ENTRY = getTestResourceLoc("sampleGoPathEntry");
	
	public static String fixTestsPaths(String originalSource) {
		if(!MiscUtil.OS_IS_WINDOWS) {
			return originalSource.replaceAll("D:/devel/tools", "/devel/tools");
		}
		return originalSource;
	}
	
}