package com.googlecode.goclipse.editors;

import org.eclipse.jface.text.rules.*;

public class PartitionScanner extends RuleBasedPartitionScanner {
	public final static String COMMENT = "__comment";
	public final static String STRING = "__string";
	public final static String MULTILINE_STRING = "__multiline_string";

	public PartitionScanner() {

		IToken comment = new Token(COMMENT);
		IToken string  = new Token(STRING);
		IToken mstring  = new Token(MULTILINE_STRING);

		IPredicateRule[] rules = new IPredicateRule[5];

		rules[0] = new MultiLineRule("/*", "*/", comment, (char) 0, true);
		rules[1] = new SingleLineRule("//", null, comment);
        rules[2] = new SingleLineRule("'", "'", string);           // RAW STRING LITERAL
        rules[3] = new MultiLineRule("`", "`", mstring);           // RAW STRING LITERAL
        rules[4] = new SingleLineRule("\"", "\"", string, '\\');
		setPredicateRules(rules);
	}
}
