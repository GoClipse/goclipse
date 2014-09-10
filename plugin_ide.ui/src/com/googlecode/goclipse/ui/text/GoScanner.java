package com.googlecode.goclipse.ui.text;

import melnorme.lang.ide.ui.text.coloring.AbstractLangScanner;

import org.eclipse.cdt.ui.text.ITokenStoreFactory;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.WhitespaceRule;

import com.googlecode.goclipse.ui.GoUIPreferenceConstants;
import com.googlecode.goclipse.ui.text.CombinedWordRule.WordMatcher;

public class GoScanner extends AbstractLangScanner {
	
	private static String tokenPrefProperties[] = new String[] {
		GoUIPreferenceConstants.SYNTAX_COLORING__TEXT.key,
		GoUIPreferenceConstants.SYNTAX_COLORING__KEYWORD.key,
		GoUIPreferenceConstants.SYNTAX_COLORING__VALUE.key,
		GoUIPreferenceConstants.SYNTAX_COLORING__PRIMITIVE.key,
		GoUIPreferenceConstants.SYNTAX_COLORING__BUILTIN_FUNCTION.key,
		GoUIPreferenceConstants.SYNTAX_COLORING__OPERATOR.key,
		GoUIPreferenceConstants.SYNTAX_COLORING__STRING.key,
	};
	
