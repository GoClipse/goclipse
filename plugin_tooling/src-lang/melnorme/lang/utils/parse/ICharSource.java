/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.utils.parse;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;


public interface ICharSource<EXC extends Exception> {
	
	int consume() throws EXC;

	int lookahead(int n) throws EXC;
	
	/** @return number of characters available to consume without having to wait for a stream input, 
	 * and with no risk of IOException if consume() is called.
	 * 
	 * Use intended for tests or performance optimizations */
	int bufferedCharCount();
	
	
	default int lookahead() throws EXC {
		return lookahead(0);
	}

	default boolean hasCharAhead() throws EXC {
		return lookahead() != -1;
	}
	
	default char consumeNonEOF() throws EXC {
		int ch = consume();
		assertTrue(ch != -1);
		return (char) ch;
	}
	
	default void consume(int amount) throws EXC {
		while(amount-- > 0) {
			consume();
		}
	}
	
	default boolean lookaheadMatches(String string) throws EXC {
		return lookaheadMatches(string, 0);
	}
	
	default boolean lookaheadMatches(String string, int fromIndex) throws EXC {
		for(int ix = 0; ix < string.length(); ix++) {
			if(lookahead(ix + fromIndex) != string.charAt(ix)) {
				return false;
			}
		}
		return true;
	}
	
}