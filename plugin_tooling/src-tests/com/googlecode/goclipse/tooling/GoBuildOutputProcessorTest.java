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
package com.googlecode.goclipse.tooling;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.CoreUtil.listFrom;

import java.nio.file.Path;
import java.util.List;

import melnorme.lang.tests.LangToolingTestResources;
import melnorme.lang.tooling.ops.ToolSourceError;
import melnorme.lang.tooling.ops.SourceLineColumnLocation;
import melnorme.utilbox.core.CommonException;

import org.junit.Test;


public class GoBuildOutputProcessorTest extends CommonGoToolingTest {
	
	protected static final Path BUILD_OUTPUT_TestResources = LangToolingTestResources.getTestResourcePath("buildOutput");
	
	protected static ToolSourceError error(Path path, int line, int column, String errorMessage) {
		return new ToolSourceError(new SourceLineColumnLocation(path, line, column), errorMessage);
	}
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		Path basePath = path("ProjectPath");
		GoBuildOutputProcessor buildProcessor = new GoBuildOutputProcessor(basePath) {
			@Override
			protected void handleParseError(CommonException ce) {
				assertFail();
			}
		};
		
		buildProcessor.parseErrors("");
		assertEquals(buildProcessor.getBuildErrors(), listFrom()); // Empty

		List<ToolSourceError> OUTPUTA_Errors = listFrom(
			error(basePath.resolve("MyGoLibFoo/libfoo/blah.go"), 7, 0, "undefined: asdfsd"),
			error(basePath.resolve("MyGoLibFoo/libfoo/blah.go"), 10, 0, "not enough arguments in call to fmt.Printf")
		);
		buildProcessor.parseErrors(readStringFromFile(BUILD_OUTPUT_TestResources.resolve("outputA.txt")));
		assertEquals(buildProcessor.getBuildErrors(), OUTPUTA_Errors);
		
		
		String OUTPUT_B = readStringFromFile(BUILD_OUTPUT_TestResources.resolve("outputB.txt"));
		
		String errorMessage1 = findMatch(OUTPUT_B, "cannot find package \"xxx.*\\n.*\\n.*", 0).replace("\r", "");
		String errorMessage2 = findMatch(OUTPUT_B, "cannot find package \"zzz.*\\n.*\\n.*", 0).replace("\r", "");
		
		
		List<ToolSourceError> OUTPUTB_Errors = listFrom(
			error(normResolve(basePath, "libbar/blah.go"), 3, 8, errorMessage1),
			error(normResolve(basePath, "../MyGoLibFoo/libfoo/blah.go"), 3, 8, errorMessage2)
		);
		buildProcessor.parseErrors(OUTPUT_B);
		assertEquals(buildProcessor.getBuildErrors(), OUTPUTB_Errors);
	}
	
	protected Path normResolve(Path basePath, String other) {
		return basePath.resolve(other).normalize();
	}
	
}