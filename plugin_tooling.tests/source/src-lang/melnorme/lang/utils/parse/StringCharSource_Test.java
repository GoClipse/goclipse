/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.utils.parse;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import melnorme.utilbox.tests.CommonTest;

public class StringCharSource_Test extends CommonTest {
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {

		testStringCS("abcdef", 3, "abc");
		testStringCS("abcdef", 6, "abcdef"); // boundary
		testStringCS("abcdef", 10, "abcdef"); // read more than available
		
		testStringCS("", 0, null);
		testStringCS("", 1, null);
		
	}
	
	protected StringCharSource testStringCS(String source, int readLen, String expected) throws IOException {
		char[] cbuf = new char[readLen];
		StringCharSource cs = new StringCharSource(source);
		
		int actualReadLen = cs.toReader().read(cbuf);
		
		if(actualReadLen == -1) {
			assertTrue(expected == null);
		} else {
			assertTrue(Arrays.equals(
				Arrays.copyOf(cbuf, actualReadLen), 
				expected.toCharArray())
			);
		}
		
		return cs;
	}
	
}