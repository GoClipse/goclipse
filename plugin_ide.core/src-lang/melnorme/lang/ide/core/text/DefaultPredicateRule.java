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
package melnorme.lang.ide.core.text;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public abstract class DefaultPredicateRule implements IPredicateRule {

	protected IToken token;
	
	public DefaultPredicateRule(IToken token) {
		this.token = assertNotNull(token);
	}
	
	@Override
	public IToken getSuccessToken() {
		return token;
	}
	
	@Override
	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		if(resume) {
			return Token.UNDEFINED; // Not supported
		}
		return evaluate(scanner);
	}
	
	/* ----------------- helpers ----------------- */
	
	public static boolean consume(char ch, ICharacterScanner scanner) {
		int next = scanner.read();
		if(next == ch) {
			return true;
		}
		scanner.unread();
		return false;
	}
	
	protected IToken currentOr(char altA, ICharacterScanner scanner) {
		int second = scanner.read();
		if(second == altA) {
			return getSuccessToken();
		}
		
		scanner.unread(); 
		return getSuccessToken();
	}
	
	protected IToken currentOr(char altA, char altB, ICharacterScanner scanner) {
		int second = scanner.read();
		if(second == altA || second == altB) {
			return getSuccessToken();
		}
		
		scanner.unread(); 
		return getSuccessToken();
	}
	
}