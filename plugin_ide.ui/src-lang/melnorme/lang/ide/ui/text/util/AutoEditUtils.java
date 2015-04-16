/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 		Andrey Tarantsov ?
 *******************************************************************************/

package melnorme.lang.ide.ui.text.util;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextUtilities;

/**
 * An utility class that provides helper methods for language-dependant
 * implementations of AutoEditStrategies.
 * 
 * @author Andrey Tarantsov
 */
public class AutoEditUtils {

	private AutoEditUtils() {
		throw new AssertionError("Cannot instantiate utility class"); //$NON-NLS-1$
	}

	/**
	 * Return a pair for the given brace. Ex. '(' for ')', e.t.c.
	 * 
	 * @param b
	 *            input brace
	 * @return peer brace
	 * @author Kalugin Mikhail
	 */
	public static char getBracePair(char b) {
		switch (b) {
		case '(':
			return ')';
		case ')':
			return '(';
		case '[':
			return ']';
		case ']':
			return '[';
		case '{':
			return '}';
		case '}':
			return '{';
		case '\"':
			return '\"';
		case '\'':
			return '\'';
		}
		return b;
	}

	/**
	 * Checks if the given command is an insertion command that inserts a
	 * snippet of text ending with one of the valid end-of-line sequences.
	 * 
	 * @param d
	 * @param c
	 * @return
	 */
	public static boolean isNewLineInsertionCommand(IDocument d, DocumentCommand c) {
		if (c.length > 0 || c.text == null)
			return false;
		String[] legalLineDelimiters = d.getLegalLineDelimiters();
		for (int i = 0; i < legalLineDelimiters.length; i++) {
			if (legalLineDelimiters[i].equals(c.text))
				return true;
		}
		return false;
	}

	/**
	 * Checks if the given command inserts or replaces a single character.
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isSingleCharactedInsertionOrReplaceCommand(DocumentCommand c) {
		return c.length <= 1 && c.text.length() == 1;
	}

	/**
	 * Returns the given number of spaces.
	 * 
	 * @param spaces
	 * @return
	 */
	public static String getNSpaces(int spaces) {
		return getNChars(spaces, ' ');
	}

	/**
	 * Returns <code>count</code> copies of the given character.
	 * 
	 * @param count
	 * @param ch
	 * @return
	 */
	public static String getNChars(int count, char ch) {
		StringBuffer buf = new StringBuffer(count);
		for (int i = 0; i < count; i++)
			buf.append(ch);
		return buf.toString();

	}

//	/**
//	 * Determines if the given document range contains at least one character
//	 * that belongs to one of the given partitions.
//	 * 
//	 * @param document
//	 * @param startOffset
//	 * @param length
//	 * @param partitions
//	 * @param partitioning
//	 * @return
//	 * @throws BadLocationException
//	 */
//	public static boolean rangeContainsPartitions(IDocument document, int startOffset, int length,
//			String[] partitions, String partitioning) throws BadLocationException {
//		for (int offset = startOffset; offset < startOffset + length; offset++) {
//			ITypedRegion region = TextUtilities.getPartition(document, partitioning, offset, true);
//			String rt = region.getType();
//			if (AutoEditUtils.isPartitionOneOf(rt, partitions))
//				return true;
//			offset = region.getOffset() + region.getLength(); // will be
//			// incremented
//			// by loop
//		}
//		return false;
//	}

	/**
	 * Determines whether all characters of the given range belong to the
	 * default partition.
	 * 
	 * @param document
	 * @param startOffset
	 * @param length
	 * @param partitions
	 * @param partitioning
	 * @return
	 * @throws BadLocationException
	 */
	public static boolean rangeIsInsideDefaultPartition(IDocument document, int startOffset,
			int length, String partitioning) throws BadLocationException {
		for (int offset = startOffset; offset < startOffset + length; offset++) {
			ITypedRegion region = TextUtilities.getPartition(document, partitioning, offset, true);
			String rt = region.getType();
			if (rt != IDocument.DEFAULT_CONTENT_TYPE)
				return false;
			offset = region.getOffset() + region.getLength(); // will be
			// incremented
			// by loop
		}
		return true;
	}

	/**
	 * Determines if the given offset is at the very start of line (that is, at
	 * the beginning of the document or immediately after a valid end-of-line
	 * sequence).
	 * 
	 * Assumes that no end-of-line sequence is longer than 2 characters.
	 * 
	 * @param document
	 * @param offset
	 * @return
	 * @throws BadLocationException
	 */
	public static boolean isStartOfLine(IDocument document, int offset) throws BadLocationException {
		String string;
		if (offset >= 2)
			string = document.get(offset - 2, 2);
		else if (offset >= 1)
			string = document.get(offset - 1, 1);
		else
			return true;
		return TextUtilities.endsWith(document.getLegalLineDelimiters(), string) != -1;
	}

