/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.text.format;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.swt.SWT;
import org.junit.Test;

import melnorme.lang.ide.core.text.BlockHeuristicsScannner;
import melnorme.lang.ide.core.text.SamplePartitionScanner;
import melnorme.lang.ide.core.text.Scanner_BaseTest;
import melnorme.lang.ide.core.text.format.FormatterIndentMode;
import melnorme.lang.ide.core.text.format.ILangAutoEditsPreferencesAccess;
import melnorme.lang.ide.core.text.format.LangAutoEditStrategy;
import melnorme.lang.ide.core.text.format.LangAutoEditUtils;
import melnorme.lang.ide.core.text.format.LangAutoEditStrategyExt.ILangAutoEditsPreferencesAccessExt;
import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.misc.StringUtil;

public class LangAutoEditStrategyTest extends Scanner_BaseTest {
	
	public static class Mock_LangAutoEditsPreferencesAccess implements ILangAutoEditsPreferencesAccessExt {
		@Override
		public boolean isSmartIndent() {
			return true;
		}
		
		@Override
		public boolean isSmartDeIndent() {
			return true;
		}
		
		@Override
		public boolean closeBraces() {
			return true;
		}
		
		@Override
		public boolean closeBlocks() {
			return true;
		}
		
		@Override
		public boolean isSmartPaste() {
			return true;
		}
		
		@Override
		public FormatterIndentMode getTabStyle() {
			return FormatterIndentMode.TAB;
		}
		
		@Override
		public int getIndentSize() {
			return 4;
		}
		
		@Override
		public boolean parenthesesAsBlocks() {
			return true;
		}
	}
	
	public static final String NEUTRAL_SRCX; 
	
	static {
		NEUTRAL_SRCX = MiscUtil.getClassResource(LangAutoEditStrategyTest.class, "sample_block_code");
	}
	
	public static final String PENDING_WS1 = "  "; 
	public static final String PENDING_WS2 = "\t ";
	public static final String PENDING_TXT = "\tpending";
	
	protected LangAutoEditStrategy autoEditStrategy;
	
	protected LangAutoEditStrategy getAutoEditStrategy() {
		if(autoEditStrategy == null) {
			
			ILangAutoEditsPreferencesAccess preferences = new Mock_LangAutoEditsPreferencesAccess();
			
			autoEditStrategy = new LangAutoEditStrategy(null, preferences) {
				@Override
				protected BlockHeuristicsScannner createBlockHeuristicsScanner(IDocument doc) {
					return Scanner_BaseTest.createBlockHeuristicScannerWithSamplePartitioning(doc);
				};
			};
		}
		return autoEditStrategy;
	}
	
	protected DocumentCommand createDocumentCommand(int start, int length, String text) {
		DocumentCommand documentCommand = new DocumentCommand() {};
		documentCommand.doit = true;
		documentCommand.text = text;
		
		documentCommand.offset = start;
		documentCommand.length = length;
		
		documentCommand.owner = null;
		documentCommand.caretOffset = -1;
		documentCommand.shiftsCaret = true;
		return documentCommand;
	}
	
	
	protected void testEnterEdit(String textBeforeCursor, String expectIndent) {
		testEnterEdit(textBeforeCursor, "", expectIndent);
	}
	
	protected void testEnterEdit(String textBeforeCursor, String textAfterCursor, String expectIndent) {
		int indent = 0;
		
		while(indent < 3) {
			if(textAfterCursor == null) {
				if(indent == 0) {
					textAfterCursor = "";
				}
				if(indent == 1) {
					textAfterCursor += NL + ")}" + NEUTRAL_SRC1; // This source after should have no effect
				}
				if(indent == 2) {
					textAfterCursor += "blah)}]"; // This source after should have no effect
				}
			}
			String[] before_Lines = textBeforeCursor.split("\\\\n");
			String textBeforeCursor_mod = StringUtil.collToString(before_Lines, TABn(indent));
			
			testEnterAutoEdit(textBeforeCursor_mod, textAfterCursor, expectIndent);
			
			indent++;
		}
	}
	
