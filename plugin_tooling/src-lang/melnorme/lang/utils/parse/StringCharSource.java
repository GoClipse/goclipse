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

public class StringCharSource extends OffsetBasedCharacterReader<RuntimeException> implements ICharacterReader {
	
	protected final String source;
	
	public StringCharSource(String source) {
		this.source = source;
	}
	
	public String getSource() {
		return source;
	}
	
	/**
	 * @return character from current read position, plus given offset (offset can be negative). 
	 * EOF if resulting position is out of bounds.
	 */
	@Override
	public int lookahead(int offset) {
		int index = readPosition + offset;
		
		if(index < 0 || index >= source.length()) {
			return EOS;
		}
		return source.charAt(index);
	}
	
	@Override
	public String lookaheadString(int offset, int length) throws RuntimeException {
		return sourceSubString(offset, offset + length);
	}
	
	protected String sourceSubString(int startPos, int endPos) {
		return source.substring(readPosition + startPos, readPosition + endPos);
	}
	
	@Override
	public int bufferedCharCount() {
		return source.length() - readPosition;
	}
	
	@Override
	public void consume(int amount) throws RuntimeException {
		readPosition += amount;
		assertTrue(readPosition <= source.length());
	}
	
	/* -----------------  ----------------- */
	
	@Override
	protected void doUnread() {
	}
	
}