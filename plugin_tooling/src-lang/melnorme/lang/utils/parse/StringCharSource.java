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

import java.io.Reader;

import melnorme.utilbox.misc.ToStringHelper;
import melnorme.utilbox.misc.ToStringHelper.ToString;

public class StringCharSource extends OffsetBasedCharacterReader<RuntimeException> 
	implements ICharacterReader, ToString 
{
	
	protected final String source;
	
	public StringCharSource(String source) {
		this.source = assertNotNull(source);
	}
	
	public String getSource() {
		return source;
	}
	
	@Override
	public String toString() {
		return defaultToString();
	}
	
	@Override
	public void toString(ToStringHelper sh) {
		sh.writeElement(getClass().getSimpleName() + " @" + readPosition + " of " + source.length());
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
	
	@Override
	public int bufferedCharCount() {
		if(readPosition >= source.length()) {
			return 0;
		}
		return source.length() - readPosition;
	}
	
	@Override
	protected void doUnread() {
	}
	
	protected String sourceSubString(int startPos, int endPos) {
		return source.substring(readPosition + startPos, readPosition + endPos);
	}
	
	/**
	 * Copy characters from the current position onwards to a buffer.
	 * 
	 * @param buf Buffer to copy data to
	 * @param off Offset in the buffer of the first position to which data should be copied.
	 * @param len Number of characters to copy.
	 * @return the actual number of characters that has been copied.
	 */
	public int copyToBuffer(char [] buf, int off, int len) {
		int numberOfCharactersToRead = bufferedCharCount();
		if (numberOfCharactersToRead > len) {
			numberOfCharactersToRead = len;
		}
		source.getChars(readPosition, readPosition + numberOfCharactersToRead, buf, off);
		readPosition += numberOfCharactersToRead;
		return numberOfCharactersToRead;
	}
	
	/* -----------------  ----------------- */
	
	public StringCharSourceReader toReader() {
		return new StringCharSourceReader(this);
	}
	
	public static class StringCharSourceReader extends Reader implements ToString {
		
		protected StringCharSource charSource;
		
		public StringCharSourceReader(StringCharSource charSource) {
			this.charSource = assertNotNull(charSource);
		}
		
		@Override
		public void close() {
			// No need to close anything
		}

		@Override
		public int read(char[] cbuf, int off, int len) {
			int result = this.charSource.copyToBuffer(cbuf, off, len);
			if (result == 0) {
				return -1; // Indicate that the end of the stream has been reached.
			} else {
				return result;
			}
		}
		
		@Override
		public String toString() {
			return defaultToString();
		}
		
		@Override
		public void toString(ToStringHelper sh) {
			charSource.toString(sh);
		}
	}
	
}