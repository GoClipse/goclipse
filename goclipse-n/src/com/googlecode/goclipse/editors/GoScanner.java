package com.googlecode.goclipse.editors;

import java.util.Map;

import org.eclipse.jdt.internal.ui.text.CombinedWordRule;
import org.eclipse.jdt.internal.ui.text.CombinedWordRule.WordMatcher;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.preferences.PreferenceConstants;

@SuppressWarnings("restriction")
public class GoScanner extends RuleBasedScanner {

	public GoScanner() {
	   Token text = new Token(new TextAttribute(ColorManager.INSTANCE.getColor(IColorConstants.DEFAULT), null, SWT.NONE));
	   CombinedWordRule combinedWordRule = new CombinedWordRule(new IWordDetector() {
         public boolean isWordStart(char c) {
            String s =new String(new char[]{c});
            
            return s.matches("[A-Za-z_]");
         }

         public boolean isWordPart(char c) {
            String s =new String(new char[]{c});
            return s.matches("[A-Za-z0-9_]");
         }
      }, text);
	   
	   
	   
	   //WordDetector wordDetector = new WordDetector();
	   //KeywordRule keywordRule = new KeywordRule(wordDetector);
	   //wordDetector.keywordRule = keywordRule;
//		WordRule keywordRule = new WordRule(new IWordDetector() {
//         public boolean isWordStart(char c) {
//            String s =new String(new char[]{c});
//            
//            return s.matches("[A-Za-z_]");
//         }
//
//         public boolean isWordPart(char c) {
//            String s =new String(new char[]{c});
//            return s.matches("[A-Za-z0-9_]");
//         }
//      });
		String useHighlighting = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.FIELD_USE_HIGHLIGHTING);
		
		if(useHighlighting.equals(PreferenceConstants.VALUE_HIGHLIGHTING_TRUE)){
			// TODO get these settings from preferences
			Token keyword   		= new Token(new TextAttribute(ColorManager.INSTANCE.getColor(IColorConstants.KEYWORD),   		  null, SWT.BOLD            ));
			Token value     		= new Token(new TextAttribute(ColorManager.INSTANCE.getColor(IColorConstants.VALUE),     		  null, SWT.BOLD|SWT.ITALIC ));
			Token primitive 		= new Token(new TextAttribute(ColorManager.INSTANCE.getColor(IColorConstants.PRIMITIVE), 		  null, SWT.ITALIC          ));
			Token comment   		= new Token(new TextAttribute(ColorManager.INSTANCE.getColor(IColorConstants.COMMENT),   		  null, SWT.NORMAL          ));
			Token builtinFunction   = new Token(new TextAttribute(ColorManager.INSTANCE.getColor(IColorConstants.BUILTIN_FUNCTION),null, SWT.BOLD            ));
			Token string    		= new Token(new TextAttribute(ColorManager.INSTANCE.getColor(IColorConstants.STRING)                                       ));
//			Token text           = new Token(new TextAttribute(manager.getColor(IColorConstants.DEFAULT),           null, SWT.BOLD|SWT.ITALIC));
			
			WordMatcher keywordRule = new WordMatcher();
			combinedWordRule.addWordMatcher(keywordRule);
	      
			// add tokens for each reserved word
//			rule.addWord("[a-zA-Z_]",   text);
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
			
			keywordRule.addWord("cap",  	   builtinFunction); 
			keywordRule.addWord("close" , 	builtinFunction); 
			keywordRule.addWord("closed", 	builtinFunction); 
			keywordRule.addWord("len", 	   builtinFunction); 
			keywordRule.addWord("make", 	   builtinFunction); 
			keywordRule.addWord("new", 	   builtinFunction); 
			keywordRule.addWord("panic", 	   builtinFunction); 
			keywordRule.addWord("panicln",	builtinFunction); 
			keywordRule.addWord("print", 	   builtinFunction); 
			keywordRule.addWord("println",	builtinFunction); 
			keywordRule.addWord("append",      builtinFunction);
			keywordRule.addWord("copy",      builtinFunction);
	
			keywordRule.addWord("nil",   value);
			keywordRule.addWord("true",  value);
			keywordRule.addWord("false", value);
			keywordRule.addWord("iota",  value);
			
			keywordRule.addWord("uint8",   primitive); // the set of all unsigned  8-bit integers (0 to 255)
			keywordRule.addWord("uint16",  primitive); // the set of all unsigned 16-bit integers (0 to 65535)
			keywordRule.addWord("uint32",  primitive); // the set of all unsigned 32-bit integers (0 to 4294967295)
			keywordRule.addWord("uint64",  primitive); // the set of all unsigned 64-bit integers (0 to 18446744073709551615)
			keywordRule.addWord("int8",    primitive); // the set of all signed  8-bit integers (-128 to 127)
			keywordRule.addWord("int16",   primitive); // the set of all signed 16-bit integers (-32768 to 32767)
			keywordRule.addWord("int32",   primitive); // the set of all signed 32-bit integers (-2147483648 to 2147483647)
			keywordRule.addWord("int64",   primitive); // the set of all signed 64-bit integers (-9223372036854775808 to 9223372036854775807)
			keywordRule.addWord("float32", primitive); // the set of all IEEE-754 32-bit floating-point numbers
			keywordRule.addWord("float64", primitive); // the set of all IEEE-754 64-bit floating-point numbers
			keywordRule.addWord("byte",    primitive); // familiar alias for uint8
			keywordRule.addWord("uint",    primitive); // either 32 or 64 bits
			keywordRule.addWord("int",     primitive); // either 32 or 64 bits
			keywordRule.addWord("uintptr", primitive); // an unsigned integer large enough to store the uninterpreted bits of a pointer value
			keywordRule.addWord("string",  primitive);
			keywordRule.addWord("bool",    primitive);
	
			setRules(new IRule[] {  
					 combinedWordRule,
					 new WhitespaceRule(new IWhitespaceDetector() {
						public boolean isWhitespace(char c) {
							return Character.isWhitespace(c);
						}
					 }), 
					});
		}

	}
	
	/**
	 * 
	 * @author steel
	 * @deprecated
	 */
	class WordDetector implements IWordDetector{
	   KeywordRule keywordRule;
	   StringBuilder buffer = new StringBuilder();
	   
      public boolean isWordStart(char c) {
         if(Character.isWhitespace(c)){
            buffer= new StringBuilder();
            return false;
         }
         else{
            buffer.append(c);
         }
         String s =new String(new char[]{c});            
         return s.matches("[A-Za-z_]");
      }

      public boolean isWordPart(char c) {
         if(Character.isWhitespace(c)){
            buffer = new StringBuilder();
            return false;
         }
         else{
            buffer.append(c);
         }
         String buf = buffer.toString();
         for(Object obj:keywordRule.getFWords().keySet()){
            String word = (String)obj;
            if(word.startsWith(buf)){
               return true;
            }
         }
         return false;
      }
	}
	
	/**
	 * 
	 * @author steel
	 * @deprecated
	 */
	class KeywordRule extends WordRule {

		public KeywordRule(IWordDetector detector) {
			super(detector);
		}

		@SuppressWarnings("rawtypes")
		public Map getFWords() {
			return fWords;
		}
	}
	
}
