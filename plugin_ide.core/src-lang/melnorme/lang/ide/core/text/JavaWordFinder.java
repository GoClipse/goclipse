/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.text;


import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;

public class JavaWordFinder {

	public static IRegion findWord(IDocument document, int offset) {

		int start= -2;
		int end= -1;

		try {
			int pos= offset;
			char c;

			while (pos >= 0) {
				c= document.getChar(pos);
				if (!Character.isJavaIdentifierPart(c)) {
					// Check for surrogates
					if (Character.isSurrogate(c)) {
						/*
						 * XXX: Here we should create the code point and test whether
						 * it is a Java identifier part. Currently this is not possible
						 * because java.lang.Character in 1.4 does not support surrogates
						 * and because com.ibm.icu.lang.UCharacter.isJavaIdentifierPart(int)
						 * is not correctly implemented.
						 */
					} else {
						break;
					}
				}
				--pos;
			}
			start= pos;

			pos= offset;
			int length= document.getLength();

			while (pos < length) {
				c= document.getChar(pos);
				if (!Character.isJavaIdentifierPart(c)) {
					if (Character.isSurrogate(c)) {
						/*
						 * XXX: Here we should create the code point and test whether
						 * it is a Java identifier part. Currently this is not possible
						 * because java.lang.Character in 1.4 does not support surrogates
						 * and because com.ibm.icu.lang.UCharacter.isJavaIdentifierPart(int)
						 * is not correctly implemented.
						 */
					} else {
						break;
					}

				}
				++pos;
			}
			end= pos;

		} catch (BadLocationException x) {
		}

		if (start >= -1 && end > -1) {
			if (start == offset && end == offset) {
				try {
					char c= document.getChar(offset);
					switch (c) {
						case '-':
							if (document.getChar(offset + 1) == '>') {
								return new Region(offset, 2);
							}
							break;
						case '>':
							if (document.getChar(offset - 1) == '-') {
								return new Region(offset - 1, 2);
							}
							break;
						case ':':
							if (document.getChar(offset + 1) == ':') {
								return new Region(offset, 2);
							} else if (document.getChar(offset - 1) == ':') {
								return new Region(offset - 1, 2);
							}
							break;
					}
				} catch (BadLocationException e) {
				}
				return new Region(offset, 0);
			} else if (start == offset) {
				return new Region(start, end - start); //XXX: probably unused...
			} else {
				return new Region(start + 1, end - start - 1);
			}
		}

		return null;
	}
}
