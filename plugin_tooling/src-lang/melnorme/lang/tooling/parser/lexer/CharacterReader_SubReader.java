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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import melnorme.lang.utils.parse.ICharSource;
import melnorme.lang.utils.parse.ICharacterReader;
import melnorme.lang.utils.parse.OffsetBasedCharacterReader;

public class CharacterReader_SubReader extends OffsetBasedCharacterReader<RuntimeException> 
	implements ICharacterReader {
	
	protected final ICharacterReader reader;
	
	public CharacterReader_SubReader(ICharacterReader reader) {
		this.reader = assertNotNull(reader);
	}
	
	public ICharSource<RuntimeException> getParentReader() {
		return reader;
	}
	
	@Override
	public int lookahead(int offset) throws RuntimeException {
		return reader.lookahead(readOffset + offset);
	}
	
	@Override
	public String lookaheadString(int offset, int length) {
		return reader.lookaheadString(readOffset + offset, length);
	}
	
	@Override
	public int bufferedCharCount() {
		return Math.max(0, reader.bufferedCharCount() - readOffset);
	}
	
	@Override
	protected void doUnread() {
	}
	
	public void consumeInParentReader() {
		reader.consume(readOffset);
		readOffset = 0;
	}
	
}