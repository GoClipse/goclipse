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
package melnorme.lang.ide.core_text;

import melnorme.lang.utils.parse.BasicCharSource;
import melnorme.lang.utils.parse.ICharSource;
import melnorme.lang.utils.parse.ICharacterReader;

public class CharacterScanner_ReaderHelper extends BasicCharSource<RuntimeException> 
	implements ICharacterReader, ICharSource<RuntimeException> {
	
	protected final BufferedRuleBasedScannerExt scanner;
	
	public CharacterScanner_ReaderHelper(BufferedRuleBasedScannerExt scanner) {
		this.scanner = scanner;
	}
	
	@Override
	public int lookahead(int offset) {
		return scanner.lookahead(offset);
	}
	
	@Override
	public String lookaheadString(int offset, int length) {
		return scanner.lookaheadString(offset, length);
	}
	
	@Override
	public int bufferedCharCount() throws RuntimeException {
		if(scanner.offsetIsOutsideBuffer(scanner.getOffset())) {
			return 0;
		}
		return scanner.fEnd - scanner.getOffset();
	}
	
	@Override
	protected void doConsume() {
		scanner.read();
	}
	
	@Override
	public void unread() throws RuntimeException {
		scanner.unread();
	}
	
}