	protected void testEnterAutoEdit(String sourceBefore, String sourceAfter, String expectedEdit) {
		testEnterAutoEdit_____(sourceBefore, sourceAfter, NL+expectedEdit, -1);
	}
	
	protected void testEnterAutoEdit(String textBefore, String textAfter, String expInsert, String expInsertAfter) {
		expInsert = NL + expInsert;
		testEnterAutoEdit_____(textBefore, textAfter, expInsert+expInsertAfter, expInsert.length());
	}
	
	protected void testEnterAutoEdit_____(String textBefore, String textAfter, String expectedInsert, int offsetDelta) {
		Document document = setupDocument(textBefore, textAfter);
		int keypressOffset = textBefore.length();
		DocumentCommand docCommand = createDocumentCommand(keypressOffset, 0, NL);
		getAutoEditStrategy().customizeDocumentCommand(document, docCommand);
		int caretOffset = (offsetDelta == -1) ? -1 : textBefore.length() + offsetDelta;
		int replaceLength = 0;
		checkCommand(docCommand, expectedInsert, keypressOffset, replaceLength, caretOffset);
	}
	
	protected Document setupDocument(String textBefore, String textAfter) {
		Document document = getDocument();
		document.set(textBefore + textAfter);
		return document;
	}
	
	protected void checkCommand(DocumentCommand documentCommand, String text, int offset, int length) {
		checkCommand(documentCommand, text, offset, length, -1);
	}
	
	protected void checkCommand(DocumentCommand documentCommand, String text, int offset, int length, 
			int caretOffset) {
		assertEquals(documentCommand.text, text);
		assertTrue(documentCommand.offset == offset);
		assertTrue(documentCommand.length == length);
		assertTrue(documentCommand.caretOffset == caretOffset);
		assertTrue(documentCommand.shiftsCaret == (caretOffset == -1));
	}
	
	
	protected static String TABn(int indent) {
		return LangAutoEditUtils.stringNTimes(TAB, indent);
	}
	
	protected static String expectInd(String indent) {
		return indent;
	}
	
	protected static String expectInd(String nl, int indent) {
		return nl+TABn(indent);
	}
	
