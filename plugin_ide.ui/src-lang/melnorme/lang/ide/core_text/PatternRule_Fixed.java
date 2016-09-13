package melnorme.lang.ide.core_text;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.Token;

/**
 * This is a workaround for this {@link PatternRule} bug: https://bugs.eclipse.org/bugs/show_bug.cgi?id=409538
 * 
 * We'll have to keep using this workaround indefinetely unless/until the time comes 
 * where platform 4.3.1 is specified as a minimum requirement 
 */
public class PatternRule_Fixed extends PatternRule {
	
	public PatternRule_Fixed(String startSequence, String endSequence, IToken token, char escapeCharacter,
		boolean breaksOnEOL, boolean breaksOnEOF) {
		super(startSequence, endSequence, token, escapeCharacter, breaksOnEOL, breaksOnEOF);
	}
	
	public PatternRule_Fixed(String startSequence, String endSequence, IToken token, char escapeCharacter,
		boolean breaksOnEOL) {
		this(startSequence, endSequence, token, escapeCharacter, breaksOnEOL, false);
	}

	@Override
	protected IToken doEvaluate(ICharacterScanner scanner, boolean resume) {
		if (resume) {

			if (endSequenceDetected(scanner))
				return fToken;

		} else {

			int c= scanner.read();
			if (c == fStartSequence[0]) {
				if (sequenceDetected(scanner, fStartSequence, false)) {
					if (endSequenceDetected(scanner))
						return fToken;
				}
			}
		}

		scanner.unread();
		return Token.UNDEFINED;
	}
	
}