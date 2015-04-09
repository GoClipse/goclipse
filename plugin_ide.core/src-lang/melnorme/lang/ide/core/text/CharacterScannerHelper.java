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
package melnorme.lang.ide.core.text;

import melnorme.lang.tooling.parser.lexer.ICharacterReader;

import org.eclipse.jface.text.rules.ICharacterScanner;

/**
 * Adapt {@link ICharacterReader} over a {@link ICharacterScanner}
 */
public class CharacterScannerHelper implements ICharacterReader {

	protected final ICharacterScanner scanner;
	protected int readCount;
	
	public CharacterScannerHelper(ICharacterScanner scanner) {
		this.scanner = scanner;
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
		return scanner.read();
	}
	
	@Override
	public void unread() {
		readCount--;
		scanner.unread();
	}
	
	@Override
	public void reset() {
		while(readCount > 0) {
			scanner.unread();
			readCount--;
		}
	}
	
	@Override
	public boolean consume(int character) {
		int ch = read();
		if(ch == character) {
			return true;
		}
		unread();
		return false;
	}
	
}