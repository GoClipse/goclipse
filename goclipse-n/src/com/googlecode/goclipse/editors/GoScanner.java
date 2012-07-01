package com.googlecode.goclipse.editors;

import java.util.Map;

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
import com.googlecode.goclipse.editors.CombinedWordRule.WordMatcher;
import com.googlecode.goclipse.preferences.PreferenceConstants;

public class GoScanner extends RuleBasedScanner {

	public GoScanner() {
	   Token text = new Token(new TextAttribute( ColorManager.INSTANCE.getColor(IColorConstants.DEFAULT), null, SWT.NONE));
	   CombinedWordRule combinedWordRule = new CombinedWordRule(new IWordDetector() {
         @Override
        public boolean isWordStart(char c) {
            String s =new String(new char[]{c});
            
            return s.matches("[A-Za-z_]");
         }

         @Override
        public boolean isWordPart(char c) {
            String s =new String(new char[]{c});
            return s.matches("[A-Za-z0-9_]");
         }
      }, text);
	   
	    IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
		boolean useHighlighting = prefStore.getBoolean(PreferenceConstants.FIELD_USE_HIGHLIGHTING);
		
		if(useHighlighting){
			final Color keywordColor         = ColorManager.INSTANCE.getColor(PreferenceConverter.getColor(prefStore, PreferenceConstants.FIELD_SYNTAX_KEYWORD_COLOR         ));
			final Color valueColor           = ColorManager.INSTANCE.getColor(PreferenceConverter.getColor(prefStore, PreferenceConstants.FIELD_SYNTAX_VALUE_COLOR           ));
			final Color primitiveColor       = ColorManager.INSTANCE.getColor(PreferenceConverter.getColor(prefStore, PreferenceConstants.FIELD_SYNTAX_PRIMITIVE_COLOR       ));
			final Color builtinFunctionColor = ColorManager.INSTANCE.getColor(PreferenceConverter.getColor(prefStore, PreferenceConstants.FIELD_SYNTAX_BUILTIN_FUNCTION_COLOR));
			//final Color textColor            = ColorManager.INSTANCE.getColor(PreferenceConverter.getColor(prefStore, PreferenceConstants.FIELD_SYNTAX_TEXT_COLOR));
			// TODO: added to fix a compilation problem
			final Color textColor            = ColorManager.INSTANCE.getColor(IColorConstants.DEFAULT);
			
			final int keywordStyle         = prefStore.getInt(PreferenceConstants.FIELD_SYNTAX_KEYWORD_STYLE         );
			final int valueStyle           = prefStore.getInt(PreferenceConstants.FIELD_SYNTAX_VALUE_STYLE           );
			final int primitiveStyle       = prefStore.getInt(PreferenceConstants.FIELD_SYNTAX_PRIMITIVE_STYLE       );
			final int builtinFunctionStyle = prefStore.getInt(PreferenceConstants.FIELD_SYNTAX_BUILTIN_FUNCTION_STYLE);
			//final int textStyle            = prefStore.getInt(PreferenceConstants.FIELD_SYNTAX_TEXT_STYLE);
			// TODO: added to fix a compilation problem
			final int textStyle            = 0;
			
			Token keyword   		= new Token(new TextAttribute(keywordColor,         null, keywordStyle        ));
			Token value     		= new Token(new TextAttribute(valueColor,           null, valueStyle          ));
			Token primitive 		= new Token(new TextAttribute(primitiveColor,       null, primitiveStyle      ));
			Token builtinFunction   = new Token(new TextAttribute(builtinFunctionColor, null, builtinFunctionStyle));
			Token textToken         = new Token(new TextAttribute(textColor,            null, textStyle));
			
			final WordMatcher keywordRule = new WordMatcher();
			combinedWordRule.addWordMatcher(keywordRule);
			
			this.setDefaultReturnToken(textToken);
			
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
			
			keywordRule.addWord("append",      builtinFunction);
			keywordRule.addWord("cap",  	   builtinFunction);
			keywordRule.addWord("close" , 	   builtinFunction);
			keywordRule.addWord("complex",     builtinFunction);
			keywordRule.addWord("copy",        builtinFunction);
			keywordRule.addWord("delete",      builtinFunction);
			keywordRule.addWord("imag",        builtinFunction);
			keywordRule.addWord("len", 	       builtinFunction);
			keywordRule.addWord("make", 	   builtinFunction);
			keywordRule.addWord("new", 	       builtinFunction);
			keywordRule.addWord("panic", 	   builtinFunction);
			keywordRule.addWord("print", 	   builtinFunction);
			keywordRule.addWord("println",	   builtinFunction);
			keywordRule.addWord("real",        builtinFunction);
			keywordRule.addWord("recover",     builtinFunction);
				
			keywordRule.addWord("nil",   value);
			keywordRule.addWord("true",  value);
			keywordRule.addWord("false", value);
			keywordRule.addWord("iota",  value);
			
			keywordRule.addWord("uint8",      primitive); // the set of all unsigned  8-bit integers (0 to 255)
			keywordRule.addWord("uint16",     primitive); // the set of all unsigned 16-bit integers (0 to 65535)
			keywordRule.addWord("uint32",     primitive); // the set of all unsigned 32-bit integers (0 to 4294967295)
			keywordRule.addWord("uint64",     primitive); // the set of all unsigned 64-bit integers (0 to 18446744073709551615)
			keywordRule.addWord("int8",       primitive); // the set of all signed  8-bit integers (-128 to 127)
			keywordRule.addWord("int16",      primitive); // the set of all signed 16-bit integers (-32768 to 32767)
			keywordRule.addWord("int32",      primitive); // the set of all signed 32-bit integers (-2147483648 to 2147483647)
			keywordRule.addWord("int64",      primitive); // the set of all signed 64-bit integers (-9223372036854775808 to 9223372036854775807)
			keywordRule.addWord("float32",    primitive); // the set of all IEEE-754 32-bit floating-point numbers
			keywordRule.addWord("float64",    primitive); // the set of all IEEE-754 64-bit floating-point numbers
			keywordRule.addWord("complex64",  primitive);
			keywordRule.addWord("complex128", primitive);
			keywordRule.addWord("rune",       primitive);
			keywordRule.addWord("byte",       primitive); // familiar alias for uint8
			keywordRule.addWord("uint",       primitive); // either 32 or 64 bits
			keywordRule.addWord("int",        primitive); // either 32 or 64 bits
			keywordRule.addWord("uintptr",    primitive); // an unsigned integer large enough to store the uninterpreted bits of a pointer value
			keywordRule.addWord("string",     primitive);
			keywordRule.addWord("bool",       primitive);
			keywordRule.addWord("error",      primitive);
			
			setRules(new IRule[] {
					 combinedWordRule,
					 new WhitespaceRule(new IWhitespaceDetector() {
						@Override
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
	   
      @Override
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

      @Override
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
