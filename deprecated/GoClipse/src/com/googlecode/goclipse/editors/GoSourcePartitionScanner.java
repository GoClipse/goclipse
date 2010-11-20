package com.googlecode.goclipse.editors;

import org.eclipse.jface.text.rules.*;

public class GoSourcePartitionScanner extends RuleBasedPartitionScanner {
	public final static String GO_COMMENT = "__go_comment";

	public GoSourcePartitionScanner() {

		IToken goComment = new Token(GO_COMMENT);

		IPredicateRule[] rules = new IPredicateRule[] {
				new MultiLineRule("/*", "*/", goComment)
		};

		setPredicateRules(rules);
	}
}
