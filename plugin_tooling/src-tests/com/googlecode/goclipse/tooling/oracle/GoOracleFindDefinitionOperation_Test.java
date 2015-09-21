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
package com.googlecode.goclipse.tooling.oracle;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.json.JSONException;
import org.junit.Test;

import com.googlecode.goclipse.tooling.CommonGoToolingTest;

import melnorme.lang.tooling.ops.FindDefinitionResult;
import melnorme.lang.tooling.ops.SourceLineColumnRange;
import melnorme.utilbox.core.CommonException;

public class GoOracleFindDefinitionOperation_Test extends CommonGoToolingTest {
	
	@Test
	public void testCreateProcessBuilder() throws Exception { testCreateProcessBuilder$(); }
	public void testCreateProcessBuilder$() throws Exception {
		GoOracleFindDefinitionOperation op = new GoOracleFindDefinitionOperation("gopath");
		
		ProcessBuilder pb;
		pb = op.createProcessBuilder(SAMPLE_GOEnv_1, SAMPLE_GOPATH_Entry.resolve("src/foobar/file.go"), 0);
		assertEquals(pb.command().get(4), "foobar");
		
		try {
			op.createProcessBuilder(SAMPLE_GOEnv_1, SAMPLE_GOPATH_Entry.resolve("not_on_src/foobar/file.go"), 0);
			assertFail();
		} catch (CommonException se) {
			assertTrue(se.getMessage().contains("file not in the Go environment"));
		}
	}
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		
		testParseResult(fixTestsPaths(getClassResourceAsString("oracle_result.var_ref.json")),
			new FindDefinitionResult(loc(fixTestsPaths("D:/devel/tools.Go/go-workspace/src/newmath/sqrt.go")), 
				new SourceLineColumnRange(5, 6), null)
			);
		
		testParseResult(fixTestsPaths(getClassResourceAsString("oracle_result.type1_ref.json")),
			new FindDefinitionResult(loc(fixTestsPaths("D:/devel/tools.Go/GoTest/src/other/blah.go")),
				new SourceLineColumnRange(16, 6), null)
			);
		
		testParseResult(getClassResourceAsString("oracle_result.type2_def.json"), 
			null);
		
		testParseResult(getClassResourceAsString("oracle_result.type3_anon.json"), 
			null);
	}
	
	protected void testParseResult(String toolOutput, FindDefinitionResult expectedResult) throws JSONException,
			CommonException {
		GoOracleFindDefinitionOperation op = new GoOracleFindDefinitionOperation("gopath");
		
		try {
			FindDefinitionResult result = op.parseJsonResult(toolOutput);
			assertAreEqual(result, expectedResult);
		} catch(CommonException ce) {
			assertTrue(expectedResult == null);
		}
		
	}
	
}