package LANG_PROJECT_ID.ide.ui.text;

import java.util.ArrayList;
import java.util.List;

import melnorme.lang.ide.ui.text.coloring.AbstractLangScanner;

import org.eclipse.cdt.ui.text.ITokenStoreFactory;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

/**
 * Sample LANGUAGE code scanner
 */
public class LANGUAGE_CodeScanner extends AbstractLangScanner {
	
	private static String tokenPrefProperties[] = new String[] {
		LANGUAGE_ColorPreferences.DEFAULT.key,
		LANGUAGE_ColorPreferences.KEYWORDS.key,
		LANGUAGE_ColorPreferences.KEYWORDS_VALUES.key,
		LANGUAGE_ColorPreferences.OPERATORS.key
	};
	
	public LANGUAGE_CodeScanner(ITokenStoreFactory factory) {
		super(factory.createTokenStore(tokenPrefProperties));
		setRules(createRules());
	}
	
	protected List<IRule> createRules() {
		List<IRule> rules = new ArrayList<IRule>();
		
		IToken tkOther = getToken(LANGUAGE_ColorPreferences.DEFAULT.key);
		IToken tkKeywords = getToken(LANGUAGE_ColorPreferences.KEYWORDS.key);
		IToken tkKeywordValues = getToken(LANGUAGE_ColorPreferences.KEYWORDS_VALUES.key);
		IToken tkOperators = getToken(LANGUAGE_ColorPreferences.OPERATORS.key);
		
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
	
}