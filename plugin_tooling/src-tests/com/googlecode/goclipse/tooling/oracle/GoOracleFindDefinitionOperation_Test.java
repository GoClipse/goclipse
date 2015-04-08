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
import melnorme.lang.tooling.ops.FindDefinitionResult;
import melnorme.lang.tooling.ops.SourceLineColumnRange;
import melnorme.utilbox.core.CommonException;

import org.json.JSONException;
import org.junit.Test;

import com.googlecode.goclipse.tooling.CommonGoToolingTest;

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
		
		testParseResult(getClassResourceAsString("oracle_result.var_ref.json"), 
			new SourceLineColumnRange(path("D:\\go-workspace\\src\\github.com\\user\\newmath\\sqrt.go"), 5, 6));
		
		testParseResult(getClassResourceAsString("oracle_result.type1_ref.json"), 
			new SourceLineColumnRange(path("D:\\devel\\DDT\\_runtime\\GoTest\\src\\other\\blah.go"), 16, 6));
		
		testParseResult(getClassResourceAsString("oracle_result.type2_def.json"), 
			null);
		
		testParseResult(getClassResourceAsString("oracle_result.type3_anon.json"), 
			null);
	}
	
	protected void testParseResult(String toolOutput, SourceLineColumnRange expectedRange) throws JSONException,
			CommonException{
		GoOracleFindDefinitionOperation op = new GoOracleFindDefinitionOperation("gopath");
		
		FindDefinitionResult result = op.parseJsonResult(toolOutput);
		
		assertAreEqual(result.getLocation(), expectedRange);
		assertEquals(result.getErrorMessage() == null, expectedRange != null);
	}
	
}