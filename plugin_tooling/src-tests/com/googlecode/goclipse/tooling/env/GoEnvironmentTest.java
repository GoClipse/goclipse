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
package com.googlecode.goclipse.tooling.env;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.io.File;

import org.junit.Test;

import com.googlecode.goclipse.tooling.CommonGoToolingTest;
import com.googlecode.goclipse.tooling.GoPackageName;

import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.CollectionUtil;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.MiscUtil;

public class GoEnvironmentTest extends CommonGoToolingTest {
	
	public static Location SAMPLE_ROOT_PATH = Location.create_fromValid(path(MiscUtil.OS_IS_WINDOWS ? "C:/" : "/"));
	
	private static final Location WS_BAR = TESTS_WORKDIR.resolve_valid("WorkspaceBar");
	private static final Location WS_FOO = TESTS_WORKDIR.resolve_valid("WorkspaceFoo");
	
	public static GoPackageName goPkg(String pathString) {
		return GoPackageName.fromPath(MiscUtil.createPathOrNull(pathString));
	}
	
	@Test
	public void test_GoPackageName() throws Exception { test_GoPackageName$(); }
	public void test_GoPackageName$() throws Exception {
		GoPackageName.createValid("foo/bar");
		GoPackageName.createValid("foo/bar.asdf");
		GoPackageName.createValid("foo/bar..asdf");
		GoPackageName.createValid("foo/bar...asdf");
		
		if(MiscUtil.OS_IS_WINDOWS) {
			assertEquals(GoPackageName.createValid("foo\\bar").toString(), "foo/bar");
		}
		
		verifyThrows(() -> GoPackageName.createValid("foo/.."), CommonException.class);
		verifyThrows(() -> GoPackageName.createValid("./foo"), CommonException.class);
		verifyThrows(() -> GoPackageName.createValid(""), CommonException.class);
	}
	
	@Test
	public void test_GoPath() throws Exception { test_GoPath$(); }
	public void test_GoPath$() throws Exception {
		GoPath goPath = new GoPath(WS_FOO + File.pathSeparator + WS_BAR);
		
		assertAreEqual(goPath.findGoPathEntry(WS_FOO.resolve_valid("xxx")), new GoWorkspaceLocation(WS_FOO));
		assertAreEqual(goPath.findGoPathEntry(WS_BAR.resolve_valid("xxx")), new GoWorkspaceLocation(WS_BAR));
		assertAreEqual(goPath.findGoPathEntry(TESTS_WORKDIR.resolve_valid("xxx")), null);
		
		assertAreEqual(goPath.findGoPackageForLocation(WS_FOO.resolve_valid("xxx/")), null);
		assertAreEqual(goPath.findGoPackageForLocation(WS_FOO.resolve_valid("src/xxx/")), goPkg("xxx"));
		assertAreEqual(goPath.findGoPackageForLocation(WS_FOO.resolve_valid("src/xxx/zzz")), goPkg("xxx/zzz"));
		assertAreEqual(goPath.findGoPackageForLocation(WS_FOO.resolve_valid("src")), null);
		assertAreEqual(goPath.findGoPackageForLocation(WS_BAR.resolve_valid("src/xxx")), goPkg("xxx"));
		assertAreEqual(goPath.findGoPackageForLocation(WS_BAR.resolve_valid("src/src/src")), goPkg("src/src"));
		assertAreEqual(goPath.findGoPackageForLocation(TESTS_WORKDIR.resolve_valid("src/xxx")), null);
		
		// Test empty case
		goPath = new GoPath("");
		assertTrue(goPath.isEmpty());
		assertTrue(goPath.getGoPathEntries().size() == 0);
		assertEquals(goPath.getGoPathWorkspaceString(), "");
		
		verifyThrows(() -> new GoPath("").validate(), CommonException.class, "empty");
	}
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		
		GoEnvironment goEnv = SAMPLE_GOEnv_1;
		
		Location goRootSrc = goEnv.getGoRoot_Location().resolve_valid("src");
		
		assertAreEqual(goEnv.findGoPackageForSourceFile(goRootSrc.resolve_valid("pack/m.go")), 
			goPkg("pack"));
		assertAreEqual(goEnv.findGoPackageForSourceFile(goRootSrc.resolve_valid("pack/foo/m.go")), 
			goPkg("pack/foo"));
		assertAreEqual(goEnv.findGoPackageForSourceFile(goRootSrc.resolve_valid("../foo/m.go")), 
			null);
		
		assertAreEqual(goEnv.findGoPackageForSourceFile(SAMPLE_ROOT_PATH), null);
	}
	
	
	@Test
	public void testFindSourcePackage() throws Exception { testFindSourcePackage$(); }
	public void testFindSourcePackage$() throws Exception {
		
		GoPath goPath = new GoPath(TR_SAMPLE_GOPATH_ENTRY.toString());
		
		ArrayList2<GoPackageName> Packages_foo = list(
			goPkg("samplePackage"),
			goPkg("samplePackage/subpack"),
			goPkg("samplePackage/subpack/bar")
		);
		ArrayList2<GoPackageName> Packages_foobar = list(
			goPkg("samplePackage2/xxx")
		);
		
		ArrayList2<GoPackageName> PackagesAll = new ArrayList2<GoPackageName>()
				.addAll2(Packages_foo)
				.addAll2(Packages_foobar);
		
		GoWorkspaceLocation goWorkspace = new GoWorkspaceLocation(TR_SAMPLE_GOPATH_ENTRY);
		
		assertEqualSorted(goWorkspace.findSourcePackages(TR_SAMPLE_GOPATH_ENTRY.resolve_valid("src")), PackagesAll);
		assertEqualSorted(goWorkspace.findSourcePackages(TR_SAMPLE_GOPATH_ENTRY.resolve_valid("..")), list());
		assertEqualSorted(goWorkspace.findSourcePackages(TR_SAMPLE_GOPATH_ENTRY), list());
		assertEqualSorted(goWorkspace.findSubPackages(""), PackagesAll);
		assertEqualSorted(goWorkspace.findSubPackages("."), PackagesAll);
		assertEqualSorted(goWorkspace.findSubPackages("samplePackage"), Packages_foo);
		assertEqualSorted(goWorkspace.findSubPackages("samplePackage/."), Packages_foo);
		assertEqualSorted(goWorkspace.findSubPackages("samplePackage2/."), Packages_foobar);
		
		
		assertEqualSorted(goPath.findGoSourcePackages(TR_SAMPLE_GOPATH_ENTRY), PackagesAll);
		assertEqualSorted(goPath.findGoSourcePackages(TR_SAMPLE_GOPATH_ENTRY.resolve_valid("src")), 
			PackagesAll);
		assertEqualSorted(goPath.findGoSourcePackages(TR_SAMPLE_GOPATH_ENTRY.resolve_valid("src/samplePackage")), 
			Packages_foo);
		assertEqualSorted(goPath.findGoSourcePackages(TR_SAMPLE_GOPATH_ENTRY.resolve_valid("src/samplePackage2")), 
			Packages_foobar);
		
		 // Test no results
		assertAreEqual(goPath.findGoSourcePackages(TR_SAMPLE_GOPATH_ENTRY.resolve_valid("..")), list());
	}
	
	protected <T extends Comparable<T>> void assertEqualSorted(ArrayList2<T> list1, Indexable<?> list2) {
		CollectionUtil.sort(list1);
		assertAreEqual(list1, list2);
	}
	
}