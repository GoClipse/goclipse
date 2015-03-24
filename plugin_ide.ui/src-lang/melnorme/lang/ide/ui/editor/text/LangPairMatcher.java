/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Christian Plesner Hansen (plesner@quenta.org) - changed implementation to use DefaultCharacterPairMatcher
 *******************************************************************************/
package melnorme.lang.ide.ui.editor.text;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import melnorme.lang.ide.core.TextSettings_Actual;
import melnorme.utilbox.ownership.IDisposable;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.DefaultCharacterPairMatcher;

/**
 * Helper class for match pairs of characters.
 */
public final class LangPairMatcher extends DefaultCharacterPairMatcher implements IDisposable{
	
	protected final char[] pairs;
	
	public LangPairMatcher(char[] pairs) {
		super(pairs, TextSettings_Actual.PARTITIONING_ID, true);
		this.pairs = assertNotNull(pairs);
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
	
	/* -----------------  ----------------- */
	
	private boolean isOpeningBracket(char character) {
		for (int i= 0; i < pairs.length; i+= 2) {
			if (character == pairs[i])
				return true;
		}
		return false;
	}
	
	private boolean isClosingBracket(char character) {
		for (int i= 1; i < pairs.length; i+= 2) {
			if (character == pairs[i])
				return true;
		}
		return false;
	}
	
	/*
	 * Copy of org.eclipse.jface.text.source.DefaultCharacterPairMatcher.getOffsetAdjustment(IDocument, int, int)
	 */
	public int getOffsetAdjustment(IDocument document, int offset, int length) {
		if (length == 0 || Math.abs(length) > 1)
			return 0;
		try {
			if (length < 0) {
				if (isOpeningBracket(document.getChar(offset))) {
					return 1;
				}
			} else {
				if (isClosingBracket(document.getChar(offset - 1))) {
					return -1;
				}
			}
		} catch (BadLocationException e) {
			//do nothing
		}
		return 0;
	}
	
}
