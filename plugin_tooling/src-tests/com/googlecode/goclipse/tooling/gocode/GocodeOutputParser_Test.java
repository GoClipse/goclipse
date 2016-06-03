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
package com.googlecode.goclipse.tooling.gocode;

import static melnorme.lang.tooling.CompletionProposalKind.FUNCTION;
import static melnorme.lang.tooling.CompletionProposalKind.INTERFACE;
import static melnorme.lang.tooling.CompletionProposalKind.NATIVE;
import static melnorme.lang.tooling.CompletionProposalKind.PACKAGE;
import static melnorme.lang.tooling.CompletionProposalKind.STRUCT;
import static melnorme.lang.tooling.CompletionProposalKind.TYPE_DECL;
import static melnorme.lang.tooling.CompletionProposalKind.VARIABLE;
import static melnorme.lang.tooling.EProtection.PRIVATE;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.junit.Test;

import melnorme.lang.tooling.CompletionProposalKind;
import melnorme.lang.tooling.EAttributeFlag;
import melnorme.lang.tooling.EProtection;
import melnorme.lang.tooling.ElementAttributes;
import melnorme.lang.tooling.ToolCompletionProposal;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.tests.CommonTestExt;

public class GocodeOutputParser_Test extends CommonTestExt {
	
	protected ElementAttributes attribs(EProtection protection) {
		return new ElementAttributes(protection);
	}
	
	protected SourceRange sr(int offset, int length) {
		return new SourceRange(offset, length);
	}
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		testProposalParse("var,,xxx,,int", 
			proposal("xxx", "xxx", VARIABLE, attribs(PRIVATE), ": int"));
		testProposalParse("var,,Global,,", 
			proposal("Global", "Global", VARIABLE, attribs(null), null));
		
		testProposalParse("const,,xxx,,int", 
			proposal("xxx", "xxx", VARIABLE, new ElementAttributes(PRIVATE, EAttributeFlag.CONST), ": int"));
		
		
		testProposalParse("type,,Foo,,struct", 
			proposal("Foo", "Foo", STRUCT, attribs(null), null));
		testProposalParse("type,,Foo,,interface", 
			proposal("Foo", "Foo", INTERFACE, attribs(null), null));
		
		testProposalParse("type,,Type,,map[string][]string", 
			proposal("Type", "Type", TYPE_DECL, attribs(null), ": map[string][]string"));

		testProposalParse("type,,int16,,built-in", 
			proposal("int16", "int16", NATIVE, attribs(null), null));
		
		testProposalParse("package,,fmt,,", 
			proposal("fmt", "fmt", PACKAGE, attribs(null), null));
		
		testProposalParse("func,,xpto,,func() int", 
			fnProposal("xpto", "xpto()", null, "xpto()", FUNCTION, attribs(PRIVATE), "int"));
		testProposalParse("func,,xpto,,func() (int, string)", 
			fnProposal("xpto", "xpto()", null, "xpto()", FUNCTION, attribs(PRIVATE), "(int, string)"));
		testProposalParse("func,,ApiFunc,,func()", 
			fnProposal("ApiFunc", "ApiFunc()", null, "ApiFunc()", FUNCTION, attribs(null), ""));
		// Incorrectly formated function entry
		testProposalParse("func,,xpto,,func", 
			fnProposal("xpto", "xpto", null, "xpto", FUNCTION, attribs(PRIVATE), null));
		
		// Test function parameters
		testProposalParse("func,,ApiFunc,,func(writer io.Writer)", 
			fnProposal("ApiFunc", "ApiFunc(writer)", list(sr(8, 6)), 
				"ApiFunc(writer io.Writer)", FUNCTION, 
				attribs(null), ""));
		testProposalParse("func,,ApiFunc,,func(writer io.Writer, a ...interface{})", 
			fnProposal("ApiFunc", "ApiFunc(writer, a)", list(sr(8, 6), sr(8+6+2, 1)), 
				"ApiFunc(writer io.Writer, a ...interface{})", FUNCTION, 
				attribs(null), ""));
		
		testProposalParse("func,,ApiFunc,,func(writer, a ...interface{})", 
			fnProposal("ApiFunc", "ApiFunc(writer, a)", list(sr(8, 6), sr(8+6+2, 1)), 
				"ApiFunc(writer, a ...interface{})", FUNCTION, 
				attribs(null), ""));
		
	}
	
	protected ToolCompletionProposal proposal(String replaceString, String label, CompletionProposalKind kind, 
			ElementAttributes attribs, String typeLabel) {
		return new ToolCompletionProposal(10-6, 6, replaceString, label, kind, attribs, typeLabel, null, null);
	}
	
	protected ToolCompletionProposal fnProposal(
			String simpleReplaceString, String fullReplaceString, Indexable<SourceRange> sourceSubElements, 
			String label, 
			CompletionProposalKind kind, ElementAttributes attribs, String typeLabel) {
		return new ToolCompletionProposal(10-6, 6, simpleReplaceString, label, kind, attribs, typeLabel, null, null,
			fullReplaceString, sourceSubElements);
	}
	
	protected void testProposalParse(String gocodeResultLine, ToolCompletionProposal expectedProposal) 
			throws CommonException {
		GocodeOutputParser2 outputParser = new GocodeOutputParser2(10, "    prefix") {
			@Override
			protected void logWarning(String message) {
				assertFail();
			}
		};
		ToolCompletionProposal completion = outputParser.parseCompletion(gocodeResultLine);
		
		// These 3 checks are redundant, they are just a shortcut for interactive debugging: 
		assertAreEqual(completion.getTypeLabel(), expectedProposal.getTypeLabel());
		assertAreEqual(completion.getFullReplaceString(), expectedProposal.getFullReplaceString());
		assertAreEqual(completion.getSourceSubElements(), expectedProposal.getSourceSubElements());
		
		
		assertTrue(completion.equals(expectedProposal));
	}
	
}