	/**
	 * Returns the leading whitespaces and tabs.
	 * 
	 * @param document -
	 *            the document being parsed
	 * @param line -
	 *            the line being searched
	 * @return the leading whitespace
	 * @throws BadLocationException
	 *             in case <code>line</code> is invalid in the document
	 */
	public static String getLineIndent(IDocument document, int line) throws BadLocationException {
		if (line > -1) {
			int start = document.getLineOffset(line);
			int end = start + document.getLineLength(line); // was - 1
			int whiteend = AutoEditUtils.findEndOfWhiteSpace(document, start, end);
			return document.get(start, whiteend - start);
		}
		return ""; //$NON-NLS-1$
	}
	
	public static String getLineIndentOfOffset(IDocument document, int offset) throws BadLocationException {
		return getLineIndent(document, document.getLineOfOffset(offset));
	}
	
	/**
	 * Returns the first offset greater than or equal to <code>offset</code>
	 * and less than <code>end</code> whose character is not a space or tab
	 * character. If no such offset is found, <code>end</code> is returned.
	 * 
	 * @param document
	 *            the document to search in
	 * @param offset
	 *            the offset at which searching start
	 * @param end
	 *            the offset at which searching stops
	 * @return the offset in the specified range whose character is not a space
	 *         or tab
	 * @exception BadLocationException
	 *                if position is an invalid range in the given document
	 */
	public static int findEndOfWhiteSpace(IDocument document, int offset, int end)
			throws BadLocationException {		
//		int docLength = document.getLength();
		while (offset < end) {
//			if (offset >= docLength) {
//				return docLength;
//			}
			char c = document.getChar(offset);
			if (c != ' ' && c != '\t') {
				return offset;
			}
			offset++;
		}
		return end;
	}

	/**
	 * Returns the leading whitespaces and tabs.
	 * 
	 * @param line -
	 *            the line being searched
	 * @return the leading whitespace
	 */
	public static String getLineIndent(String line) {
		int end = line.length();
		int whiteend = end;
		int offset = 0;
		while (offset < end) {
			char c = line.charAt(offset);
			if (c != ' ' && c != '\t') {
				whiteend = offset;
				break;
			}
			offset++;
		}
		return line.substring(0, whiteend);
	}

	/**
	 * Returns the document's text for the given line.
	 * 
	 * @param document
	 *            the document
	 * @param line
	 *            the index of the line to get
	 * @return The line with the specified index from the document.
	 * @throws BadLocationException
	 *             if <b>line</b> is not correct line number
	 */
	public static String getDocumentLine(IDocument document, int line) throws BadLocationException {
		int lineStart = document.getLineOffset(line);
		int lineLength = document.getLineLength(line);
		return document.get(lineStart, lineLength);
	}

	/**
	 * Find line with number less or equal to <code>line</code>, that is not
	 * empty and is not a comment line starting with <code>commentString</code>.
	 * 
	 * @param d
	 *            the document to search in
	 * @param line
	 *            the index of the line to start searching on (must be less than
	 *            the number of lines in the document)
	 * @return The index of the line, or -1 if no such line is found.
	 * @throws BadLocationException
	 */
	public static int getLastNonEmptyLine(IDocument d, int line, String commentString)
			throws BadLocationException {
		if (commentString == null)
			for (int res = line; res >= 0; res--) {
				String str = getDocumentLine(d, res).trim();
				if (str.trim().length() > 0)
					return res;
			}
		else
			for (int res = line; res >= 0; res--) {
				String str = getDocumentLine(d, res).trim();
				if (!str.startsWith(commentString) && str.trim().length() > 0)
					return res;
			}
		return -1;
	}

