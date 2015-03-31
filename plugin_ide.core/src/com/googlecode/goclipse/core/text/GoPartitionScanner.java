package com.googlecode.goclipse.core.text;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

public class GoPartitionScanner extends RuleBasedPartitionScanner implements GoPartitions {
	
	public GoPartitionScanner() {
		IToken comment = new Token(COMMENT);
		IToken character = new Token(CHARACTER);
		IToken string = new Token(STRING);
		IToken mstring = new Token(MULTILINE_STRING);
		
		IPredicateRule[] rules = new IPredicateRule[5];
		
		rules[0] = new MultiLineRule("/*", "*/", comment, (char) 0, true);
		rules[1] = new SingleLineRule("//", null, comment, (char) 0, true);
		
		rules[2] = new SingleLineRule("'", "'", character, '\\');
		rules[3] = new MultiLineRule("`", "`", mstring); // RAW STRING LITERAL
		rules[4] = new SingleLineRule("\"", "\"", string, '\\');
		
		setPredicateRules(rules);
	}
	
}
