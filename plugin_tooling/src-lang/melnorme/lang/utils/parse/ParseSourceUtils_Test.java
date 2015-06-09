/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.utils.parse;

import melnorme.lang.tests.CommonToolingTest;

import org.junit.Test;

public class ParseSourceUtils_Test extends CommonToolingTest {
	
	@Test
	public void test_consumeDelimited() throws Exception { test_consumeDelimited$(); }
	public void test_consumeDelimited$() throws Exception {
		
		assertEquals(new StringParseSource("blah").consumeDelimitedString('|', '#'), "blah");
		
		StringParseSource lexingHelper = new StringParseSource("one|two|three##|four#|xxx|###|five");
		assertEquals(lexingHelper.consumeDelimitedString('|', '#'), "one");
		assertEquals(lexingHelper.consumeDelimitedString('|', '#'), "two");
		assertEquals(lexingHelper.consumeDelimitedString('|', '#'), "three#");
		assertEquals(lexingHelper.consumeDelimitedString('|', '#'), "four|xxx");
		assertEquals(lexingHelper.consumeDelimitedString('|', '#'), "#|five");
	}
	
}