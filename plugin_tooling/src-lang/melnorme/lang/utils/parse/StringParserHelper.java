/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.utils.parse;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import java.io.IOException;


public class StringParserHelper extends StringParseSource implements ParseSourceUtils {
	
	public StringParserHelper(String source) {
		super(source);
	}
	
	public int getOffset() {
		return sourceIndex;
	}
	
	@Override
	public boolean lookaheadIsEOF() {
		try {
			return ParseSourceUtils.super.lookaheadIsEOF();
		} catch(IOException e) {
			throw assertFail();
		}
	}
	
	@Override
	public boolean tryConsume(String string) {
		try {
			return ParseSourceUtils.super.tryConsume(string);
		} catch(IOException e) {
			throw assertFail();
		}
	}
	
	@Override
	public String consumeUntil(String string) {
		try {
			return ParseSourceUtils.super.consumeUntil(string);
		} catch(IOException e) {
			throw assertFail();
		}
	}
	
	@Override
	public String consumeUntil(String endString, boolean consumeEndString) {
		try {
			return ParseSourceUtils.super.consumeUntil(endString, consumeEndString);
		} catch(IOException e) {
			throw assertFail();
		}
	}
	
	@Override
	public String consumeDelimitedString(int delimiter, int escapeChar) {
		try {
			return ParseSourceUtils.super.consumeDelimitedString(delimiter, escapeChar);
		} catch(IOException e) {
			throw assertFail();
		}
	}

}