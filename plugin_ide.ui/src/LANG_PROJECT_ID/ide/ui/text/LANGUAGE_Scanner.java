package LANG_PROJECT_ID.ide.ui.text;

import java.util.ArrayList;
import java.util.List;

import melnorme.lang.ide.ui.text.coloring.AbstractLangScanner;

import org.eclipse.cdt.ui.text.ITokenStoreFactory;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

/**
 * Sample LANGUAGE code scanner
 */
public class LANGUAGE_Scanner extends AbstractLangScanner {
	
	private static String tokenPrefProperties[] = new String[] {
		LANGUAGE_ColorConstants.CODE__DEFAULT,
		LANGUAGE_ColorConstants.CODE__KEYWORDS,
		LANGUAGE_ColorConstants.CODE__KEYWORD_VALUES,
		LANGUAGE_ColorConstants.CODE__OPERATORS
	};
	
	public LANGUAGE_Scanner(ITokenStoreFactory factory) {
		super(factory.createTokenStore(tokenPrefProperties));
		setRules(createRules());
	}
	
	protected List<IRule> createRules() {
		List<IRule> rules = new ArrayList<IRule>();
		
		IToken tkOther = getToken(LANGUAGE_ColorConstants.CODE__DEFAULT);
		IToken tkKeywords = getToken(LANGUAGE_ColorConstants.CODE__KEYWORDS);
		IToken tkKeywordValues = getToken(LANGUAGE_ColorConstants.CODE__KEYWORD_VALUES);
		IToken tkOperators = getToken(LANGUAGE_ColorConstants.CODE__OPERATORS);
		
		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new LangWhitespaceDetector()));
		
		WordRule wordRule = new WordRule(new JavaWordDetector(), tkOther);
		
		wordRule.addWord("keyword",  tkKeywords);

		wordRule.addWord("null", tkKeywordValues);
		wordRule.addWord("true", tkKeywordValues);
		wordRule.addWord("false", tkKeywordValues);
		
		
		wordRule.addWord("==",  tkOperators);
		wordRule.addWord("!=",  tkOperators);
		
		rules.add(wordRule);
		
		setDefaultReturnToken(tkOther);
		return rules;
	}
	
	public static class LangWhitespaceDetector implements IWhitespaceDetector {
		@Override
		public boolean isWhitespace(char character) {
			return Character.isWhitespace(character);
		}
	}
	
	public static class JavaWordDetector implements IWordDetector {
		
		@Override
		public boolean isWordPart(char character) {
			return Character.isJavaIdentifierPart(character);
		}
		
		@Override
		public boolean isWordStart(char character) {
			return Character.isJavaIdentifierPart(character);
		}
	}
	
}