	protected static String expectClose(int indent, String close) {
		return NL+TABn(indent-1)+close;
	}
	
	
	@Test
	public void testSmartIndent() throws Exception { testSmartIndent$(); }
	public void testSmartIndent$() throws Exception {
		
		testEnterEdit("void main(){}", "blah", ""); // balance 0 : 0
		
		testEnterEdit("void main(){", NL+"}", TAB); // balance 0 : 1 (closed)
		testEnterEdit("void main(){", "}", TAB);
		
		
		String dNL = getDocument().getDefaultLineDelimiter();
		// balance 0 : 1(unclosed)
		testEnterAutoEdit("void main{", ""                             , TAB, dNL+"}");
		testEnterAutoEdit("void main(",      NL+"func(){}"+NL+"blah();", TAB,  NL+")");
		testEnterAutoEdit("vo() main{", "  "+NL+"func(){}"+NL+"blah();", TAB,  NL+"}");
		// balance 0 : 1(unclosed but don't close due to pending text)
		testEnterEdit("void main(){", "func(){}"+NL+"blah();", TAB);
		
		String s;
		
		s = line("func{")+
			TAB+"abc}"; // balance -1 : 0
		testEnterEdit(s, "}"+NEUTRAL_SRC1, "");

		s = line("func{{")+
			TAB+"abc}}"; // balance -2 : 0
		testEnterEdit(s, "}"+NEUTRAL_SRC1, "");
		
		s = line(TAB+"func((")+
			TAB+"abc))"; // balance -2 : 0	 '('
		testEnterEdit(s, NEUTRAL_SRC1+")", TAB);
		
		testEnterEdit("func("+NL + "\tblah", 
			"\t");
		
		testEnterEdit("func("+NL + "abc{", 
			"}", TABn(1)); // test 0 : 1
		
		testEnterEdit("func{"+NL + "}abc{", 
			PENDING_WS1+NL+"}", TABn(1)); // test -1 : 1

		
		testEnterEdit("func{"+NL + "\t\t ", // test all WhiteSpace 
			NL +")", "\t\t ");
		// test all WhiteSpace with pending WhiteSpace   
		testEnterEdit(line("func{") + "\t\t ", 
			PENDING_WS2+NL+")", "\t\t ");

		
		s = line("func{")+
			"}blah("; // test another -1 : 1   
		testEnterEdit(s, NL +")", TABn(1));
		
		
		s = line("func({")+
			"abc(";     // test potential close (go outside dominating block?)
		testEnterEdit(s, NL+")"+ NEUTRAL_SRC1+"}", TABn(1));
		
		s = line("func{")+
			"abc(";     // test potential close (unclosed dominating block)
		testEnterEdit(s, NL+")", TABn(1));
		
		s = line("func{")+
			"abc(";     // test potential close (pending text)
		testEnterEdit(s, PENDING_TXT+NL+")", TABn(1));
		
		
		s = line("func{")+
			"}blah(";   // test close, -1 : 1, right=(_
		testEnterAutoEdit(s, NL+NEUTRAL_SRC1, TABn(1), expectClose(0+1, ")"));
		
		s = line("func{")+
			"}blah{{";  // test close, -1 : 2, right={{_
		testEnterAutoEdit(s, PENDING_WS2+NL, TABn(0+2), expectClose(0+2, "}"));
		
		s = line("func{")+
			"}}blah{";  // test close, -2 : 1, right={_..
		testEnterAutoEdit(s, NL+NEUTRAL_SRC1, TABn(1), expectClose(0+1, "}"));
		
		s = line("func{")+
			"}}blah{{"; // test close, -2 : 1, right= {{_..}     
		testEnterAutoEdit(s, NL+NEUTRAL_SRC1+"}", TABn(0+2), expectClose(0+2, "}"));
		
		s = line("}}blah{")+
			line("{func{")+
			line(TABn(2) + NEUTRAL_SRC1)+
			"}blah{"; // test close, -2 : 1, right=}} {{..}{_..}     
		testEnterAutoEdit(s, NL+NEUTRAL_SRC1+line("}"), TABn(1), expectClose(0+1, "}"));
		
		s = line("func{{{")+
			TAB+"abc}}}"; // test -3 : 0
		testEnterEdit(s, NL+NEUTRAL_SRCX, TABn(0));

		s = line(TABn(7) + "func({{{")+  // start block still has : 2 open block
			TAB+"abc}}"; // test -2 : 0
		testEnterEdit(s, NL+NEUTRAL_SRCX, TABn(0+7+2));

		s = line("func")+
			TAB+"abc}}}"; // test -3 : 0 with zero indent
		testEnterEdit(s, NL+NEUTRAL_SRCX, TABn(0));
		
		s = line("func")+
			"abc}}}";     // test -3 : 0 with zero indent
		testEnterEdit(s, NL+NEUTRAL_SRCX, TABn(0));

		
		s = line("func{{{")+
			line(TABn(4) + "func{()}")+ // test interim lines with irregular ident
			TAB+"abc}}";  // -2 : 0
		testEnterEdit(s, NL+NEUTRAL_SRC1, TABn(1)); 
		
		s = line(TABn(2) +NEUTRAL_SRC1)+ // more lines
			line(TABn(2) +"}}func{{{")+  // matching start block is -2 : 3
			line(TABn(2) +NEUTRAL_SRC1)+ // more lines
			line(TABn(2-2) + "func{()}")+ // interim lines with irregular ident (negative)
			TABn(2) + TAB+"abc(blah{}) blah}}"; // -2 : 0
		testEnterEdit(s, NL+NEUTRAL_SRCX, TABn(2+1));
		
		
		s = line("func{")+
			""; // test empty line   
		testEnterEdit(s, "){"+NL+")", TABn(0));
		
	}
	
