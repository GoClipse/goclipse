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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

public abstract class AbstractCharacterReader implements ICharacterReader {
	
	protected int readCount;
	
	public AbstractCharacterReader() {
		super();
	}
	
	@Override
	public int lookahead() {
		int ch = read();
		unread();
		return ch;
	}
	
	@Override
	public int read() {
		readCount++;
		return doRead();
	}
	
	protected abstract int doRead();
	
	@Override
	public void unread() {
		readCount--;
		assertTrue(readCount >= 0);
		doUnread();
	}
	
	protected abstract void doUnread();
	
	@Override
	public void reset() {
		// Note: we reset according to our own read count
		unread(readCount);
	}
	
}