/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.editor.text;

import org.eclipse.jface.text.Document;
import org.junit.Test;

import melnorme.lang.ide.ui.tests.CommonUITest;
import melnorme.lang.ide.ui.text.completion.LangCompletionProposal;
import melnorme.lang.tooling.CompletionProposalKind;
import melnorme.lang.tooling.ElementAttributes;
import melnorme.lang.tooling.ToolCompletionProposal;

public class LangCompletionProposalTest extends CommonUITest {

	@Test
	public void testApply() throws Exception { testApply$(); }
	public void testApply$() throws Exception {
		// Test a regression in apply
		
		String source = "void foo(); Int blah";
		
		ToolCompletionProposal tcp = new ToolCompletionProposal(source.indexOf("Int"), 3, "interface", 
			"label", CompletionProposalKind.values()[0], new ElementAttributes(null), "moduleName", null);
		LangCompletionProposal completionProposal = new LangCompletionProposal(tcp, null, null);
		
		Document document = new Document(source);
		completionProposal.doApply(document, true);
		
		assertEquals(document.get(), "void foo(); interface blah");
	}
	
}