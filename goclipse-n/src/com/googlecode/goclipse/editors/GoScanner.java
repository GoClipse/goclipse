package com.googlecode.goclipse.editors;

import java.util.Map;

import org.eclipse.jdt.internal.ui.text.CombinedWordRule;
import org.eclipse.jdt.internal.ui.text.CombinedWordRule.WordMatcher;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

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
	    IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
		String useHighlighting = prefStore.getString(PreferenceConstants.FIELD_USE_HIGHLIGHTING);
		
		if(useHighlighting.equals(PreferenceConstants.VALUE_HIGHLIGHTING_TRUE)){
			Color keywordColor         = ColorManager.INSTANCE.getColor(PreferenceConverter.getColor(prefStore, PreferenceConstants.FIELD_SYNTAX_KEYWORD_COLOR         ));
			Color valueColor           = ColorManager.INSTANCE.getColor(PreferenceConverter.getColor(prefStore, PreferenceConstants.FIELD_SYNTAX_VALUE_COLOR           ));
			Color primitiveColor       = ColorManager.INSTANCE.getColor(PreferenceConverter.getColor(prefStore, PreferenceConstants.FIELD_SYNTAX_PRIMITIVE_COLOR       ));
			//Color commentColor         = ColorManager.INSTANCE.getColor(PreferenceConverter.getColor(prefStore, PreferenceConstants.FIELD_SYNTAX_COMMENT_COLOR         ));  // Used in com.googlecode.goclipse.editors.Configuration
			Color builtinFunctionColor = ColorManager.INSTANCE.getColor(PreferenceConverter.getColor(prefStore, PreferenceConstants.FIELD_SYNTAX_BUILTIN_FUNCTION_COLOR));
			//Color stringColor          = ColorManager.INSTANCE.getColor(PreferenceConverter.getColor(prefStore, PreferenceConstants.FIELD_SYNTAX_STRING_COLOR          ));  // Used in com.googlecode.goclipse.editors.Configuration
			
			int keywordStyle         = prefStore.getInt(PreferenceConstants.FIELD_SYNTAX_KEYWORD_STYLE         );
			int valueStyle           = prefStore.getInt(PreferenceConstants.FIELD_SYNTAX_VALUE_STYLE           );
			int primitiveStyle       = prefStore.getInt(PreferenceConstants.FIELD_SYNTAX_PRIMITIVE_STYLE       );
			//int commentStyle         = prefStore.getInt(PreferenceConstants.FIELD_SYNTAX_COMMENT_STYLE         ); // Used in com.googlecode.goclipse.editors.Configuration
			int builtinFunctionStyle = prefStore.getInt(PreferenceConstants.FIELD_SYNTAX_BUILTIN_FUNCTION_STYLE);
			//int stringStyle          = prefStore.getInt(PreferenceConstants.FIELD_SYNTAX_STRING_STYLE          ); // Used in com.googlecode.goclipse.editors.Configuration
						
			Token keyword   		= new Token(new TextAttribute(keywordColor,         null, keywordStyle        ));
			Token value     		= new Token(new TextAttribute(valueColor,           null, valueStyle          ));
			Token primitive 		= new Token(new TextAttribute(primitiveColor,       null, primitiveStyle      ));
			//Token comment   		= new Token(new TextAttribute(commentColor,         null, commentStyle        )); // Used in com.googlecode.goclipse.editors.Configuration
			Token builtinFunction   = new Token(new TextAttribute(builtinFunctionColor, null, builtinFunctionStyle));
			//Token string    		= new Token(new TextAttribute(stringColor,          null, stringStyle         )); // Used in com.googlecode.goclipse.editors.Configuration
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
			keywordRule.addWord("close" , 	   builtinFunction); 
			keywordRule.addWord("closed", 	   builtinFunction); 
			keywordRule.addWord("len", 	       builtinFunction); 
			keywordRule.addWord("make", 	   builtinFunction); 
			keywordRule.addWord("new", 	       builtinFunction); 
			keywordRule.addWord("panic", 	   builtinFunction); 
			keywordRule.addWord("panicln",	   builtinFunction); 
			keywordRule.addWord("print", 	   builtinFunction); 
			keywordRule.addWord("println",	   builtinFunction); 
			keywordRule.addWord("append",      builtinFunction);
			keywordRule.addWord("copy",        builtinFunction);
	
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
