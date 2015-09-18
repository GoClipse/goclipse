/*******************************************************************************
 * Copyright (c) 2014, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package LANG_PROJECT_ID.ide.ui.text;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

import _org.eclipse.cdt.ui.text.ITokenStoreFactory;
import melnorme.lang.ide.ui.text.AbstractLangScanner;
import melnorme.utilbox.collections.ArrayList2;

/**
 * Sample LANGUAGE code scanner
 */
public class LANGUAGE_CodeScanner extends AbstractLangScanner {
	
	private static String tokenPrefProperties[] = new String[] {
		LANGUAGE_ColorPreferences.DEFAULT.key,
		LANGUAGE_ColorPreferences.KEYWORDS.key,
		LANGUAGE_ColorPreferences.KEYWORDS_VALUES.key,
	};
	
	public LANGUAGE_CodeScanner(ITokenStoreFactory factory) {
		super(factory.createTokenStore(tokenPrefProperties));
	}
	
	@Override
	protected void initRules(ArrayList2<IRule> rules) {
		
		IToken tkOther = getToken(LANGUAGE_ColorPreferences.DEFAULT.key);
		IToken tkKeywords = getToken(LANGUAGE_ColorPreferences.KEYWORDS.key);
		IToken tkKeywordValues = getToken(LANGUAGE_ColorPreferences.KEYWORDS_VALUES.key);
		
		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new LangWhitespaceDetector()));
		
		WordRule wordRule = new WordRule(new JavaWordDetector(), tkOther);
		
		wordRule.addWord("keyword",  tkKeywords);

		wordRule.addWord("null", tkKeywordValues);
		wordRule.addWord("true", tkKeywordValues);
		wordRule.addWord("false", tkKeywordValues);
		
		
		rules.add(wordRule);
		
		setDefaultReturnToken(tkOther);
	}
	
}