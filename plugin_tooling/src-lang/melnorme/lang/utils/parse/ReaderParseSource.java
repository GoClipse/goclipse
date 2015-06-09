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

import java.io.IOException;
import java.io.Reader;

public class ReaderParseSource implements IParseSource {
	
	protected final StringBuilder buffer = new StringBuilder();
	protected final Reader reader;
	
	public ReaderParseSource(Reader reader) {
		this.reader = reader;
	}
	
	@Override
	public int consume() throws IOException {
		if(buffer.length() == 0) {
			return reader.read();
		}
		
		char ch = buffer.charAt(0);
		buffer.deleteCharAt(0);
		return ch;
	}
	
	@Override
	public int lookahead(int n) throws IOException {
		while(buffer.length() < n + 1) {
			int ch = pushChar();
			if(ch == -1) {
				return -1;
			}
		}
		
		return buffer.charAt(n);
	}
	
	protected int pushChar() throws IOException {
		int read = reader.read();
		if(read == -1) {
			return -1;
		}
		buffer.append((char) read); // Convert to char!
		return read;
	}
	
	@Override
	public int bufferedCharCount() {
		return buffer.length();
	}
	
}