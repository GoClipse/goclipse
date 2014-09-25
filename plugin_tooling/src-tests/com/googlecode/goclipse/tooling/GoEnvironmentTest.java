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

import java.io.File;
import java.nio.file.Path;

import org.junit.Test;

public class GoEnvironmentTest extends CommonGoToolingTest {
	
	private static final Path WS_BAR = TESTS_DIR.resolve("WorkspaceBar");
	private static final Path WS_FOO = TESTS_DIR.resolve("WorkspaceFoo");
	
	@Test
	public void test_GoPath() throws Exception { test_GoPath$(); }
	public void test_GoPath$() throws Exception {
		GoPath goPath = new GoPath(WS_FOO + File.pathSeparator + WS_BAR);
		 
		assertAreEqual(goPath.getGoWorkspacePathEntry(WS_FOO.resolve("xxx")), WS_FOO);
		assertAreEqual(goPath.getGoWorkspacePathEntry(WS_BAR.resolve("xxx")), WS_BAR);
		assertAreEqual(goPath.getGoWorkspacePathEntry(TESTS_DIR.resolve("xxx")), null);
		
		assertAreEqual(goPath.getGoPackageFromGoModule(WS_FOO.resolve("xxx/m.go")), null);
		assertAreEqual(goPath.getGoPackageFromGoModule(WS_FOO.resolve("src/xxx/m.go")), path("xxx"));
		assertAreEqual(goPath.getGoPackageFromGoModule(WS_FOO.resolve("src/xxx/zzz/m.go")), path("xxx/zzz"));
		assertAreEqual(goPath.getGoPackageFromGoModule(WS_FOO.resolve("src/m.go")), null);
		assertAreEqual(goPath.getGoPackageFromGoModule(WS_BAR.resolve("src/xxx/m.go")), path("xxx"));
		assertAreEqual(goPath.getGoPackageFromGoModule(WS_BAR.resolve("src/src/src/m.go")), path("src/src"));
		assertAreEqual(goPath.getGoPackageFromGoModule(TESTS_DIR.resolve("src/xxx/m.go")), null);
	}
	
}