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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.Optional;

import org.eclipse.jface.text.Document;
import org.junit.Test;

import melnorme.lang.ide.ui.tests.CommonUITest;
import melnorme.lang.ide.ui.text.completion.LangCompletionProposal;
import melnorme.lang.tooling.CompletionProposalKind;
import melnorme.lang.tooling.ElementAttributes;
import melnorme.lang.tooling.ToolCompletionProposal;
import melnorme.lang.tooling.common.ISourceBuffer;
import melnorme.lang.tooling.toolchain.ops.SourceOpContext;
import melnorme.utilbox.misc.Location;

public class LangCompletionProposalTest extends CommonUITest {
	
	public static class TestsSourceBuffer implements ISourceBuffer {
		
		protected final String source;
		
		public TestsSourceBuffer(String source) {
			this.source = assertNotNull(source);
		}
		
		@Override
		public Location getLocation_orNull() {
			return null;
		}
		
		@Override
		public String getSource() {
			return source;
		}
		
		@Override
		public boolean isDirty() {
			return false;
		}
		
		@Override
		public boolean doTrySaveBuffer() {
			return false;
		}
		
		@Override
		public ISourceBuffer getReadOnlyView() {
			return this;
		}
		
	}
	
	/* -----------------  ----------------- */
	
	@Test
	public void testApply() throws Exception { testApply$(); }
	public void testApply$() throws Exception {
		// Test a regression in apply
		
		String source = "void foo(); Int blah";
		
		int offset = source.indexOf("Int");
		ToolCompletionProposal tcp = new ToolCompletionProposal(offset, 3, "interface", 
			"label", CompletionProposalKind.values()[0], new ElementAttributes(null), null, "moduleName", null);
		SourceOpContext sourceOpContext = new SourceOpContext(Optional.empty(), offset, source, true);
		LangCompletionProposal completionProposal = new LangCompletionProposal(sourceOpContext, tcp, null, null);
		
		
		Document document = new Document(source);
		completionProposal.doApply(document, true);
		
		assertEquals(document.get(), "void foo(); interface blah");
	}
	
}