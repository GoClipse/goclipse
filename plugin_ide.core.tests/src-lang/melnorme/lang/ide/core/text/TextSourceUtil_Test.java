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
package melnorme.lang.ide.core.text;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.junit.Test;

import melnorme.lang.ide.core.tests.CommonCoreTest;

public class TextSourceUtil_Test extends CommonCoreTest {
	
	@Test
	public void testNTimes() throws Exception { testNTimes$(); }
	public void testNTimes$() throws Exception {
		assertEquals(TextSourceUtils.stringNTimes("", 0), "");
		assertEquals(TextSourceUtils.stringNTimes("", 10), "");
		
		assertEquals(TextSourceUtils.stringNTimes("abc", 0), "");
		assertEquals(TextSourceUtils.stringNTimes("abc", 1), "abc");
		assertEquals(TextSourceUtils.stringNTimes("abc", 3), "abcabcabc");
	}
	
	protected static int marker(String source) {
		int indexOf = source.indexOf('#');
		assertTrue(indexOf != -1);
		return indexOf;
	}
	
	@Test
	public void testIndentFunctions() throws Exception { testIndentFunctions$(); }
	public void testIndentFunctions$() throws Exception {
		assertEquals(TextSourceUtils.findLineStartForOffset("", 0), 0);
		test_findLineStartForOffset("#", 0);
		
		test_findLineStartForOffset("abc#\n", 0);
		test_findLineStartForOffset("abc\n#xxx", 4);
		test_findLineStartForOffset("abc\nxxx#", 4);
	}
	
	protected void test_findLineStartForOffset(String source, int expectedOffset) {
		int indexOf = marker(source);
		assertEquals(TextSourceUtils.findLineStartForOffset(source, indexOf), expectedOffset);
	}
	
}