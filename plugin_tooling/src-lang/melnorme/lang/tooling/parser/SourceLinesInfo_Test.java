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
package melnorme.lang.tooling.parser;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import org.junit.Test;

import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.tests.CommonTest;

public class SourceLinesInfo_Test extends CommonTest {
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		
		getSourceLinesInfo("");
		getSourceLinesInfo("\n");
		SourceLinesInfo  sourceLinesInfo = getSourceLinesInfo("12345\n12345");
		
		testOffset(sourceLinesInfo, 1, 0, 0, 1);
		testOffset(sourceLinesInfo, 5, 0, 0, 5);
		testOffset(sourceLinesInfo, 6, 1, 6, 0);
		testOffset(sourceLinesInfo, 7, 1, 6, 1);
		
		verifyThrows(() -> sourceLinesInfo.getValidatedOffset_1(1, 10), null, "Invalid column, out of bounds");
		verifyThrows(() -> sourceLinesInfo.getValidatedOffset_1(2, 10), null, "line+column, out of bounds");
		verifyThrows(() -> sourceLinesInfo.getValidatedOffset_1(3, 1), null, "Invalid line: 3 is over the max bound: 2");
		
		assertEquals(sourceLinesInfo.getValidatedOffset_1(1, 1), 0);
		assertEquals(sourceLinesInfo.getValidatedOffset_1(2, 1), 6);
		assertEquals(sourceLinesInfo.getValidatedOffset_1(2, 6), "12345\n12345".length());
	}
	
	public SourceLinesInfo getSourceLinesInfo(String sampleSource) throws CommonException {
		SourceLinesInfo sourceLinesInfo = new SourceLinesInfo(sampleSource);
		
		// Run common tests
		testOffset(sourceLinesInfo, 0, 0, 0, 0);
		assertEquals(sourceLinesInfo.getLineForOffset(sampleSource.length()), sourceLinesInfo.getNumberOfLines()-1);
		
		int lengthPlusOne = sampleSource.length()+1;
		verifyThrows(() -> sourceLinesInfo.getLineForOffset(lengthPlusOne), null, 
			"Invalid offset " + lengthPlusOne + ", it is out of bounds.");
		
		return sourceLinesInfo;
	}
	
	protected void testOffset(SourceLinesInfo sourceLinesInfo, int offset, 
			int expectedLine, int expectedLineStart, int expectedColumn) {
		try {
			assertEquals(sourceLinesInfo.getLineForOffset(offset), expectedLine);
			assertEquals(sourceLinesInfo.getLineStartForOffset(offset), expectedLineStart);
			assertEquals(sourceLinesInfo.getColumnForOffset(offset), expectedColumn);
		} catch(CommonException e) {
			assertFail();
		}
	}
	
}