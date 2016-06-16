/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
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

import org.junit.Test;

import com.googlecode.goclipse.tooling.CommonGoToolingTest;

public class GoDocParser_Test extends CommonGoToolingTest {
	
	public static String SAMPLE_SRC_A = getClassResourceAsString(GoDocParser_Test.class, "godoc_sample_A.go");
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		
		assertAreEqual(new GoDocParser().parseDocForDefinitionAt("", 0), null);
		assertAreEqual(new GoDocParser().parseDocForDefinitionAt("blah", 0), null);
		assertAreEqual(new GoDocParser().parseDocForDefinitionAt("blah", 4), null);
		
		// TODO: throw exception
		assertAreEqual(new GoDocParser().parseDocForDefinitionAt("", 4), null);
		
		testParseGoDoc(SAMPLE_SRC_A, "Op string", null);
		testParseGoDoc(SAMPLE_SRC_A, "// D1", null);
		
		
		testParseGoDoc(SAMPLE_SRC_A, "url", "D1D2");
		testParseGoDoc(SAMPLE_SRC_A, "Error", "E1");
		testParseGoDoc(SAMPLE_SRC_A, "struct", "E1");
		testParseGoDoc(SAMPLE_SRC_A, "empty_doc", "");
		testParseGoDoc(SAMPLE_SRC_A, "\nMARKER_A", "D1D2");
		testParseGoDoc(SAMPLE_SRC_A, "\n// D1", "A B C");
		
		testParseGoDoc("\n___", "___", null);
		
		testParseGoDoc(SAMPLE_SRC_A, "test_indent", "indentA indentB");
		testParseGoDoc(SAMPLE_SRC_A, "test_indent2", "indent2");
		testParseGoDoc(SAMPLE_SRC_A, "test_indent3", "indent3");
	}
	
	protected void testParseGoDoc(String source, String indexMarker, String expected) {
		int offset = getIndexOf(source, indexMarker);
		
		assertAreEqual(new GoDocParser().parseDocForDefinitionAt(source, offset), expected);
		// test with '\n' only
		String source2 = source.replace("\r\n", "\n");
		offset = getIndexOf(source2, indexMarker);
		assertAreEqual(new GoDocParser().parseDocForDefinitionAt(source2, offset), expected);
	}
	
	public static int getIndexOf(String string, String other) {
		int offset = string.indexOf(other);
		assertTrue(offset != -1);
		return offset;
	}
	
}