	@Test
	public void testSmartIdent_Boundary() throws Exception { testSmartIdent_Boundary$(); }
	public void testSmartIdent_Boundary$() throws Exception {
		String s;
		s = "(";     // test potential close
		testEnterEdit(s, NL +")"+ NEUTRAL_SRC1+"}", TABn(1));

		s = "}(";     // test potential close
		testEnterEdit(s, NL +")"+ NEUTRAL_SRC1+"}", TABn(1));
		
		s = "{"+NL+"(";     // test potential close
		testEnterEdit(s, NL +"){", TABn(1));
	}
	
	@Test
	public void testSmartIndent_xPartitioning() throws Exception { testSmartIndent_xPartitioning$(); }
	public void testSmartIndent_xPartitioning$() throws Exception {
		assertContains(getDocument().getPartitionings(), SamplePartitionScanner.LANG_PARTITIONING);
		
		int indent = 1; // Needs to be greater than 1
		String s;
		
		s = line(TABn(indent) + "func()")+
			"//"+TAB+"abc"; // test with line comment
		testEnterEdit(s, NL +"})"+ NEUTRAL_SRC1, TABn(indent));
		
		s = line(TABn(indent) + "func(")+
			"//"+TAB+"abc)"; // test with line comment, with +1 indent
		testEnterEdit(s, NL +")"+ NEUTRAL_SRC1, TABn(indent+1));
		
		s = line(TABn(indent) + "func{")+
			line(TABn(indent+1) + "blah}")+
			"//"+TAB+"abc)"; // test with line comment, with -1 indent
		testEnterEdit(s, NL +")"+ NEUTRAL_SRC1, TABn(indent));
		
		s = line(TABn(indent) + "{func(")+
			"//"+TAB+"abc"; // test with line comment, characters after
		testEnterEdit(s, ")"+NL+ NEUTRAL_SRC1, TABn(indent+2));
		
		
		s = line(TABn(indent) + "func({")+
			"/**/"; 			// test with block comment, with +2 indent Close
		testEnterAutoEdit(s, NL +"blah"+ NEUTRAL_SRC1, TABn(indent+2), expectClose(indent+2, "}"));
		
		s = line(TABn(indent) + "func(((")+
			TABn(indent) + "// blah"; 		// test line comment with whitespace before, (This-Line)
		testEnterEdit(s, NL +"}}}"+ NEUTRAL_SRC1, TABn(indent));
		s = line(TABn(indent) + "func(((")+
			TABn(indent) + "/**/"; 		// test block comment with whitespace before, (This-Line)
		testEnterEdit(s, NL +"}}}"+ NEUTRAL_SRC1, TABn(indent));

		s = line(TABn(indent) + "func(")+
			"/* */"+TAB+"abc{{{"; // test block comment with characters after, (This-Line)
		testEnterEdit(s, NL +"}}})"+ NEUTRAL_SRC1, TABn(0+3));
		
		
		s = "//abc{"; 		// test line comment, no valid line before
		testEnterEdit(s, NL +"})"+ NEUTRAL_SRC1, TABn(0));
		s = "/*abc{*/"; 		// test block comment, no valid line before
		testEnterEdit(s, NL +"})"+ NEUTRAL_SRC1, TABn(0));
		
		s = line(TABn(indent) + "func((()))")+
			"/**/"; 		// test block comment at EOF 
		testEnterEdit(s, "", TABn(indent));
		
		
		/* ------- */
		
		// we don't consider the after-edit text in the edit-line for block balance in any case
		// if this changes, we need to review these two test cases
		
		s = line(TABn(indent) + "{func(")+
			"// foobar"; // test line comment, characters after that afect block balance
		testEnterEdit(s, "afterEdit)}"+NL+ NEUTRAL_SRC1, TABn(indent+2)/*, expectClose(indent+2, ")")*/);
		
		s = line(TABn(indent) + "{func(")+
			"// foobar{"; // test line comment, characters after that afect block balance
		testEnterEdit(s, "afterEdit)"+NL+ NEUTRAL_SRC1, TABn(indent+2)/*, expectClose(indent+2, ")")*/);
		
		
		s = line(TABn(indent) + "(func{")+
			TABn(indent) + "/* foobar"; // test edit inside block comment
		testEnterEdit(s, NL+"})*/})"+ NEUTRAL_SRC1, TABn(indent));
		
		s = line(TABn(indent) + "{func(")+
			"/* foobar"; 		   // test edit inside block comment
		testEnterAutoEdit(s, NL+ ")}*/"+ NEUTRAL_SRC1, TABn(indent+2), expectClose(indent+2, ")"));
		
		s = line(TABn(indent) + "{func}")+
			TABn(indent) + "{func{/* foobar}"; // test edit inside block comment
		testEnterAutoEdit(s, NL+"}}*/}"+ NEUTRAL_SRC1, TABn(indent+2), expectClose(indent+2, "}"));
		
	}
	
