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
package melnorme.utilbox.misc;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertEquals;

import org.junit.Test;

public class StringUtil_Test extends StringUtil {

	@Test
	public void testCommonPrefix() throws Exception { testCommonPrefix$(); }
	public void testCommonPrefix$() throws Exception {
		
		assertEquals(commonPrefix("abc"), "abc");
		assertEquals(commonPrefix(""), "");
		
		assertEquals(commonPrefix("", "abc"), "");
		assertEquals(commonPrefix("abc", ""), "");
		
		assertEquals(commonPrefix("abc", "abc2", "ab"), "ab");
		assertEquals(commonPrefix("abc", "abcxxx", "abcxxx"), "abc");
		
		assertEquals(commonPrefix("", "abcxxx", "abcxxx"), "");
	}
	
}