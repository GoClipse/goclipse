/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Bruno Medeiros - add lookahead method
 *******************************************************************************/
package melnorme.lang.ide.core.text;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.core.runtime.Assert;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.RuleBasedScanner;

/**
 * A buffered rule based scanner. The buffer always contains a section
 * of a fixed size of the document to be scanned. Completely adheres to
 * the contract of <code>RuleBasedScanner</code>.
 */
public class BufferedRuleBasedScannerExt extends RuleBasedScanner {

	/** The default buffer size. Value = 500 */
	protected static final int DEFAULT_BUFFER_SIZE= 500;
	
	/** The actual size of the buffer. Initially set to <code>DEFAULT_BUFFER_SIZE</code> */
	protected int fBufferSize = DEFAULT_BUFFER_SIZE;
	protected char[] fBuffer = new char[DEFAULT_BUFFER_SIZE];

	/** The offset of the document at which the buffer starts */
	protected int fStart;
	/** The offset of the document at which the buffer ends */
	protected int fEnd;
	/** The cached length of the document */
	protected int fDocumentLength;


	/**
	 * Creates a new buffered rule based scanner which does
	 * not have any rule and a default buffer size of 500 characters.
	 */
	protected BufferedRuleBasedScannerExt() {
		super();
	}

	/**
	 * Creates a new buffered rule based scanner which does
	 * not have any rule. The buffer size is set to the given
	 * number of characters.
	 *
	 * @param size the buffer size
	 */
	public BufferedRuleBasedScannerExt(int size) {
		super();
		setBufferSize(size);
	}
	
	public int getOffset() {
		return fOffset;
	}
	
	/**
	 * Sets the buffer to the given number of characters.
	 *
	 * @param size the buffer size
	 */
	protected void setBufferSize(int size) {
		Assert.isTrue(size > 0);
		fBufferSize= size;
		fBuffer= new char[size];
	}

	/**
	 * Shifts the buffer so that the buffer starts at the
	 * given document offset.
	 *
	 * @param offset the document offset at which the buffer starts
	 */
	protected void shiftBuffer(int offset) {

		fStart= offset;
		fEnd= fStart + fBufferSize;
		if (fEnd > fDocumentLength)
			fEnd= fDocumentLength;

		try {

			String content= fDocument.get(fStart, fEnd - fStart);
			content.getChars(0, fEnd - fStart, fBuffer, 0);

		} catch (BadLocationException x) {
		}
	}

	@Override
	public void setRange(IDocument document, int offset, int length) {

		super.setRange(document, offset, length);

		fDocumentLength= document.getLength();
		shiftBuffer(offset);
	}

	@Override
	public int read() {
		assertTrue(fOffset >= 0);
		
		int next = charAt(fOffset);
		fOffset++;
		fColumn = UNDEFINED;
		return next;
	}
	
	public int lookahead(int offset) {
		return charAt(fOffset + offset);
	}
	
	protected int charAt(int offset) {
		
		if(offset >= fRangeEnd) {
			return EOF;
		}
		
		if(offsetIsOutsideBuffer(offset)) {
			shiftBuffer(offset);
		}
		
		return fBuffer[offset - fStart];
	}
	
	public boolean offsetIsOutsideBuffer(int offset) {
		return offset < fStart || offset >= fEnd;
	}
	
	public String lookaheadString(int offset, int length) {
		try {
			assertTrue(fOffset + offset + length <= fRangeEnd);
			
			return fDocument.get(fOffset + offset, length);
		} catch(BadLocationException e) {
			throw assertFail();
		}
	}
	
	@Override
	public void unread() {

		if (fOffset == fStart)
			shiftBuffer(Math.max(0, fStart - (fBufferSize / 2)));

		--fOffset;
		fColumn= UNDEFINED;
		
		assertTrue(fOffset >= 0);
	}
	
}