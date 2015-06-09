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

import java.io.IOException;


public interface IParseSource {

	int consume() throws IOException;

	int lookahead(int n) throws IOException;
	
	/** @return number of characters available to consume without having to wait for a stream input, 
	 * and with no risk of IOException if consume() is called.
	 * 
	 * Use intended for tests or performance optimizations */
	int bufferedCharCount();
	
	
	default int lookahead() throws IOException {
		return lookahead(0);
	}

	default boolean hasCharAhead() throws IOException {
		return lookahead() != -1;
	}
	
	default char consumeNonEOF() throws IOException {
		int ch = consume();
		assertTrue(ch != -1);
		return (char) ch;
	}
	
	default void consume(int amount) throws IOException {
		while(amount-- > 0) {
			consume();
		}
	}
	
	default boolean lookaheadMatches(String string) throws IOException {
		for(int ix = 0; ix < string.length(); ix++) {
			if(lookahead(ix) != string.charAt(ix)) {
				return false;
			}
		}
		return true;
	}
	
}