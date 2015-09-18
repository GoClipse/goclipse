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

public class StringParseSource extends OffsetBasedCharacterReader<RuntimeException> implements ICharacterReader {
	
	protected final String source;
	
	public StringParseSource(String source) {
		this.source = source;
	}
	
	public String getSource() {
		return source;
	}
	
	@Override
	public int lookahead(int n) {
		int index = readOffset + n;
		
		if(index >= source.length()) {
			return -1;
		}
		return source.charAt(index);
	}
	
	@Override
	public String lookaheadString(int offset, int length) throws RuntimeException {
		return sourceSubString(offset, offset + length);
	}
	
	protected String sourceSubString(int startPos, int endPos) {
		return source.substring(readOffset + startPos, readOffset + endPos);
	}
	
	@Override
	public int bufferedCharCount() {
		return source.length() - readOffset;
	}
	
	@Override
	public void consume(int amount) throws RuntimeException {
		readOffset += amount;
	}
	
	/* -----------------  ----------------- */
	
	@Override
	protected void doUnread() {
	}
	
}