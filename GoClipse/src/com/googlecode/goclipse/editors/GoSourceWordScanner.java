/**
 * 
 */
package com.googlecode.goclipse.editors;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

/**
 * @author eko
 *
 * This is a wordrule that automatically has all of the Go keywords
 */
public class GoSourceWordScanner extends WordRule {

	public GoSourceWordScanner() {
		super(new WordDetector(), new Token(new TextAttribute(IGoSourceColorConstants.DEFAULT)));

		addKeyWords();
	}

	private void addKeyWords()
	{
		IToken keyword = new Token(new TextAttribute(IGoSourceColorConstants.KEYWORD, null, SWT.BOLD, null));
		IToken type = new Token(new TextAttribute(IGoSourceColorConstants.TYPE, null, SWT.BOLD, null));

		// Key words
		//   break        default      func         interface    select
		//   case         defer        go           map          struct
		//   chan         else         goto         package      switch
		//   const        fallthrough  if           range        type
		//   continue     for          import       return       var
		addWord("break",       keyword);
		addWord("case",        keyword);
		addWord("chan",        keyword);
		addWord("const",       keyword);
		addWord("continue",    keyword);
		addWord("default",     keyword);
		addWord("defer",       keyword);
		addWord("else",        keyword);
		addWord("fallthrough", keyword);
		addWord("for",         keyword);
		addWord("func",        keyword);
		addWord("go",          keyword);
		addWord("goto",        keyword);
		addWord("if",          keyword);
		addWord("import",      keyword);
		addWord("interface",   keyword);
		addWord("map",         keyword);
		addWord("package",     keyword);
		addWord("range",       keyword);
		addWord("return",      keyword);
		addWord("select",      keyword);
		addWord("struct",      keyword);
		addWord("switch",      keyword);
		addWord("type",        keyword);
		addWord("var",         keyword);
		addWord("iota",        keyword);

		// Types
		//   uint8    int8
		//   uint16   int16   
		//   uint32   int32   float32   complex64
		//   uint64   int64   float64   complex128  byte
		//   uint     int     float     complex     uintptr   string
		addWord("byte",       type);  
		addWord("uint",       type); 
		addWord("uint8",      type); 
		addWord("uint16",     type); 
		addWord("uint32",     type); 
		addWord("uint64",     type);
		addWord("uintptr",    type);
		addWord("int",        type);
		addWord("int8",       type);
		addWord("int16",      type);
		addWord("int32",      type);
		addWord("int64",      type);
		addWord("float",      type);
		addWord("float32",    type);
		addWord("float64",    type);
		addWord("complex",    type);
		addWord("complex64",  type);
		addWord("complex128", type);
		addWord("string",     type);
	}

	private static class WordDetector implements IWordDetector {
		public boolean isWordStart(char c) {
			return Character.isJavaIdentifierStart(c);
		}

		public boolean isWordPart(char c) {
			// TODO Auto-generated method stub
			return Character.isJavaIdentifierPart(c);
		}
	}
}
