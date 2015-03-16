/*******************************************************************************
 * Copyright (c) 2011, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.text;

import melnorme.utilbox.core.fntypes.Function;
import melnorme.utilbox.misc.ArrayUtil;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;

/**
 * A rule that matches exact sequences in the scanner.
 * A word detector is used to specify word parts that if present after a matched possible sequence, 
 * will cause this rule *not* to match.
 * It is recommended that there are not too many possibleSequence's as performance will not be ideal otherwise.
 */
public class FullPatternRule implements IRule {
	
	protected IToken token;
	protected final IWordDetector ruleCancelWordDetector;
	protected char[][] possibleSequences;
	
	public FullPatternRule(IToken token, String[] possibleSequences, IWordDetector ruleCancelWordDetector) {
		this.token = token;
		this.ruleCancelWordDetector = ruleCancelWordDetector;
		this.possibleSequences = ArrayUtil.map(possibleSequences, STRING_to_CHAR_ARRAY, char[].class);
	}
	
	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		int ch = scanner.read();
		for (int i = 0; i < possibleSequences.length; i++) {
			char[] sequence = possibleSequences[i];
			if(matchesSequence(scanner, ch, sequence)) {
				int endCh = scanner.read(); 
				scanner.unread(); // rewind end character
				if(endCh == ICharacterScanner.EOF || !ruleCancelWordDetector.isWordPart((char) endCh)) {
					return token;
				}
				
				// need to rewind scanner until first character
				scannerUnread(scanner, sequence.length-1);
			}
			
		}
		scanner.unread();
		return Token.UNDEFINED;
	}
	
	/** Returns true or false according to whether scanner matches given sequence, with given firstChar already read
	 * from the scanner. 
	 */
	protected static boolean matchesSequence(ICharacterScanner scanner, int firstChar, char[] sequence) {
		int ch = firstChar;
		if(ch == sequence[0]) {
			for(int i = 1; i < sequence.length; ++i) {
				ch = scanner.read();
				// The if will also trigger if ch is EOF
				if(ch != sequence[i]) {
					// need to rewind scanner until first character
					scannerUnread(scanner, i);
					return false; 
				}
			}
			return true;
		}
		return false;
	}
	
	protected static void scannerUnread(ICharacterScanner scanner, int count) {
		while(--count >= 0) {
			scanner.unread();
		}
	}
	
	public static final Function<String, char[]> STRING_to_CHAR_ARRAY = new Function<String, char[]>() {
		@Override
		public char[] evaluate(String obj) {
			return obj.toCharArray();
		}
	};
	
}
