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
import melnorme.utilbox.misc.MiscUtil;

public class LangAutoEditStrategyTest extends Scanner_BaseTest {
	
	public static final String NEUTRAL_SRCX; 
	
	static {
		NEUTRAL_SRCX = MiscUtil.getClassResourceAsString(LangAutoEditStrategyTest.class, "sample_block_code");
	}
	
	public static final String PENDING_WS1 = "  "; 
	public static final String PENDING_WS2 = "\t ";
	public static final String PENDING_TXT = "\tpending";
	
	protected LangAutoEditStrategy autoEditStrategy;
	
	protected LangAutoEditStrategy getAutoEditStrategy() {
		if(autoEditStrategy == null) {
			
			ILangAutoEditsPreferencesAccess preferences = new ILangAutoEditsPreferencesAccess() {
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
			};
			
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
	
	@Test
	public void testSmartIndentBasic() {
		testEnterAutoEdit("void main(){}", "blah", NL); // balance 0 : 0
		
		testEnterAutoEdit("void main(){", NL+"}", NL+TAB); // balance 0 : 1 (closed)
		testEnterAutoEdit("void main(){", "}",    NL+TAB);
	}
	
	@Test
	public void testSmartIndentBasic2() {
		String dNL = getDocument().getDefaultLineDelimiter();
		// balance 0 : 1(unclosed)
		testEnterAutoEdit("void main{", ""                             , NL+TAB, dNL+"}");
		testEnterAutoEdit("void main(",      NL+"func(){}"+NL+"blah();", NL+TAB,  NL+")");
		testEnterAutoEdit("vo() main{", "  "+NL+"func(){}"+NL+"blah();", NL+TAB,  NL+"}");
		// balance 0 : 1(unclosed but don't close due to pending text)
		testEnterAutoEdit("void main(){",       "func(){}"+NL+"blah();", NL+TAB);
	}
	
	@Test
	public void testSmartIndentBasic3() {
		String s;
		
		s = line("func{")+
			TAB+"abc}"; // balance -1 : 0
		testEnterAutoEdit(s, "}"+NEUTRAL_SRC1, NL);

		s = line("func{{")+
			TAB+"abc}}"; // balance -2 : 0
		testEnterAutoEdit(s, "}"+NEUTRAL_SRC1, NL);
		
		s = line(TAB+"func((")+
			TAB+"abc))"; // balance -2 : 0	 '('
		testEnterAutoEdit(s, NEUTRAL_SRC1+")", NL+TAB);
	}
	
	protected final void testEnterAutoEdit(String sourceBefore, String sourceAfter, String expectedEdit) {
		dotestEnterAutoEdit(sourceBefore, sourceAfter, expectedEdit, -1);
	}
	
	protected void testEnterAutoEdit(String textBefore, String textAfter, String expInsert, String expInsertAfter) {
		dotestEnterAutoEdit(textBefore, textAfter, expInsert+expInsertAfter, expInsert.length());
	}
	
	protected void dotestEnterAutoEdit(String textBefore, String textAfter, String expectedInsert, int offsetDelta) {
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
	
	
	@Test
	public void testSmartIndent() throws Exception { testSmartIndent$(); }
	public void testSmartIndent$() throws Exception {
		int indent = 0;
		String s;
		
		s = mkline(indent, "func(")+
			mklast(indent, "abc{"); // test 0 : 1
		testEnterAutoEdit(s, NL +"})"+ NEUTRAL_SRC1, expectInd(indent+1));
		
		s = mkline(indent, "func{")+
			mklast(indent, "}abc{"); // test -1 : 1
		testEnterAutoEdit(s, PENDING_WS1+NL+"}"+ NEUTRAL_SRC1, expectInd(indent+1));
		
		indent = 1;

		s = mkline(indent, "func{")+
			mklast(indent, "\t\t "); // test all WhiteSpace   
		testEnterAutoEdit(s, NL +")"+ NEUTRAL_SRCX, expectInd(indent+2)+" ");
		
		s = mkline(indent, "func{")+
			mklast(indent, "\t\t "); // test all WhiteSpace with pending WhiteSpace   
		testEnterAutoEdit(s, PENDING_WS2+NL+")"+ NEUTRAL_SRCX, expectInd(indent+2)+" ");

		
		s = mkline(indent, "func{")+
			mklast(indent, "}blah("); // test another -1 : 1   
		testEnterAutoEdit(s, NL +")"+ NEUTRAL_SRCX, expectInd(indent+1));
		
		
		s = mkline(indent, "func({")+
			mklast(indent, "abc(");     // test potential close (go outside dominating block?)
		testEnterAutoEdit(s, NL+")"+ NEUTRAL_SRC1+"}", expectInd(indent+1));
		
		s = mkline(indent, "func{")+
			mklast(indent, "abc(");     // test potential close (unclosed dominating block)
		testEnterAutoEdit(s, NL+")", expectInd(indent+1));
		
		s = mkline(indent, "func{")+
			mklast(indent, "abc(");     // test potential close (pending text)
		testEnterAutoEdit(s, PENDING_TXT+NL+")", expectInd(indent+1));
		
		
		s = mkline(indent, "func{")+
			mklast(indent, "}blah(");   // test close, -1 : 1, right=(_
		testEnterAutoEdit(s, NL+NEUTRAL_SRC1, expectInd(indent+1), expectClose(indent+1, ")"));
		
		s = mkline(indent, "func{")+
			mklast(indent, "}blah{{");  // test close, -1 : 2, right={{_
		testEnterAutoEdit(s, PENDING_WS2+NL, expectInd(indent+2), expectClose(indent+2, "}"));
		
		s = mkline(indent, "func{")+
			mklast(indent, "}}blah{");  // test close, -2 : 1, right={_..
		testEnterAutoEdit(s, NL+NEUTRAL_SRC1, expectInd(indent+1), expectClose(indent+1, "}"));
		
		s = mkline(indent, "func{")+
			mklast(indent, "}}blah{{"); // test close, -2 : 1, right= {{_..}     
		testEnterAutoEdit(s, NL+NEUTRAL_SRC1+"}", expectInd(indent+2), expectClose(indent+2, "}"));
		
		s = mkline(indent, "}}blah{")+
			mkline(indent, "{func{")+
			mkline(indent+2, NEUTRAL_SRC1)+
			mklast(indent, "}blah{"); // test close, -2 : 1, right=}} {{..}{_..}     
		testEnterAutoEdit(s, NL+NEUTRAL_SRC1+mkline(indent, "}"), expectInd(indent+1), expectClose(indent+1, "}"));
		
		indent = 0;
		
		s = mkline(indent, "func{{{")+
			mklast(indent, TAB+"abc}}}"); // test -3 : 0
		testEnterAutoEdit(s, NL+NEUTRAL_SRCX, expectInd(indent+0));

		s = mkline(indent+7, "func({{{")+  // start block still has : 2 open block
			mklast(indent  , TAB+"abc}}"); // test -2 : 0
		testEnterAutoEdit(s, NL+NEUTRAL_SRCX, expectInd(indent+7+2));

		indent = 0;
		s = mkline(indent, "func")+
			mklast(indent, TAB+"abc}}}"); // test -3 : 0 with zero indent
		testEnterAutoEdit(s, NL+NEUTRAL_SRCX, expectInd(indent+0));
		
		s = mkline(indent, "func")+
			mklast(indent, "abc}}}");     // test -3 : 0 with zero indent
		testEnterAutoEdit(s, NL+NEUTRAL_SRCX, expectInd(indent+0));

		
		s = mkline(indent, "func{{{")+
			mkline(indent+4, "func{()}")+ // test interim lines with irregular ident
			mklast(indent, TAB+"abc}}");  // -2 : 0
		testEnterAutoEdit(s, NL+NEUTRAL_SRC1, expectInd(indent+1)); 
		
		indent = 2;
		s = mkline(indent, NEUTRAL_SRC1)+ // more lines
			mkline(indent, "}}func{{{")+  // matching start block is -2 : 3
			mkline(indent, NEUTRAL_SRC1)+ // more lines
			mkline(indent-2, "func{()}")+ // interim lines with irregular ident (negative)
			mklast(indent, TAB+"abc(blah{}) blah}}"); // -2 : 0
		testEnterAutoEdit(s, NL+NEUTRAL_SRCX, expectInd(indent+1));
		
		
		s = mkline(indent, "func{")+
			mklast(0, ""); // test empty line   
		testEnterAutoEdit(s, "){"+NL+")", expectInd(0));
	}
	
	@Test
	public void testSmartIdent_Boundary() throws Exception { testSmartIdent_Boundary$(); }
	public void testSmartIdent_Boundary$() throws Exception {
		String s;
		s = "(";     // test potential close
		testEnterAutoEdit(s, NL +")"+ NEUTRAL_SRC1+"}", expectInd(1));

		s = "}(";     // test potential close
		testEnterAutoEdit(s, NL +")"+ NEUTRAL_SRC1+"}", expectInd(1));
		
		s = "{"+NL+"(";     // test potential close
		testEnterAutoEdit(s, NL +"){", expectInd(1));
	}
	
	@Test
	public void testSmartIndent_xPartitioning() throws Exception { testSmartIndent_xPartitioning$(); }
	public void testSmartIndent_xPartitioning$() throws Exception {
		assertContains(getDocument().getPartitionings(), SamplePartitionScanner.LANG_PARTITIONING);
		
		int indent = 1; // Needs to be greater than 1
		String s;
		
		s = mkline(indent, "func()")+
			mklast(0, "//"+TAB+"abc"); // test with line comment
		testEnterAutoEdit(s, NL +"})"+ NEUTRAL_SRC1, expectInd(indent));
		
		s = mkline(indent, "func(")+
			mklast(0, "//"+TAB+"abc)"); // test with line comment, with +1 indent
		testEnterAutoEdit(s, NL +")"+ NEUTRAL_SRC1, expectInd(indent+1));
		
		s = mkline(indent, "func{")+
			mkline(indent+1, "blah}")+
			mklast(0, "//"+TAB+"abc)"); // test with line comment, with -1 indent
		testEnterAutoEdit(s, NL +")"+ NEUTRAL_SRC1, expectInd(indent));
		
		s = mkline(indent, "{func(")+
			mklast(0, "//"+TAB+"abc"); // test with line comment, characters after
		testEnterAutoEdit(s, ")"+NL+ NEUTRAL_SRC1, expectInd(indent+2));
		
		
		s = mkline(indent, "func({")+
			mklast(0, "/**/"); 			// test with block comment, with +2 indent Close
		testEnterAutoEdit(s, NL +"blah"+ NEUTRAL_SRC1, expectInd(indent+2), expectClose(indent+2, "}"));
		
		s = mkline(indent, "func(((")+
			mklast(indent, "// blah"); 		// test line comment with whitespace before, (This-Line)
		testEnterAutoEdit(s, NL +"}}}"+ NEUTRAL_SRC1, expectInd(indent));
		s = mkline(indent, "func(((")+
			mklast(indent, "/**/"); 		// test block comment with whitespace before, (This-Line)
		testEnterAutoEdit(s, NL +"}}}"+ NEUTRAL_SRC1, expectInd(indent));

		s = mkline(indent, "func(")+
			mklast(0     , "/* */"+TAB+"abc{{{"); // test block comment with characters after, (This-Line)
		testEnterAutoEdit(s, NL +"}}})"+ NEUTRAL_SRC1, expectInd(0+3));
		
		
		s = mklast(0, "//abc{"); 		// test line comment, no valid line before
		testEnterAutoEdit(s, NL +"})"+ NEUTRAL_SRC1, expectInd(0));
		s = mklast(0, "/*abc{*/"); 		// test block comment, no valid line before
		testEnterAutoEdit(s, NL +"})"+ NEUTRAL_SRC1, expectInd(0));
		
		s = mkline(indent, "func((()))")+
			mklast(0, "/**/"); 		// test block comment at EOF 
		testEnterAutoEdit(s, "", expectInd(indent));
		
		
		/* ------- */
		
		// we don't consider the after-edit text in the edit-line for block balance in any case
		// if this changes, we need to review these two test cases
		
		s = mkline(indent, "{func(")+
			mklast(0, "// foobar"); // test line comment, characters after that afect block balance
		testEnterAutoEdit(s, "afterEdit)}"+NL+ NEUTRAL_SRC1, expectInd(indent+2)/*, expectClose(indent+2, ")")*/);
		
		s = mkline(indent, "{func(")+
			mklast(0, "// foobar{"); // test line comment, characters after that afect block balance
		testEnterAutoEdit(s, "afterEdit)"+NL+ NEUTRAL_SRC1, expectInd(indent+2)/*, expectClose(indent+2, ")")*/);
		
		
		s = mkline(indent, "(func{")+
			mklast(indent, "/* foobar"); // test edit inside block comment
		testEnterAutoEdit(s, NL+"})*/})"+ NEUTRAL_SRC1, expectInd(indent));
		
		s = mkline(indent, "{func(")+
			mklast(0, "/* foobar"); 		   // test edit inside block comment
		testEnterAutoEdit(s, NL+ ")}*/"+ NEUTRAL_SRC1, expectInd(indent+2), expectClose(indent+2, ")"));
		
		s = mkline(indent, "{func}")+
			mklast(indent, "{func{/* foobar}"); // test edit inside block comment
		testEnterAutoEdit(s, NL+"}}*/}"+ NEUTRAL_SRC1, expectInd(indent+2), expectClose(indent+2, "}"));
		
	}
	
	protected String mkline(int indent, String string) {
		return line(TABn(indent) + string);
	}
	
	protected String mklast(int indent, String string) {
		return TABn(indent) + string;
	}
	
	protected static String TABn(int indent) {
		return LangAutoEditUtils.stringNTimes(TAB, indent);
	}
	
	protected static String expectInd(int indent) {
		return expectInd(NL, indent);
	}
	
	private static String expectInd(String nl, int indent) {
		return nl+TABn(indent);
	}
	
	protected static String expectClose(int indent, String close) {
		return NL+TABn(indent-1)+close;
	}
	
	
	@Test
	public void testSmartIdent_SyntaxErrors() throws Exception { testSmartIdent_SyntaxErrors$(); }
	public void testSmartIdent_SyntaxErrors$() throws Exception {
		String s;
		int indent = 0;
		
		s = mkline(indent, "func")+
			mklast(indent, "abc{"); // test 0 : 1 (with syntax error)
		testEnterAutoEdit(s, NL +"})"+ NEUTRAL_SRC1, expectInd(indent+1));
		
		s = mkline(indent, "func{")+
			mklast(indent, TAB+"{ab(c}"); // test 0 : 0 (corrected)
		testEnterAutoEdit(s, PENDING_WS1+NL+"}"+ NEUTRAL_SRCX, expectInd(1+indent));

		s = mkline(indent, "func{")+
			mklast(indent, TAB+"{ab)c}"); // test 0 : 0 (corrected)
		testEnterAutoEdit(s, NL +"}"+ NEUTRAL_SRC3, expectInd(1+indent));

		indent = 1;
		s = mkline(indent, "func{")+
			mklast(indent, TAB+"(ab{c)"); // test 0 : 2 (corrected)
		testEnterAutoEdit(s, NL +"}"+NEUTRAL_SRC1+"}", expectInd(1+indent+2));
		
		s = mkline(indent, "func{")+
			mklast(indent, TAB+"(ab}c)"); // test -1 : 0 (corrected)
		testEnterAutoEdit(s, PENDING_WS2+NL +"}"+ NEUTRAL_SRCX, expectInd(indent));

		
		s = mkline(indent, "func{")+
			mklast(indent, "}blah{)"); // test -1 : 1 (corrected)
		testEnterAutoEdit(s, NL +"}"+ NEUTRAL_SRC3, expectInd(indent+1));
		
		
		s = mkline(indent, "func{")+
			mklast(indent, "}blah{)"); // test -1 : 1 with close, right={)_..   
		testEnterAutoEdit(s, NL+/*}*/ NEUTRAL_SRC1, expectInd(indent+1), expectClose(indent+1, "}"));
		
		s = mkline(indent, "func{")+
			mklast(indent, "}blah{)"); // test -1 : 1 with close, right={)_({..(}
		testEnterAutoEdit(s, NL+/*}*/ "({"+NEUTRAL_SRC1+"(}", expectInd(indent+1), expectClose(indent+1, "}"));
		
		s = mkline(indent  , "func{")+
			mkline(indent+4, "func{()}")+ // test interim lines with irregular ident
			mklast(indent+1, "}blah("); // test close, -1 : 1, right=(_..} 
		testEnterAutoEdit(s, PENDING_WS2+NL+NEUTRAL_SRC1+"}", expectInd(indent+2), expectClose(indent+2, ")"));
		
		s = mkline(indent, "func{{){")+    // (corrected)
			mklast(indent, TAB+"abc}}(}"); // test -3 : 0 (corrected)
		testEnterAutoEdit(s, PENDING_TXT+NL+NEUTRAL_SRCX, expectInd(indent+0));

		s = mkline(indent, "func{({")+    // (corrected on EOF)
			mklast(indent, TAB+"aaa}})"); // test -3 : 0
		testEnterAutoEdit(s, NL+NEUTRAL_SRC3, expectInd(indent+0));

		s = mkline(indent, "func(")+    // decoy
			mkline(indent+7, "{func{")+ // (corrected on '{' superblock )
			mklast(indent, "aaa})");    
		testEnterAutoEdit(s, NL+NEUTRAL_SRC1, expectInd(indent+7+1));
		
		// A boundary case, unbalanced close
		s = mkline(0, "func}");
		testEnterAutoEdit(s, ""+ NEUTRAL_SRC1, expectInd(0));
		s = mkline(2, "func}");
		testEnterAutoEdit(s, ""+ NEUTRAL_SRC1, expectInd(0));
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
		
		s = mklast(0, "void main() {");
		testDeIndentAutoEdit(s, NL+TAB, pNL+"}"); 
		
		indent = 1;
		s = NEUTRAL_SRC1+
			mklast(indent, "void main{} (");
		testDeIndentAutoEdit(s, expectInd(pNL, indent+1), pNL+")"); 
		
		
		s = NEUTRAL_SRC1+
			mklast(indent, "void main{({");
		testDeIndentAutoEdit(s, expectInd(pNL, indent+3), "{{"+NL+TABn(indent+3+2));
		
		s = NEUTRAL_SRC1+
			mklast(indent, "void main{({"); // Less indent than expected
		testDeIndentAutoEdit(s, expectInd(pNL, indent+1), "|{{", false);
		
		s = mkline(0, "")+
			mklast(indent, "\t\t");  // Test all Whitespace
		testDeIndentAutoEdit(s, expectInd(pNL, indent+2), PENDING_WS1+pNL+")"); 
		
		s = NEUTRAL_SRC1+
			mklast(0, "");  // Test empty line
		testDeIndentAutoEdit(s, expectInd(pNL, 0), PENDING_WS2+pNL+")"); 
		
		
		s = NEUTRAL_SRC1+
			mkline(indent+0, "void func{({")+
			mklast(indent+1, "void main()"); // test with 0 : 0 balance
		testDeIndentAutoEdit(s, expectInd(pNL, indent+1), PENDING_TXT+pNL+"}"); 
		
		
		s = NEUTRAL_SRC3+
			mklast(indent, "void main{{)(");
		testDeIndentAutoEdit(s, expectInd(pNL, indent+3), "");
		
		s = mklast(0, "void main() }");
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
