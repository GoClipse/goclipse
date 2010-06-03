package com.googlecode.goclipse.editors;

import org.eclipse.jface.text.rules.*;
import org.eclipse.jface.text.*;

public class GoSourceScanner extends RuleBasedScanner {

	public GoSourceScanner() {
		IToken eolComment = new Token(new TextAttribute(IGoSourceColorConstants.COMMENT));
		IToken string = new Token( new TextAttribute(IGoSourceColorConstants.STRING));

		IRule[] rules = new IRule[] {
				// Add EOL comments
				new EndOfLineRule("//", eolComment),
				
				// Add rule for double quotes
				new SingleLineRule("\"", "\"", string, '\\'),
				
				// Add a rule for single quotes
				new SingleLineRule("'", "'", string, '\\'),
				
				// Add a rule for exported symbols
				new GoSourceExportedSymbolScanner(),
				
				// Add a keyword rule finder
				new GoSourceWordScanner(),
				
				// Add numerics
				new GoSourceNumberRule(),
				
				// Add generic whitespace rule.
				new WhitespaceRule(new WhitespaceDetector())
		};

		setRules(rules);
	}
	
	private static class WhitespaceDetector implements IWhitespaceDetector {

		public boolean isWhitespace(char c) {
			return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
		}
	}

}