	@Test
	public void testSmartIdent_SyntaxErrors() throws Exception { testSmartIdent_SyntaxErrors$(); }
	public void testSmartIdent_SyntaxErrors$() throws Exception {
		String s;
		s = line("func")+
			"abc{"; // test 0 : 1 (with syntax error)
		testEnterEdit(s, NL +"})"+ NEUTRAL_SRC1, TABn(1));
		
		s = line("func{")+
			TAB+"{ab(c}"; // test 0 : 0 (corrected)
		testEnterEdit(s, PENDING_WS1+NL+"}"+ NEUTRAL_SRCX, TABn(1+0));

		s = line("func{")+
			TAB+"{ab)c}"; // test 0 : 0 (corrected)
		testEnterEdit(s, NL +"}"+ NEUTRAL_SRC3, TABn(1+0));

		s = line("func{")+
			TAB+"(ab{c)"; // test 0 : 2 (corrected)
		testEnterEdit(s, NL +"}"+NEUTRAL_SRC1+"}", TABn(1+0+2));
		
		s = line("func{")+
			TAB+"(ab}c)"; // test -1 : 0 (corrected)
		testEnterEdit(s, PENDING_WS2+NL +"}"+ NEUTRAL_SRCX, TABn(0));

		
		s = line("func{")+
			"}blah{)"; // test -1 : 1 (corrected)
		testEnterEdit(s, NL +"}"+ NEUTRAL_SRC3, TABn(1));
		
		
		s = line("func{")+
			"}blah{)"; // test -1 : 1 with close, right={)_..   
		testEnterAutoEdit(s, NL+/*}*/ NEUTRAL_SRC1, TABn(1), expectClose(0+1, "}"));
		
		s = line("func{")+
			"}blah{)"; // test -1 : 1 with close, right={)_({..(}
		testEnterAutoEdit(s, NL+/*}*/ "({"+NEUTRAL_SRC1+"(}", TABn(1), expectClose(0+1, "}"));
		
		s = line("func{")+
			line(TABn(0+4) + "func{()}")+ // test interim lines with irregular ident
			TABn(1) + "}blah("; // test close, -1 : 1, right=(_..} 
		testEnterAutoEdit(s, PENDING_WS2+NL+NEUTRAL_SRC1+"}", TABn(2), expectClose(0+2, ")"));
		
		s = line("func{{){")+    // (corrected)
			TAB+"abc}}(}"; // test -3 : 0 (corrected)
		testEnterEdit(s, PENDING_TXT+NL+NEUTRAL_SRCX, TABn(0));

		s = line("func{({")+    // (corrected on EOF)
			TAB+"aaa}})"; // test -3 : 0
		testEnterEdit(s, NL+NEUTRAL_SRC3, TABn(0));

		s = line("func(")+    // decoy
			line(TABn(0+7) + "{func{")+ // (corrected on '{' superblock )
			"aaa})";    
		testEnterEdit(s, NL+NEUTRAL_SRC1, TABn(0+7+1));
		
		// A boundary case, unbalanced close
		s = line("func}");
		testEnterEdit(s, ""+ NEUTRAL_SRC1, TABn(0));
		s = line(TABn(2) + "func}");
		testEnterEdit(s, ""+ NEUTRAL_SRC1, TABn(0));
	}
	
	/* ---------------------------------------*/
	
	@Test
	public void testSmartDeIndent() throws Exception { testSmartDeIndent$(); }
	public void testSmartDeIndent$() throws Exception {
		testSmartDeIndent$("\n");
		testSmartDeIndent$(NL);
	}
	
