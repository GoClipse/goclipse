package com.googlecode.goclipse.core.text;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

import melnorme.lang.ide.core.text.LangPartitionScanner;
import melnorme.utilbox.collections.ArrayList2;

public class GoPartitionScanner extends LangPartitionScanner implements GoPartitions {
	
	@Override
	protected void initPredicateRules(ArrayList2<IPredicateRule> rules) {
		addStandardRules(rules, LINE_COMMENT, BLOCK_COMMENT, null, null, null);
		
		rules.add(new SingleLineRule("'", "'", new Token(CHARACTER), '\\'));
		rules.add(new MultiLineRule("`", "`", new Token(MULTILINE_STRING))); // RAW STRING LITERAL
		rules.add(new SingleLineRule("\"", "\"", new Token(STRING), '\\'));
	}
	
}