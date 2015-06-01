/*******************************************************************************
 * Copyright (c) 2000, 2009 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     QNX Software Systems - Initial API and implementation
 *     Sergey Prigogin (Google)
 *******************************************************************************/
package melnorme.lang.ide.ui.text.util;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;

// Originally based on org.eclipse.cdt.internal.ui.text.CWordFinder
/**
 * This is a helper class for the text editor to be able to determine, given a
 * particular offset in a document, various candidates segments for things like
 * context help, proposals and hovering.
 */
public class WordFinder {
	
	/**
	 * This method determines for a given offset into a given document what the
	 * region is which defines the current word. A word is defined as a contiguous
	 * sequence of C-identifier characters. So assuming that | indicates the current
	 * cursor position:
	 * <pre>
	 *   |afunction(int a, int b) --> word = afunction
	 *   afunc|tion(int a, int b) --> word = afunction
	 *   afunction|(int a, int b) --> word = afunction
	 *   afunction(|int a, int b) --> word = int
	 *   afunction(int a,| int b) --> word = length 0
	 *   afunction(|)             --> word = length 0
	 * </pre>
	 * 
	 * @param document
	 *            The document to be examined
	 * @param offset
	 *            The offset into the document where a word should be
	 *            identified.
	 * @return The region defining the current word, which may be a region of
	 *         length 0 if the offset is not in a word, or null if there is an
	 *         error accessing the document data.
	 */
	public static IRegion findWord(IDocument document, int offset) {
		int start = -2;
		int end = -1;

		try {
			int pos = offset;
			char c;

			while (--pos >= 0) {
				c = document.getChar(pos);
				if (!Character.isJavaIdentifierPart(c)) {
					break;
				}
			}

			start = pos;

			pos = offset;
			int length = document.getLength();

			while (pos < length) {
				c = document.getChar(pos);
				if (!Character.isJavaIdentifierPart(c))
					break;
				++pos;
			}

			end = pos;
		} catch (BadLocationException x) {
		}

		if (start >= -1 && end > -1) {
			if (start == offset && end == offset)
				return new Region(offset, 0);
			else if (start == offset)
				return new Region(start, end - start);
			else
				return new Region(start + 1, end - start - 1);
		}

		return null;
	}

}
