/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.tooling.oracle;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.json.JSONException;
import org.junit.Test;

import com.googlecode.goclipse.tooling.CommonGoToolingTest;

import melnorme.lang.tooling.common.SourceLineColumnRange;
import melnorme.lang.tooling.toolchain.ops.FindDefinitionResult;
import melnorme.utilbox.core.CommonException;

public class GuruFindDefinitionOperation_Test extends CommonGoToolingTest {
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		
		testParseResult(fixTestsPaths(getClassResource("oracle_result.var_ref.json")),
			new FindDefinitionResult(loc(fixTestsPaths("D:/devel/tools.Go/go-workspace/src/newmath/sqrt.go")), 
				new SourceLineColumnRange(5, 6), null)
			);
		
		testParseResult(fixTestsPaths(getClassResource("oracle_result.type1_ref.json")),
			new FindDefinitionResult(loc(fixTestsPaths("D:/devel/tools.Go/GoTest/src/other/blah.go")),
				new SourceLineColumnRange(16, 6), null)
			);
		
		testParseResult(getClassResource("oracle_result.type2_def.json"), 
			null);
		
		testParseResult(getClassResource("oracle_result.type3_anon.json"), 
			null);
	}
	
	protected void testParseResult(String toolOutput, FindDefinitionResult expectedResult) throws JSONException,
			CommonException {
		GuruFindDefinitionOperation op = new GuruFindDefinitionOperation("gopath");
		
		try {
			FindDefinitionResult result = op.parseJsonResult(toolOutput);
			assertAreEqual(result, expectedResult);
		} catch(CommonException ce) {
			assertTrue(expectedResult == null);
		}
		
	}
	
}