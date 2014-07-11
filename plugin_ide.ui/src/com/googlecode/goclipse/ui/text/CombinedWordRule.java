package com.googlecode.goclipse.ui.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;

/**
 * An implementation of <code>IRule</code> capable of detecting words.
 * <p>
 * Word rules also allow for the association of tokens with specific words. That
 * is, not only can the rule be used to provide tokens for exact matches, but
 * also for the generalized notion of a word in the context in which it is used.
 * A word rules uses a word detector to determine what a word is.
 * </p>
 * <p>
 * This word rule allows a word detector to be shared among different word
 * matchers. Its up to the word matchers to decide if a word matches and, in
 * this a case, which token is associated with that word.
 * </p>
 * 
 * @see IWordDetector
 * @since 3.0
 */
public class CombinedWordRule implements IRule {

	/**
	 * Word matcher, that associates matched words with tokens.
	 */
	public static class WordMatcher {

		/** The table of predefined words and token for this matcher */
		private Map<CharacterBuffer, IToken> fWords = new HashMap<CharacterBuffer, IToken>();

		/**
		 * Adds a word and the token to be returned if it is detected.
		 * 
		 * @param word
		 *            the word this rule will search for, may not be
		 *            <code>null</code>
		 * @param token
		 *            the token to be returned if the word has been found, may
		 *            not be <code>null</code>
		 */
		public void addWord(String word, IToken token) {
			Assert.isNotNull(word);
			Assert.isNotNull(token);

			fWords.put(new CharacterBuffer(word), token);
		}

		/**
		 * Returns the token associated to the given word and the scanner state.
		 * 
		 * @param scanner
		 *            the scanner
		 * @param word
		 *            the word
		 * @return the token or <code>null</code> if none is associated by this
		 *         matcher
		 */
		public IToken evaluate(ICharacterScanner scanner, CharacterBuffer word) {
			IToken token = fWords.get(word);
			if (token != null)
				return token;
			return Token.UNDEFINED;
		}

		/**
		 * Removes all words.
		 */
		public void clearWords() {
			fWords.clear();
		}
	}

	/**
	 * Character buffer, mutable <b>or</b> suitable for use as key in hash maps.
	 */
	public static class CharacterBuffer {

		/** Buffer content */
		private char[] fContent;
		/** Buffer content size */
		private int fLength = 0;

		/** Is hash code cached? */
		private boolean fIsHashCached = false;
		/** The hash code */
		private int fHashCode;

		/**
		 * Initialize with the given capacity.
		 * 
		 * @param capacity
		 *            the initial capacity
		 */
		public CharacterBuffer(int capacity) {
			fContent = new char[capacity];
		}

		/**
		 * Initialize with the given content.
		 * 
		 * @param content
		 *            the initial content
		 */
		public CharacterBuffer(String content) {
			fContent = content.toCharArray();
			fLength = content.length();
		}

		/**
		 * Empties this buffer.
		 */
		public void clear() {
			fIsHashCached = false;
			fLength = 0;
		}

		/**
		 * Appends the given character to the buffer.
		 * 
		 * @param c
		 *            the character
		 */
		public void append(char c) {
			fIsHashCached = false;
			if (fLength == fContent.length) {
				char[] old = fContent;
				fContent = new char[old.length << 1];
				System.arraycopy(old, 0, fContent, 0, old.length);
			}
			fContent[fLength++] = c;
		}

		/**
		 * Returns the length of the content.
		 * 
		 * @return the length
		 */
		public int length() {
			return fLength;
		}

		/**
		 * Returns the content as string.
		 * 
		 * @return the content
		 */
		@Override
		public String toString() {
			return new String(fContent, 0, fLength);
		}

		/**
		 * Returns the character at the given position.
		 * 
		 * @param i
		 *            the position
		 * @return the character at position <code>i</code>
		 */
		public char charAt(int i) {
			return fContent[i];
		}

		/*
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			if (fIsHashCached)
				return fHashCode;

			int hash = 0;
			for (int i = 0, n = fLength; i < n; i++)
				hash = 29 * hash + fContent[i];
			fHashCode = hash;
			fIsHashCached = true;
			return hash;
		}

		/*
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj == this)
				return true;
			if (!(obj instanceof CharacterBuffer))
				return false;
			CharacterBuffer buffer = (CharacterBuffer) obj;
			int length = buffer.length();
			if (length != fLength)
				return false;
			for (int i = 0; i < length; i++)
				if (buffer.charAt(i) != fContent[i])
					return false;
			return true;
		}

		/**
		 * Is the content equal to the given string?
		 * 
		 * @param string
		 *            the string
		 * @return <code>true</code> iff the content is the same character
		 *         sequence as in the string
		 */
		public boolean equals(String string) {
			int length = string.length();
			if (length != fLength)
				return false;
			for (int i = 0; i < length; i++)
				if (string.charAt(i) != fContent[i])
					return false;
			return true;
		}
	}

