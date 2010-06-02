package com.googlecode.goclipse.editors;

import org.eclipse.jface.text.rules.*;
import org.eclipse.jface.text.*;

public class GoSourceScanner extends RuleBasedScanner {

	public GoSourceScanner(ColorManager manager) {
		IToken procInstr =
			new Token(
				new TextAttribute(
					manager.getColor(IGoSourceColorConstants.PROC_INSTR)));

		IRule[] rules = new IRule[2];
		//Add rule for processing instructions
		rules[0] = new SingleLineRule("<?", "?>", procInstr);
		// Add generic whitespace rule.
		rules[1] = new WhitespaceRule(new GoSourceWhitespaceDetector());

		setRules(rules);
	}
}
