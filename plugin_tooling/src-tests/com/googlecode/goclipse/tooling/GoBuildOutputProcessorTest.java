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
package com.googlecode.goclipse.tooling;

import static melnorme.lang.tests.LangToolingTestResources.getTestResourcePath;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.CoreUtil.listFrom;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import melnorme.lang.tooling.common.SourceLineColumnRange;
import melnorme.lang.tooling.common.ToolSourceMessage;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.status.Severity;


public class GoBuildOutputProcessorTest extends CommonGoToolingTest {
	
	protected static final Path BUILD_OUTPUT_TestResources = getTestResourcePath("buildOutput");
	
	protected static ToolSourceMessage warning(Path path, int line, int column, String errorMessage) {
		return new ToolSourceMessage(path, new SourceLineColumnRange(line, column), Severity.WARNING, errorMessage);
	}
	
	protected static ToolSourceMessage error(Path path, int line, int column, String errorMessage) {
		return new ToolSourceMessage(path, new SourceLineColumnRange(line, column), Severity.ERROR, errorMessage);
	}
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		runTest();
		
		runInvalidSyntaxTest();
	}
	
	protected void runTest() throws CommonException {
		GoBuildOutputProcessor buildProcessor = new GoBuildOutputProcessor() {
			@Override
			protected void handleParseError(CommonException ce) {
				assertFail();
			}
		};
		
		testParseError(buildProcessor, "", listFrom());  // Empty
		
		// Test that this line is ignored without reporting a syntax error.
		testParseError(buildProcessor, "asdfsdaf/asdfsd", listFrom()); 
		
		
		testParseError(buildProcessor,
			readTemplatedFile(BUILD_OUTPUT_TestResources.resolve("outputA.txt")),
			
			listFrom(
				error(path("MyGoLibFoo/libfoo/blah.go"), 7, -1, "undefined: asdfsd"),
				error(path("MyGoLibFoo/libfoo/blah.go"), 10, -1, "not enough arguments in call to fmt.Printf"),
				error(path("MyGoLibFoo/foo.go"), 3, -1, "undefined: ziggy"),
				error(TR_SAMPLE_GOPATH_ENTRY.resolve_valid("src/samplePackage/foo.go").path, 5, -1, "undefined: ziggy2")
			));
		
		
		String OUTPUT_B = readTemplatedFile(BUILD_OUTPUT_TestResources.resolve("outputB.txt"));
		
		String errorMessage1 = findMatch(OUTPUT_B, "cannot find package \"xxx.*\\n.*\\n.*", 0).replace("\r", "");
		String errorMessage2 = findMatch(OUTPUT_B, "cannot find package \"yyy.*\\n.*\\n.*", 0).replace("\r", "");
		String errorMessage3 = findMatch(OUTPUT_B, "cannot find package \"zzz.*\\n.*\\n.*", 0).replace("\r", "");
		
		
		List<ToolSourceMessage> OUTPUTB_Errors = listFrom(
			error(path("libbar/blah.go"), 3, 8, errorMessage1),
			error(path("../MyGoLibFoo/libfoo/blah.go"), 3, 8, errorMessage2),
			error(TR_SAMPLE_GOPATH_ENTRY.resolve_valid("src/samplePackage/foo.go").path, 3, 2, errorMessage3)
		);
		testParseError(buildProcessor,
			OUTPUT_B,
			OUTPUTB_Errors);
		
		// Test gometalinter format
		testParseError(buildProcessor, 
			"path/file:3:: <message> (<linter>)"+"\n"+
			"path/file:3:10: <message> (<linter>)"+"\n"+
			"path/file:4:11:warning: This is a warning!"
			
			, 
			listFrom(
				error(path("path/file"), 3, -1, "<message> (<linter>)"),
				error(path("path/file"), 3, 10, "<message> (<linter>)"),
				warning(path("path/file"), 4, 11, "This is a warning!")
			)
		); 
		
	}
	
	protected void testParseError(GoBuildOutputProcessor buildProcessor, String stderr, List<?> expected) 
			throws CommonException {
		ArrayList<ToolSourceMessage> buildMessages = buildProcessor.parseMessages(stderr);
		assertEquals(buildMessages, expected);
	}
	
	protected String readTemplatedFile(Path filePath) {
		String fileContents = readStringFromFile(filePath);
		return fileContents.replaceAll(
			Pattern.quote("$$TESTRESOURCE_SAMPLE_GOPATH_ENTRY$$"), 
			Matcher.quoteReplacement(TR_SAMPLE_GOPATH_ENTRY.toString())
		);
	}
	
	protected void runInvalidSyntaxTest() throws CommonException {
		GoBuildOutputProcessor buildProcessor = new GoBuildOutputProcessor() {
			@Override
			protected void handleParseError(CommonException ce) {
			}
		};
		
		buildProcessor.parseMessages("libbar\blah.go:");
	}
	
}