	/** Internal setting for the uninitialized column constraint */
	private static final int UNDEFINED = -1;

	/** The word detector used by this rule */
	private IWordDetector fDetector;
	/**
	 * The default token to be returned on success and if nothing else has been
	 * specified.
	 */
	private IToken fDefaultToken;
	/** The column constraint */
	private int fColumn = UNDEFINED;
	/** Buffer used for pattern detection */
	private CharacterBuffer fBuffer = new CharacterBuffer(16);

	/** List of word matchers */
	private List<WordMatcher> fMatchers = new ArrayList<WordMatcher>();

	/**
	 * Creates a rule which, with the help of an word detector, will return the
	 * token associated with the detected word. If no token has been associated,
	 * the scanner will be rolled back and an undefined token will be returned
	 * in order to allow any subsequent rules to analyze the characters.
	 * 
	 * @param detector
	 *            the word detector to be used by this rule, may not be
	 *            <code>null</code>
	 * 
	 * @see WordMatcher#addWord(String, IToken)
	 */
	public CombinedWordRule(IWordDetector detector) {
		this(detector, null, Token.UNDEFINED);
	}

	/**
	 * Creates a rule which, with the help of an word detector, will return the
	 * token associated with the detected word. If no token has been associated,
	 * the specified default token will be returned.
	 * 
	 * @param detector
	 *            the word detector to be used by this rule, may not be
	 *            <code>null</code>
	 * @param defaultToken
	 *            the default token to be returned on success if nothing else is
	 *            specified, may not be <code>null</code>
	 * 
	 * @see WordMatcher#addWord(String, IToken)
	 */
	public CombinedWordRule(IWordDetector detector, IToken defaultToken) {
		this(detector, null, defaultToken);
	}

	/**
	 * Creates a rule which, with the help of an word detector, will return the
	 * token associated with the detected word. If no token has been associated,
	 * the scanner will be rolled back and an undefined token will be returned
	 * in order to allow any subsequent rules to analyze the characters.
	 * 
	 * @param detector
	 *            the word detector to be used by this rule, may not be
	 *            <code>null</code>
	 * @param matcher
	 *            the initial word matcher
	 * 
	 * @see WordMatcher#addWord(String, IToken)
	 */
	public CombinedWordRule(IWordDetector detector, WordMatcher matcher) {
		this(detector, matcher, Token.UNDEFINED);
	}

	/**
	 * Creates a rule which, with the help of an word detector, will return the
	 * token associated with the detected word. If no token has been associated,
	 * the specified default token will be returned.
	 * 
	 * @param detector
	 *            the word detector to be used by this rule, may not be
	 *            <code>null</code>
	 * @param matcher
	 *            the initial word matcher
	 * @param defaultToken
	 *            the default token to be returned on success if nothing else is
	 *            specified, may not be <code>null</code>
	 * 
	 * @see WordMatcher#addWord(String, IToken)
	 */
	public CombinedWordRule(IWordDetector detector, WordMatcher matcher,
			IToken defaultToken) {

		Assert.isNotNull(detector);
		Assert.isNotNull(defaultToken);

		fDetector = detector;
		fDefaultToken = defaultToken;
		if (matcher != null)
			addWordMatcher(matcher);
	}

	/**
	 * Adds the given matcher.
	 * 
	 * @param matcher
	 *            the matcher
	 */
	public void addWordMatcher(WordMatcher matcher) {
		fMatchers.add(matcher);
	}

	/**
	 * Sets a column constraint for this rule. If set, the rule's token will
	 * only be returned if the pattern is detected starting at the specified
	 * column. If the column is smaller then 0, the column constraint is
	 * considered removed.
	 * 
	 * @param column
	 *            the column in which the pattern starts
	 */
	public void setColumnConstraint(int column) {
		if (column < 0)
			column = UNDEFINED;
		fColumn = column;
	}

	/*
	 * @see IRule#evaluate(ICharacterScanner)
	 */
	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		int c = scanner.read();
		if (fDetector.isWordStart((char) c)) {
			if (fColumn == UNDEFINED || (fColumn == scanner.getColumn() - 1)) {

				fBuffer.clear();
				do {
					fBuffer.append((char) c);
					c = scanner.read();
				} while (c != ICharacterScanner.EOF
						&& fDetector.isWordPart((char) c));
				scanner.unread();

				for (int i = 0, n = fMatchers.size(); i < n; i++) {
					IToken token = fMatchers.get(i).evaluate(scanner, fBuffer);
					if (!token.isUndefined())
						return token;
				}

				if (fDefaultToken.isUndefined())
					unreadBuffer(scanner);

				return fDefaultToken;
			}
		}

		scanner.unread();
		return Token.UNDEFINED;
	}

	/**
	 * Returns the characters in the buffer to the scanner.
	 * 
	 * @param scanner
	 *            the scanner to be used
	 */
	private void unreadBuffer(ICharacterScanner scanner) {
		for (int i = fBuffer.length() - 1; i >= 0; i--)
			scanner.unread();
	}
}
