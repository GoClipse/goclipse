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

public class CharacterReaderWrapper extends AbstractCharacterReader {

	protected final ICharacterReader reader;
	
	public CharacterReaderWrapper(ICharacterReader reader) {
		this.reader = reader;
	}
	
	public ICharacterReader getParentReader() {
		return reader;
	}
	
	@Override
	public int lookahead() {
		return reader.lookahead();
	}
	
	@Override
	protected int doRead() {
		return reader.read();
	}
	
	@Override
	protected void doUnread() {
		reader.unread();
	}
	
	/** Resets the number of reads called on this CharacterReaderWrapper instance, not the parent. */
	@Override
	public void reset() {
		super.reset();
	}
	
}