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


public class StringParseSource implements IParseSource {
	
	protected final String source;
	protected int sourceIndex = 0;
	
	public StringParseSource(String source) {
		this.source = source;
	}
	
	@Override
	public int consume() {
		if(sourceIndex >= source.length()) {
			return -1;
		}
		return source.charAt(sourceIndex++);
	}
	
	@Override
	public int lookahead(int n) {
		int index = sourceIndex + n;
		
		if(index >= source.length()) {
			return -1;
		}
		return source.charAt(index);
	}
	
	@Override
	public int bufferedCharCount() {
		return source.length() - sourceIndex;
	}
	
}