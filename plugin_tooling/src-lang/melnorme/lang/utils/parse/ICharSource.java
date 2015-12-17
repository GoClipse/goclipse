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
package melnorme.lang.utils.parse;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

public interface ICharSource<EXC extends Exception> extends IBasicCharSource<EXC> {
	
	@Override
	default int lookahead() throws EXC {
		return lookahead(0);
	}
	
	int lookahead(int offset) throws EXC;
	
	/** @return number of characters available that can be consumed without having to wait for external input, 
	 * and with no risk exceptions being thrown.
	 * 
	 * Use intended for tests or performance optimizations 
	 */
	int bufferedCharCount();
	
	/** Return a substring from the source, starting at given offset, with given length.
	 * Although a default implementation could be provided, this is left for subclasses to implement,
	 * so that an optimized version can be defined. */
	String lookaheadString(int offset, int length) throws EXC;
	
	/* -----------------  Some common helpers  ----------------- */
	
	default void consume(int amount) throws EXC {
		while(amount-- > 0) {
			consume();
		}
	}
	
	default boolean tryConsume(String string) throws EXC {
		for(int ix = 0; ix < string.length(); ix++) {
			if(lookahead(ix) != string.charAt(ix)) {
				return false;
			}
		}
		consume(string.length());
		return true;
	}
	
	default String consumeString(int length) throws EXC {
		String string = lookaheadString(0, length);
		consume(length);
		return string;
	}
	
	default void consumeAhead(String string) throws EXC {
		assertNotNull(string);
		assertTrue(lookaheadMatches(string));
		consume(string.length());
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
	
	/* -----------------  ----------------- */
	
	default String stringUntil(String string) throws EXC {
		int length = 0;
		while(true) {
			int charAtIx = lookahead(length);
			if(charAtIx == EOS || lookaheadMatches(string, length)) {
				break;
			}
			length++;
		}
		return lookaheadString(0, length);
	}
	
	default String consumeUntil(String string) throws EXC {
		String stringUntil = stringUntil(string);
		consume(stringUntil.length());
		return stringUntil;
	}
	
	/**
	 * Consume characters until lookhead position matches given endString
	 * If consumeEndString, also consume endString from source (although this is not included in result)
	 * 
	 * @return the consumed characters until endString 
	 */
	default String consumeUntil(String endString, boolean consumeEndString) throws EXC {
		String firstString = consumeUntil(endString);
		if(consumeEndString) {
			tryConsume(endString);
		}
		return firstString;
	}
	
}