	protected void testSmartDeIndent$(String pNL) {
		String s;
		int indent = 0;
		
		s = "void main() {";
		testDeIndentAutoEdit(s, NL+TAB, pNL+"}"); 
		
		indent = 1;
		s = NEUTRAL_SRC1+
			TABn(indent) + "void main{} (";
		testDeIndentAutoEdit(s, expectInd(pNL, indent+1), pNL+")"); 
		
		
		s = NEUTRAL_SRC1+
			TABn(indent) + "void main{({";
		testDeIndentAutoEdit(s, expectInd(pNL, indent+3), "{{"+NL+TABn(indent+3+2));
		
		s = NEUTRAL_SRC1+
			TABn(indent) + "void main{({"; // Less indent than expected
		testDeIndentAutoEdit(s, expectInd(pNL, indent+1), "|{{", false);
		
		s = line("")+
			TABn(indent) + "\t\t";  // Test all Whitespace
		testDeIndentAutoEdit(s, expectInd(pNL, indent+2), PENDING_WS1+pNL+")"); 
		
		s = NEUTRAL_SRC1+
			"";  // Test empty line
		testDeIndentAutoEdit(s, expectInd(pNL, 0), PENDING_WS2+pNL+")"); 
		
		
		s = NEUTRAL_SRC1+
			line(TABn(indent) + "void func{({")+
			TABn(indent+1) + "void main()"; // test with 0 : 0 balance
		testDeIndentAutoEdit(s, expectInd(pNL, indent+1), PENDING_TXT+pNL+"}"); 
		
		
		s = NEUTRAL_SRC3+
			TABn(indent) + "void main{{)(";
		testDeIndentAutoEdit(s, expectInd(pNL, indent+3), "");
		
		s = "void main() }";
		testBackSpaceDeindent(s + NL, TAB, pNL);
		
		// Some boundary cases
		testDeIndentAutoEdit("", pNL+"", ""); 
		testDeIndentAutoEdit(TAB, pNL+"", "", false);
		testDeIndentAutoEdit(TAB+"func{", pNL+TAB, "", false);
		
		testBackSpaceCommandWithNoEffect(TAB, "" ); // backspace on first line
		testBackSpaceCommandWithNoEffect(TAB, "{" ); 
		testBackSpaceCommandWithNoEffect(" ", " {" );
		testBackSpaceCommandWithNoEffect(pNL+TAB, TAB+"{" );
		testBackSpaceCommandWithNoEffect(TAB+pNL+TAB+TAB, "");
		
		testDeleteCommandWithNoEffect("", pNL);
		testDeleteCommandWithNoEffect("", " ");
		testDeleteCommandWithNoEffect(TAB, pNL);
		testDeleteCommandWithNoEffect(NEUTRAL_SRC1, pNL);
		
		testArtificialNoopCommand("", ""); // Extreme boundary case
		testArtificialNoopCommand("", NL);
		testArtificialNoopCommand("", TAB);
		testArtificialNoopCommand(TAB, NL);
	}
	
	
	protected void testDeIndentAutoEdit(String srcPre, String srcIndent, String sourceAfter) {
		testDeIndentAutoEdit(srcPre, srcIndent, sourceAfter, true);
	}
	
	protected void testDeIndentAutoEdit(String srcPre, String srcIndent, String sourceAfter, 
			boolean indentNotSmaller) {
		testBackSpaceDeindent(srcPre, srcIndent, sourceAfter);
		
		testDeleteDeindent(srcPre, srcIndent, sourceAfter);
		
		if(indentNotSmaller) {
			testBackSpaceCommandWithNoEffect(srcPre + srcIndent +TAB, sourceAfter);
		}
		
		String pureIndent = srcIndent.replaceFirst("(\r)?\n", "");
		if(pureIndent.length() == 0) {
			return; // There is no middle of indent available for further tests
		}
		
		// AutoEdit should not apply in the middle of indent element, test that
		String srcPre2 = srcPre + srcIndent.substring(0, srcIndent.length()-1);
		String srcAfter2 = srcIndent.substring(srcIndent.length()-1, srcIndent.length()) + sourceAfter;
		testBackSpaceCommandWithNoEffect(srcPre2, srcAfter2);
		
		int nlSize = srcIndent.length() - pureIndent.length();
		String srcPre3 = srcPre + srcIndent.substring(0, nlSize);
		String srcAfter3 = srcIndent.substring(nlSize, srcIndent.length()) + sourceAfter;
		testDeleteCommandWithNoEffect(srcPre3, srcAfter3);
	}
	
