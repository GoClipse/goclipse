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

import org.junit.Test;

import com.googlecode.goclipse.tooling.CommonGoToolingTest;
import com.googlecode.goclipse.tooling.oracle.GoOracleFindDefinitionOperation.GoOracleFindDefinitionResult;

public class GoOracleFindDefinitionOperation_Test extends CommonGoToolingTest {
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		
		GoOracleFindDefinitionOperation op = new GoOracleFindDefinitionOperation("gopath");
		
		GoOracleFindDefinitionResult result = op.parseJsonResult(
			getClassResourceAsString(GoOracleFindDefinitionOperation_Test.class, "result1.json"));
		
		assertEquals(result.path, path("D:\\devel\\tools.Go\\go-workspace\\src\\github.com\\user\\newmath\\sqrt.go"));
		assertEquals(result.line, 5);
		assertEquals(result.column, 6);
	}
	
}
