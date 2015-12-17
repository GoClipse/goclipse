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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

public interface IBasicCharSource<EXC extends Exception> {
	
	public static final int EOS = -1;
	
	/** @return the next character in the stream, without advancing the stream. */
	int lookahead() throws EXC;
	
	/** @return the next character in the stream, advancing the stream. */
	char consume() throws EXC;
	
	default char nextChar() throws EXC {
		return consume();
	}
	
	default boolean lookaheadIsEOS() throws EXC {
		return !hasCharAhead();
	}
	
	default boolean hasCharAhead() throws EXC {
		return lookahead() != EOS;
	}
	
	default char lookaheadChar() throws EXC {
		int la = lookahead();
		assertTrue(la != EOS);
		return (char) la;
	}
	
	/* -----------------  ----------------- */
	
	/** Read next character if it is equal to given character. @return true if a character was read. */
	default boolean tryConsume(char character) throws EXC {
		if(lookahead() == character) {
			consume();
			return true;
		}
		return false;
	}
	
	default boolean consumeAny() throws EXC {
		if(hasCharAhead()) {
			consume();
			return true;
		}
		return false;
	}
	
}