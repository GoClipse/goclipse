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
package com.googlecode.goclipse.tooling.env;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.util.HashSet;

import melnorme.utilbox.misc.MiscUtil;

import org.junit.Test;

import com.googlecode.goclipse.tooling.CommonGoToolingTest;
import com.googlecode.goclipse.tooling.GoPackageName;

public class GoEnvironmentTest extends CommonGoToolingTest {
	
	private static final Path WS_BAR = TESTS_WORKDIR.resolve("WorkspaceBar");
	private static final Path WS_FOO = TESTS_WORKDIR.resolve("WorkspaceFoo");
	
	public static GoPackageName gopackage(String pathString) {
		return GoPackageName.fromPath(MiscUtil.createPathOrNull(pathString));
	}
	
	@Test
	public void test_GoPath() throws Exception { test_GoPath$(); }
	public void test_GoPath$() throws Exception {
		GoPath goPath = new GoPath(WS_FOO + File.pathSeparator + WS_BAR);
		 
		assertAreEqual(goPath.findGoPathEntry(WS_FOO.resolve("xxx")), WS_FOO);
		assertAreEqual(goPath.findGoPathEntry(WS_BAR.resolve("xxx")), WS_BAR);
		assertAreEqual(goPath.findGoPathEntry(TESTS_WORKDIR.resolve("xxx")), null);
		
		assertAreEqual(goPath.findGoPackageForSourceFile(WS_FOO.resolve("xxx/m.go")), null);
		assertAreEqual(goPath.findGoPackageForSourceFile(WS_FOO.resolve("src/xxx/m.go")), gopackage("xxx"));
		assertAreEqual(goPath.findGoPackageForSourceFile(WS_FOO.resolve("src/xxx/zzz/m.go")), gopackage("xxx/zzz"));
		assertAreEqual(goPath.findGoPackageForSourceFile(WS_FOO.resolve("src/m.go")), null);
		assertAreEqual(goPath.findGoPackageForSourceFile(WS_BAR.resolve("src/xxx/m.go")), gopackage("xxx"));
		assertAreEqual(goPath.findGoPackageForSourceFile(WS_BAR.resolve("src/src/src/m.go")), gopackage("src/src"));
		assertAreEqual(goPath.findGoPackageForSourceFile(TESTS_WORKDIR.resolve("src/xxx/m.go")), null);
		
		// Test empty case
		goPath = new GoPath("");
		assertTrue(goPath.isEmpty());
		assertTrue(goPath.getGoPathEntries().size() == 0);
		assertEquals(goPath.getGoPathWorkspaceString(), "");
	}
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		
		GoEnvironment goEnv = SAMPLE_GOEnv_1;
		
		assertAreEqual(goEnv.getGoOS_GoArch_segment(), "windows_386");
		
		Path goRootSrc = goEnv.getGoRoot_Path().resolve("src/pkg");
		
		assertAreEqual(goEnv.findGoPackageForSourceModule(goRootSrc.resolve("pack/m.go")), gopackage("pack"));
		assertAreEqual(goEnv.findGoPackageForSourceModule(goRootSrc.resolve("pack/foo/m.go")), gopackage("pack/foo"));
		assertAreEqual(goEnv.findGoPackageForSourceModule(goRootSrc.resolve("../foo/m.go")), null);
	}
	
	
	@Test
	public void testFindSourcePackage() throws Exception { testFindSourcePackage$(); }
	public void testFindSourcePackage$() throws Exception {
		
		GoPath goPath = new GoPath(TR_SAMPLE_GOPATH_ENTRY.toString());
		
		HashSet<GoPackageName> sampleGoPathEntry_result = hashSet(
			gopackage("samplePackage"),
			gopackage("samplePackage/subpack"),
			gopackage("samplePackage/subpack/bar"),
			gopackage("samplePackage2/xxx")
		);
		
		assertEquals(goPath.findSourcePackages(TR_SAMPLE_GOPATH_ENTRY), sampleGoPathEntry_result);
		assertEquals(goPath.findSourcePackages(TR_SAMPLE_GOPATH_ENTRY.resolve("src")), sampleGoPathEntry_result);
		
		assertEquals(goPath.findSourcePackages(TR_SAMPLE_GOPATH_ENTRY.resolve("src/samplePackage")), hashSet(
			gopackage("samplePackage"),
			gopackage("samplePackage/subpack"),
			gopackage("samplePackage/subpack/bar")
		));
		
		assertEquals(goPath.findSourcePackages(TR_SAMPLE_GOPATH_ENTRY.resolve("src/samplePackage2")), hashSet(
			gopackage("samplePackage2/xxx")
		));
		
		 // Test no results
		assertEquals(goPath.findSourcePackages(TR_SAMPLE_GOPATH_ENTRY.resolve("..").normalize()), hashSet());
	}
	
}