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
package melnorme.lang.tooling.parser.lexer;

import melnorme.lang.utils.parse.ICharacterReader;
import melnorme.utilbox.collections.HashMap2;

public class WordLexerRule<TOKEN> implements ILexingRule2<TOKEN> {
	
	protected final TOKEN whitespaceToken;
	protected final TOKEN defaultWordToken;
	protected final HashMap2<String, TOKEN> tokenMap = new HashMap2<>();
	
	public WordLexerRule(TOKEN whitespaceToken, TOKEN defaultWordToken) {
		this.whitespaceToken = whitespaceToken;
		this.defaultWordToken = defaultWordToken;
	}
	
	public void addKeywords(TOKEN token, String[] keywords) {
		for(String keyword : keywords) {
			addWord(keyword, token);
		}
	}
	
	public void addWord(String keyword, TOKEN token) {
		tokenMap.put(keyword, token);
	}
	
	@Override
	public TOKEN doEvaluate(ICharacterReader reader) {
		if(LexingUtils.skipWhitespace(reader) > 0) {
			return whitespaceToken;
		}
		
		String word = LexingUtils.readJavaIdentifier(reader);
		if(word.isEmpty()) {
			return null;
		}
		
		TOKEN keywordToken = tokenMap.get(word);
		return (keywordToken == null) ? defaultWordToken : keywordToken;
	}
	
}