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

public abstract class BasicCharSource<EXC extends Exception> implements IBasicCharSource<EXC> {
	
	protected boolean checkedHasNext = false;
	
	public BasicCharSource() {
		super();
	}
	
	@Override
	public final char consume() throws EXC {
		checkedHasNext = false;
		int next = lookahead();
		consume_next_invariant(next);
		doConsume();
		return (char) next;
	}
	
	protected void consume_next_invariant(int next) {
		assertTrue(next != EOS);
	}
	
	protected abstract void doConsume() throws EXC;
	
	@Override
	public boolean hasCharAhead() throws EXC {
		checkedHasNext = true;
		return IBasicCharSource.super.hasCharAhead();
	}
	
	@Override
	public char nextChar() throws EXC {
		assertTrue(checkedHasNext);
		return IBasicCharSource.super.nextChar();
	}
	
}