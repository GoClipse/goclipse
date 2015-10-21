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

public abstract class OffsetBasedCharacterReader<EXC extends Exception> extends BasicCharSource<EXC> {
	
	protected int readOffset;
	
	public OffsetBasedCharacterReader() {
		super();
	}
	
	public int getReadOffset() {
		return readOffset;
	}
	
	protected void invariant() {
		assertTrue(readOffset >= 0);
	}
	
	@Override
	protected void doConsume() throws EXC {
		readOffset++;
		invariant();
	}
	
	public void unread() throws EXC {
		readOffset--;
		invariant();
		doUnread();
	}
	
	protected abstract void doUnread() throws EXC;
	
	public void reset() throws EXC {
		readOffset = 0;
	}
	
}