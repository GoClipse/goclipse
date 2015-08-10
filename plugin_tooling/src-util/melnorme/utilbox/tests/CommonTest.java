/*******************************************************************************
 * Copyright (c) 2013, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.tests;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;

import melnorme.utilbox.misc.SimpleLogger;

/**
 *  Recommended base class for all tests.
 *  It verifies a contract relating to the class name, which is important for test runners.
 */
public class CommonTest extends CommonTestUtils {
	
	public CommonTest() {
		if(isJUnitTest()) {
			String klassName = getClass().getSimpleName();
			// Check proper tests nomenclature:
			// This to make sure the Maven build picks up the same tests as the the Eclipse JUnit launchers
			assertTrue(klassName.isEmpty() ||
				klassName.startsWith("Test") || klassName.endsWith("Test") || klassName.endsWith("Tests"));
		}
	}
	
	public boolean isJUnitTest() {
		return true;
	}
	
	public static PrintStream testsLogger = System.out;
	public static SimpleLogger testsLogVerbose = SimpleLogger.create("verbose");
	
	public static Set<String> executedTests = new HashSet<String>();
	
	@Before
	public void printSeparator() throws Exception {
		String simpleName = getClass().getSimpleName();
		if(!executedTests.contains(simpleName)) {
			testsLogger.println("===============================  "+simpleName+"  ===============================");
			executedTests.add(simpleName);
		}
	}
	
}