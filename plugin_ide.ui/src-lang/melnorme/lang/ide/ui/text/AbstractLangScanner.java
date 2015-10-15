/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.text;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;

import melnorme.lang.ide.core.text.BufferedRuleBasedScannerExt;
import melnorme.lang.ide.core.text.CharacterScanner_ReaderHelper;
import melnorme.lang.ide.ui.text.coloring.ILangTokenScanner;
import melnorme.lang.ide.ui.text.coloring.ThemedTextStylingPreference;
import melnorme.lang.ide.ui.text.coloring.TokenRegistry;
import melnorme.lang.tooling.parser.lexer.ILexingRule2;
import melnorme.utilbox.collections.ArrayList2;

public abstract class AbstractLangScanner extends BufferedRuleBasedScannerExt implements ILangTokenScanner {
	
	protected final TokenRegistry tokenRegistry;
	
	public AbstractLangScanner(TokenRegistry tokenRegistry) {
		this.tokenRegistry = assertNotNull(tokenRegistry);
		
		ArrayList2<IRule> arrayList2 = new ArrayList2<>();
		initRules(arrayList2);
		setRules(arrayList2.toArray(IRule.class));
	}
	
	public IToken getToken(ThemedTextStylingPreference stylingPref) {
		return tokenRegistry.getToken(stylingPref);
	}
	
	/* -----------------  ----------------- */
	
	protected abstract void initRules(ArrayList2<IRule> rules);
	
	/* -----------------  ----------------- */
	
	@Override
	public IToken nextToken() {
//		tokenStore.ensureTokensInitialised();
		return doNextToken();
	}
	
	protected IToken doNextToken() {
		fTokenOffset= fOffset;
		fColumn= UNDEFINED;

		for(int i = 0; i < fRules.length; i++) {
			fOffset = fTokenOffset; // Revert position changes that rules might have changed
			
			IToken token = fRules[i].evaluate(this);
			if(!token.isUndefined()) {
				return token; 
			}
		}
		
		if (read() == EOF)
			return Token.EOF;
		return fDefaultReturnToken;
	}
	
	/* -----------------  ----------------- */
	
	public class LexingRule_RuleAdapter implements IRule {
		
		protected final ILexingRule2<IToken> codeLexerRule;
		
		public LexingRule_RuleAdapter(ILexingRule2<IToken> codeLexerRule) {
			this.codeLexerRule = codeLexerRule;
		}
		
		@Override
		public IToken evaluate(ICharacterScanner scanner) {
			AbstractLangScanner langScanner = AbstractLangScanner.this;
			assertTrue(scanner == langScanner);
			
			CharacterScanner_ReaderHelper readerHelper = new CharacterScanner_ReaderHelper(langScanner);
			IToken token = codeLexerRule.evaluateToken(readerHelper);
			if(token == null) {
				return Token.UNDEFINED;
			}
			return token;
		}
		
	}
	
	/* ----------------- Helpers ----------------- */
	
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