	/**
	 * Returns the partition type covering the given offset.
	 * 
	 * @param d
	 * @param partitioning
	 *            the partitioning to be used
	 * @param offset
	 * @return
	 * @throws BadLocationException
	 *             if the offset is invalid in the given document
	 */
	public static String getRegionType(IDocument d, String partitioning, int offset)
			throws BadLocationException {
		int p = ((offset == d.getLength()) ? offset - 1 : offset);
		ITypedRegion region = TextUtilities.getPartition(d, partitioning, p, true);
		return region.getType();
	}

//	/**
//	 * Searchs a pair from the given offset, forward of backwards. Doesn't go
//	 * more than maxCharsAway chars away from the given offset.
//	 * 
//	 * @param document
//	 * @param startingOffset
//	 * @param forward
//	 * @param opening
//	 * @param closing
//	 * @param skipCommentLines
//	 * @param skipStrings
//	 * @return offset of the matched pair character, or <code>-1</code> if
//	 *         none is found
//	 * @throws BadLocationException
//	 */
//	public static int findMatchingCharacter(IDocument document, int startingOffset,
//			boolean forward, String partitioning, char opening, char closing,
//			IPartitionFilter partitionFilter, int maxCharsAway) throws BadLocationException {
//		int deep = 0;
//		int offset = startingOffset;
//		if (forward) {
//			while (offset < document.getLength()) {
//				if (partitionFilter != null) {
//					ITypedRegion region = TextUtilities.getPartition(document, partitioning,
//							offset, true);
//					// TODO: don't refetch the partition while where're inside
//					// it
//					if (!partitionFilter.allowPartition(region.getType())) {
//						offset = region.getOffset() + region.getLength();
//						continue;
//					}
//				}
//
//				char c = document.getChar(offset);
//				if (c == opening)
//					deep++;
//				if (c == closing) {
//					if (deep == 0)
//						return offset;
//					deep--;
//				}
//				offset++;
//				if (offset - startingOffset > maxCharsAway)
//					return -1;
//			}
//		} else {
//			while (offset >= 0) {
//				if (partitionFilter != null) {
//					ITypedRegion region = TextUtilities.getPartition(document, partitioning,
//							offset, true);
//					// TODO: don't refetch the partition while where're inside
//					// it
//					if (!partitionFilter.allowPartition(region.getType())) {
//						offset = region.getOffset() - 1;
//						continue;
//					}
//				}
//				char c = document.getChar(offset);
//				if (c == closing)
//					deep++;
//				if (c == opening) {
//					if (deep == 0)
//						return offset;
//					deep--;
//				}
//				offset--;
//				if (startingOffset - offset > maxCharsAway)
//					return -1;
//			}
//		}
//		return -1;
//	}
//
//	public static boolean isPartitionOneOf(String partition, String[] list) {
//		for (int i = 0; i < list.length; i++) {
//			String item = list[i];
//			if (partition == item)
//				return true;
//		}
//		return false;
//	}

//	/**
//	 * Calculates the number of columns the given part of the document would
//	 * occupy in the text editor. This takes into account the size of the tab
//	 * character. Note that the given range must fit entirely into a single
//	 * line.
//	 * 
//	 * @param prefs
//	 *            the preferences provider to get the tab size from.
//	 * @param document
//	 *            the document to calculate the length in
//	 * @param lineStart
//	 *            the start of the line where the given part of the document
//	 *            resides
//	 * @param lineLength
//	 *            the length of the line where the given part of the document
//	 *            resides
//	 * @param start
//	 *            the starting offset of the given part of the document
//	 * @param end
//	 *            the ending offset of the given part of the document
//	 * @return the number of columns the given part of the document would occupy
//	 *         in the text viewer
//	 * @throws BadLocationException
//	 */
//	public static int calculateVisualLength(ITabPreferencesProvider prefs, IDocument document,
//			int lineStart, int lineLength, int start, int end) throws BadLocationException {
//		if (end == document.getLength())
//			end -= 1;
//		int res = 0;
//		int tabSize = prefs.getTabSize();
//		Assert.isLegal(end <= (lineStart + lineLength),
//				Messages.AutoEditUtils_cannotCalculateVisualLengthForSeveralLines);
//		int col = 0;
//		for (int offset = lineStart; offset < start; offset++) {
//			if (document.getChar(offset) != '\t')
//				col++;
//			else {
//				col += (tabSize - col % tabSize);
//			}
//		}
//		for (int offset = start; offset < end; offset++) {
//			if (document.getChar(offset) != '\t') {
//				res++;
//				col++;
//			} else {
//				res += (tabSize - col % tabSize);
//				col += (tabSize - col % tabSize);
//			}
//		}
//		return res;
//	}

//	/**
//	 * Calculates the number of columns the given part of the document would
//	 * occupy in the text editor. This takes into account the size of the tab
//	 * character. Note that the given range must fit entirely into a single
//	 * line.
//	 * 
//	 * This function calculates the starting and ending offsets of the line and
//	 * calls another overload.
//	 * 
//	 * @param prefs
//	 *            the preferences provider to get the tab size from.
//	 * @param document
//	 *            the document to calculate the length in
//	 * @param lineIndex
//	 *            the index of the line where the given part of the document
//	 *            resides
//	 * @param start
//	 *            the starting offset of the given part of the document
//	 * @param end
//	 *            the ending offset of the given part of the document
//	 * @return the number of columns the given part of the document would occupy
//	 *         in the text viewer
//	 * @throws BadLocationException
//	 */
//	public static int calculateVisualLength(ITabPreferencesProvider prefs, IDocument document,
//			int lineIndex, int start, int end) throws BadLocationException {
//		return calculateVisualLength(prefs, document, document.getLineOffset(lineIndex), document
//				.getLineLength(lineIndex), start, end);
//	}

//	/**
//	 * Calculates the number of columns the given part of the document would
//	 * occupy in the text editor. This takes into account the size of the tab
//	 * character. Note that the given range must fit entirely into a single
//	 * line.
//	 * 
//	 * This function calculates the starting and ending offsets of the line and
//	 * calls another overload.
//	 * 
//	 * @param prefs
//	 *            the preferences provider to get the tab size from.
//	 * @param document
//	 *            the document to calculate the length in
//	 * @param start
//	 *            the starting offset of the given part of the document
//	 * @param end
//	 *            the ending offset of the given part of the document
//	 * @return the number of columns the given part of the document would occupy
//	 *         in the text editor
//	 * @throws BadLocationException
//	 */
//	public static int calculateVisualLength(ITabPreferencesProvider prefs, IDocument document,
//			int start, int end) throws BadLocationException {
//		return calculateVisualLength(prefs, document, document.getLineOfOffset(start), start, end);
//	}
//
//	/**
//	 * Calculates the number of columns the given indentation string would
//	 * occupy in the text editor. This takes into account the size of the tab
//	 * character. Assumes that the given string starts in the first column, so
//	 * can only be used for indentation strings.
//	 * 
//	 * @param prefs
//	 *            the preferences provider to get the tab size from.
//	 * @param str
//	 *            the indentation strings (probably containing whitespace
//	 *            characters only) to calculate the length of
//	 * @return
//	 */
//	public static int getIndentVisualLength(ITabPreferencesProvider prefs, String str) {
//		int res = 0;
//		for (int i = 0; i < str.length(); i++) {
//			if (str.charAt(i) == '\t')
//				res += prefs.getTabSize();
//			else
//				res++;
//		}
//		return res;
//	}
//
//	/**
//	 * Calculates the number of columns the indentation of the given line would
//	 * occupy in the text editor. This takes into account the size of the tab
//	 * character.
//	 * 
//	 * @param prefs
//	 *            the preferences provider to get the tab size from.
//	 * @param document
//	 *            the document to calculate the length in
//	 * @param lineIndex
//	 *            the line whose indent length needs to be calculated
//	 * @return
//	 * @throws BadLocationException
//	 */
//	public static int getIndentVisualLength(ITabPreferencesProvider prefs, IDocument document,
//			int lineIndex) throws BadLocationException {
//		int lineOffset = document.getLineOffset(lineIndex);
//		int lineLength = document.getLineLength(lineIndex);
//		int lineHome = findEndOfWhiteSpace(document, lineOffset, lineOffset + lineLength);
//		return calculateVisualLength(prefs, document, lineOffset, lineLength, lineOffset, lineHome);
//	}
//	
//	public static int getMaximumLineDelimiterLength(IDocument document) {
//		String[] lineDelimiters = document.getLegalLineDelimiters();
//		int length = 0;
//		for (int i = 0; i < lineDelimiters.length; i++)
//			length = Math.max(length, lineDelimiters[i].length());
//		return length;
//	}
//
//	/**
//     * Determine if the given offset is right before the end-of-line
//     * character(s) on the given line. Also returns true if the given offset
//     * is equal to the <code>endOffset</code>. 
//     * 
//     * @param document
//     *            the document that determines the set of legal line delimiter
//     *            strings
//     * @param offset
//     *            the offset to check
//     * @param endOffset
//     *            the offset right after the end of the line containing
//     *            <code>offset</code>, should equal the value that would be
//     *            returned from
//     *            <code>document.getLineOffset(document.getLineOfOffset(offset)) +
//     * document.getLineLength(document.getLineOfOffset(offset))</code>.
//     * @return
//     * @throws BadLocationException
//     */
//	public static boolean atEndOfLine(IDocument document, int offset, int endOffset) throws BadLocationException {
//	    if (offset == endOffset)
//	        return true;
//		int maxDelta = getMaximumLineDelimiterLength(document);
//		if (offset + maxDelta < endOffset)
//			return false;
//		String s = document.get(offset, Math.min(maxDelta, document.getLength() - offset));
//		String[] searchStrings = document.getLegalLineDelimiters();
//		for (int i= 0; i < searchStrings.length; i++)
//			if (s.startsWith(searchStrings[i]))
//				return true;
//		return false;
//	}

}
