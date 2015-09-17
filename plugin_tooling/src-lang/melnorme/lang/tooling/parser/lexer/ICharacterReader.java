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

public interface ICharacterReader {
	
	/** @return the next character in the reader stream. */
	int lookahead();
	
	/** @return the next character in the reader stream, incrementing the stream position. */
	int read();
	
	/** Decrement the stream position. Cannot be called more times than {@link #read()} has been called. */
	void unread();
	
	/** Reset scanner position to position before first read. */
	void reset();
	
	/* -----------------  ----------------- */
	
	default char charAhead() throws EndOfStream {
		if(lookahead() == -1) {
			throw new EndOfStream();
		}
		return (char) lookahead();
	}
	
	@SuppressWarnings("serial")
	public static class EndOfStream extends Throwable {
		
	}
	
	/** Read next character if it is equal to given character. @return true if a character was read. */
	default boolean consume(int character) {
		int ch = read();
		if(ch == character) {
			return true;
		}
		unread();
		return false;
	}
	
	default void unread(int count) {
		while(count > 0) {
			unread();
			count--;
		}
	}
	
}