	public GoScanner(ITokenStoreFactory tokenStoreFactory) {
		super(tokenStoreFactory.createTokenStore(tokenPrefProperties));
		
		final IToken text = getToken(GoUIPreferenceConstants.SYNTAX_COLORING__TEXT.key);
		
		final WordMatcher keywordRule = new WordMatcher();
		CombinedWordRule combinedWordRule = new CombinedWordRule(new IWordDetector() {
			@Override
			public boolean isWordStart(char c) {
				String s = new String(new char[] { c });

				return s.matches("[A-Za-z_]");
			}

			@Override
			public boolean isWordPart(char c) {
				String s = new String(new char[] { c });
				return s.matches("[A-Za-z0-9_]");
			}
		}, keywordRule, text);
		

			final IToken keyword         = getToken(GoUIPreferenceConstants.SYNTAX_COLORING__KEYWORD.key);
			final IToken value           = getToken(GoUIPreferenceConstants.SYNTAX_COLORING__VALUE.key);
			final IToken primitive       = getToken(GoUIPreferenceConstants.SYNTAX_COLORING__PRIMITIVE.key);
			final IToken builtinFunction = getToken(GoUIPreferenceConstants.SYNTAX_COLORING__BUILTIN_FUNCTION.key);
			final IToken operator        = getToken(GoUIPreferenceConstants.SYNTAX_COLORING__OPERATOR.key);
			final IToken textToken       = getToken(GoUIPreferenceConstants.SYNTAX_COLORING__TEXT.key);
			final IToken stringDelimeter = getToken(GoUIPreferenceConstants.SYNTAX_COLORING__STRING.key); 
			setDefaultReturnToken(textToken);
			
			// add tokens for each reserved word
			keywordRule.addWord("break",       keyword);
			keywordRule.addWord("default",     keyword);
			keywordRule.addWord("func",        keyword);
			keywordRule.addWord("interface",   keyword);
			keywordRule.addWord("select",      keyword);
			keywordRule.addWord("case",        keyword);
			keywordRule.addWord("defer",       keyword);
			keywordRule.addWord("go",          keyword);
			keywordRule.addWord("map",         keyword);
			keywordRule.addWord("struct",      keyword);
			keywordRule.addWord("chan",        keyword);
			keywordRule.addWord("else",        keyword);
			keywordRule.addWord("goto",        keyword);
			keywordRule.addWord("package",     keyword);
			keywordRule.addWord("switch",      keyword);
			keywordRule.addWord("const",       keyword);
			keywordRule.addWord("fallthrough", keyword);
			keywordRule.addWord("if",          keyword);
			keywordRule.addWord("range",       keyword);
			keywordRule.addWord("type",        keyword);
			keywordRule.addWord("continue",    keyword);
			keywordRule.addWord("for",         keyword);
			keywordRule.addWord("import",      keyword);
			keywordRule.addWord("return",      keyword);
			keywordRule.addWord("var",         keyword);

			keywordRule.addWord("append",  builtinFunction);
			keywordRule.addWord("cap",     builtinFunction);
			keywordRule.addWord("close",   builtinFunction);
			keywordRule.addWord("complex", builtinFunction);
			keywordRule.addWord("copy",    builtinFunction);
			keywordRule.addWord("delete",  builtinFunction);
			keywordRule.addWord("imag",    builtinFunction);
			keywordRule.addWord("len",     builtinFunction);
			keywordRule.addWord("make",    builtinFunction);
			keywordRule.addWord("new",     builtinFunction);
			keywordRule.addWord("panic",   builtinFunction);
			keywordRule.addWord("print",   builtinFunction);
			keywordRule.addWord("println", builtinFunction);
			keywordRule.addWord("real",    builtinFunction);
			keywordRule.addWord("recover", builtinFunction);

			keywordRule.addWord("nil",   value);
			keywordRule.addWord("true",  value);
			keywordRule.addWord("false", value);
			keywordRule.addWord("iota",  value);

			keywordRule.addWord("uint8", primitive); // the set of all unsigned
													 // 8-bit integers (0 to
													 // 255)
			keywordRule.addWord("uint16", primitive); // the set of all unsigned
													  // 16-bit integers (0 to
													  // 65535)
			keywordRule.addWord("uint32", primitive); // the set of all unsigned
													  // 32-bit integers (0 to
													  // 4294967295)
			keywordRule.addWord("uint64", primitive); // the set of all unsigned
													  // 64-bit integers (0 to
													  // 18446744073709551615)
			keywordRule.addWord("int8", primitive); // the set of all signed
													// 8-bit integers (-128 to
													// 127)
			keywordRule.addWord("int16", primitive); // the set of all signed
													 // 16-bit integers (-32768
													 // to 32767)
			keywordRule.addWord("int32", primitive); // the set of all signed
													 // 32-bit integers
													 // (-2147483648 to
													 // 2147483647)
			keywordRule.addWord("int64", primitive); // the set of all signed
													 // 64-bit integers
													 // (-9223372036854775808 to
													 // 9223372036854775807)
			keywordRule.addWord("float32", primitive); // the set of all
													   // IEEE-754 32-bit
													   // floating-point numbers
			keywordRule.addWord("float64", primitive); // the set of all
													   // IEEE-754 64-bit
													   // floating-point numbers
			keywordRule.addWord("complex64",  primitive);
			keywordRule.addWord("complex128", primitive);
			keywordRule.addWord("rune",       primitive);
			keywordRule.addWord("byte",       primitive); // familiar alias for uint8
			keywordRule.addWord("uint",       primitive); // either 32 or 64 bits
			keywordRule.addWord("int",        primitive); // either 32 or 64 bits
			keywordRule.addWord("uintptr",    primitive); // an unsigned integer
													   // large enough to store
													   // the uninterpreted bits
													   // of a pointer value
			keywordRule.addWord("string", primitive);
			keywordRule.addWord("bool",   primitive);
			keywordRule.addWord("error",  primitive);

			keywordRule.addWord("`",    stringDelimeter);
			keywordRule.addWord("\"",   stringDelimeter);
			
			keywordRule.addWord("+",   operator);
			keywordRule.addWord("&",   operator);
			keywordRule.addWord("+=",  operator);
			keywordRule.addWord("&=",  operator);
			keywordRule.addWord("&&",  operator);
			keywordRule.addWord("==",  operator);
			keywordRule.addWord("!=",  operator);
			keywordRule.addWord("(",   operator);
			keywordRule.addWord(")",   operator);
			keywordRule.addWord("-",   operator);
			keywordRule.addWord("|",   operator);
			keywordRule.addWord("-=",  operator);
			keywordRule.addWord("|=",  operator);
			keywordRule.addWord("||",  operator);
			keywordRule.addWord("<",   operator);
			keywordRule.addWord("<=",  operator);
			keywordRule.addWord("[",   operator);
			keywordRule.addWord("]",   operator);
			keywordRule.addWord("*",   operator);
			keywordRule.addWord("^",   operator);
			keywordRule.addWord("*=",  operator);
			keywordRule.addWord("^=",  operator);
			keywordRule.addWord("<-",  operator);
			keywordRule.addWord(">",   operator);
			keywordRule.addWord(">=",  operator);
			keywordRule.addWord("{",   operator);
			keywordRule.addWord("}",   operator);
			keywordRule.addWord("/",   operator);
			keywordRule.addWord("<<",  operator);
			keywordRule.addWord("/=",  operator);
			keywordRule.addWord("<<=", operator);
			keywordRule.addWord("++",  operator);
			keywordRule.addWord("=",   operator);
			keywordRule.addWord(":=",  operator);
			keywordRule.addWord(",",   operator);
			keywordRule.addWord(";",   operator);
			keywordRule.addWord("%",   operator);
			keywordRule.addWord(">>",  operator);
			keywordRule.addWord("%=",  operator);
			keywordRule.addWord(">>=", operator);
			keywordRule.addWord("--",  operator);
			keywordRule.addWord("!",   operator);
			keywordRule.addWord("...", operator);
			keywordRule.addWord(":",   operator);
			keywordRule.addWord("&^",  operator);
			keywordRule.addWord("&^=", operator);

			setRules(new IRule[] { combinedWordRule, new WhitespaceRule(new IWhitespaceDetector() {
				@Override
				public boolean isWhitespace(char c) {
					return Character.isWhitespace(c);
				}
			}) });
		
	}
	
}
