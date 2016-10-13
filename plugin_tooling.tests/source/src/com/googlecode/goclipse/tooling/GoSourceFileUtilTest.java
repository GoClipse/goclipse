/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.tooling;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.junit.Test;

import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.tests.CommonTest;

public class GoSourceFileUtilTest extends CommonTest {
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		testFindPackageDeclaration("package xxx");
		testFindPackageDeclaration("  package xxx");
		testFindPackageDeclaration("//  package zzzz\n package xxx");
		
		testFindPackageDeclaration("/*  package zzzz\n */  // package zzzz \n package xxx");
		testFindPackageDeclaration("/* ___ *//* ___ *///___\npackage xxx");
		testFindPackageDeclaration("/**/package xxx");
		
		verifyThrows(() -> testFindPackageDeclaration("/*  ___ "), CommonException.class);
		verifyThrows(() -> testFindPackageDeclaration("/**"), CommonException.class);
	}
	
	protected void testFindPackageDeclaration(String source) throws CommonException {
		int pkgNameStart = GoSourceFileUtil.findPackageDeclaration_NameStart(source);
		int expected = source.indexOf("package xxx");
		assertTrue(expected != -1);
		expected += "package ".length();
		assertTrue(pkgNameStart != -1 && pkgNameStart == expected);
	}
	
}