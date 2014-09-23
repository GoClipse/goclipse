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

import java.io.File;
import java.nio.file.Path;

import melnorme.utilbox.tests.TestsWorkingDir;

import org.junit.Test;

public class GoWorkspaceTest extends CommonGoCoreTest {
	
	protected static final Path BASEDIR = TestsWorkingDir.getWorkingDirPath();
	private static final Path WS_BAR = BASEDIR.resolve("WorkspaceBar");
	private static final Path WS_FOO = BASEDIR.resolve("WorkspaceFoo");
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		GoWorkspace2 goWorkspace = new GoWorkspace2(WS_FOO + File.pathSeparator + WS_BAR);
		 
		assertAreEqual(goWorkspace.getGoWorkspacePathEntry(WS_FOO.resolve("xxx")), WS_FOO);
		assertAreEqual(goWorkspace.getGoWorkspacePathEntry(WS_BAR.resolve("xxx")), WS_BAR);
		assertAreEqual(goWorkspace.getGoWorkspacePathEntry(BASEDIR.resolve("xxx")), null);
		
		assertAreEqual(goWorkspace.getGoPackageFromGoModule(WS_FOO.resolve("xxx/m.go")), null);
		assertAreEqual(goWorkspace.getGoPackageFromGoModule(WS_FOO.resolve("src/xxx/m.go")), path("xxx"));
		assertAreEqual(goWorkspace.getGoPackageFromGoModule(WS_FOO.resolve("src/xxx/zzz/m.go")), path("xxx/zzz"));
		assertAreEqual(goWorkspace.getGoPackageFromGoModule(WS_FOO.resolve("src/m.go")), null);
		assertAreEqual(goWorkspace.getGoPackageFromGoModule(WS_BAR.resolve("src/xxx/m.go")), path("xxx"));
		assertAreEqual(goWorkspace.getGoPackageFromGoModule(WS_BAR.resolve("src/src/src/m.go")), path("src/src"));
		assertAreEqual(goWorkspace.getGoPackageFromGoModule(BASEDIR.resolve("src/xxx/m.go")), null);
	}
	
}