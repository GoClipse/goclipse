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
		SourceLinesInfo sourceLinesInfo = new SourceLinesInfo("abcdef");
		
		verifyThrows(() -> sourceLinesInfo.getValidatedOffset(1, 10), null, "line+column, out of bounds");
	}
	
}