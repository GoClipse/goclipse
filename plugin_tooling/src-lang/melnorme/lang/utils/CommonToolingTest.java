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
package melnorme.lang.utils;


import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;

import melnorme.utilbox.misc.SimpleLogger;
import melnorme.utilbox.tests.CommonTestExt;

public class CommonToolingTest extends CommonTestExt {
	
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