	protected void testDeleteDeindent(String srcPre, String srcIndent, String sourceAfter) {
		DocumentCommand delCommand = applyDelCommand(srcPre, srcIndent + sourceAfter);
		getAutoEditStrategy().customizeDocumentCommand(getDocument(), delCommand);
		checkCommand(delCommand, "", srcPre.length(), srcIndent.length());
	}
	
	protected void testBackSpaceDeindent(String srcPre, String srcIndent, String sourceAfter) {
		DocumentCommand bsCommand = applyBackSpaceCommand(srcPre + srcIndent, sourceAfter);
		getAutoEditStrategy().customizeDocumentCommand(getDocument(), bsCommand);
		checkCommand(bsCommand, "", srcPre.length(), srcIndent.length());
	}
	
	
	protected DocumentCommand applyBackSpaceCommand(String srcPre, String sourceAfter) {
		getDocument().set(srcPre + sourceAfter);
		int keypressOffset = srcPre.length();
		int length;
		try {
			IRegion lineInfo = getDocument().getLineInformationOfOffset(keypressOffset);
			int lineLimit = lineInfo.getOffset();
			int line = getDocument().getLineOfOffset(keypressOffset);
			length = (keypressOffset == lineLimit) ? getDocument().getLineDelimiter(line - 1).length() : 1;
		} catch (BadLocationException e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		}
		getAutoEditStrategy().lastKeyEvent.character = SWT.BS;
		DocumentCommand docCommand = createDocumentCommand(keypressOffset - length, length, "");
		return docCommand;
	}
	
	protected DocumentCommand applyDelCommand(String sourcePre, String sourceAfter) {
		getDocument().set(sourcePre + sourceAfter);
		int keypressOffset = sourcePre.length();
		int length;
		try {
			IRegion lineInfo = getDocument().getLineInformationOfOffset(keypressOffset);
			int lineEnd = lineInfo.getOffset() + lineInfo.getLength();
			int line = getDocument().getLineOfOffset(keypressOffset);
			length = (keypressOffset == lineEnd) ? getDocument().getLineDelimiter(line).length() : 1;
		} catch (BadLocationException e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		}
		getAutoEditStrategy().lastKeyEvent.character = SWT.DEL;
		DocumentCommand docCommand = createDocumentCommand(keypressOffset, length, "");
		return docCommand;
	}
	
	protected void testBackSpaceCommandWithNoEffect(String sourcePre, String sourceAfter) {
		DocumentCommand bsCommand = applyBackSpaceCommand(sourcePre, sourceAfter);
		testCommandWithNoEffect(bsCommand);
	}
	
	protected void testDeleteCommandWithNoEffect(String sourcePre, String sourceAfter) {
		DocumentCommand delCommand = applyDelCommand(sourcePre, sourceAfter);
		testCommandWithNoEffect(delCommand);
	}
	
	protected void testCommandWithNoEffect(DocumentCommand bsCommand) {
		int length = bsCommand.length;
		int offset = bsCommand.offset;
		String text = bsCommand.text;
		getAutoEditStrategy().customizeDocumentCommand(getDocument(), bsCommand);
		checkCommand(bsCommand, text, offset, length);
	}
	
	protected void testArtificialNoopCommand(String sourcePre, String sourceAfter) {
		String text = sourcePre + sourceAfter;
		getDocument().set(text);
		testCommandWithNoEffect(createDocumentCommand(sourcePre.length(), 0, ""));
		testCommandWithNoEffect(createDocumentCommand(sourceAfter.length(), 0, ""));
		testCommandWithNoEffect(createDocumentCommand(text.length(), 0, ""));
	}
	
}
