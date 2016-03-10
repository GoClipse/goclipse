/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.ops.util;

import org.junit.Test;

import melnorme.utilbox.tests.CommonTest;

public class SourceLinesInfo_Test extends CommonTest {
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		String sampleSource = "12345\n12345";
		SourceLinesInfo sourceLinesInfo = new SourceLinesInfo(sampleSource);
		
		verifyThrows(() -> sourceLinesInfo.getValidatedOffset_1(1, 10), null, "Invalid column, out of bounds");
		verifyThrows(() -> sourceLinesInfo.getValidatedOffset_1(2, 10), null, "line+column, out of bounds");
		verifyThrows(() -> sourceLinesInfo.getValidatedOffset_1(3, 1), null, "Invalid line: 3 is over the max bound: 3");
		
		assertEquals(sourceLinesInfo.getValidatedOffset_1(1, 1), 0);
		assertEquals(sourceLinesInfo.getValidatedOffset_1(2, 1), 6);
		assertEquals(sourceLinesInfo.getValidatedOffset_1(2, 6), sampleSource.length());
	}
	
}