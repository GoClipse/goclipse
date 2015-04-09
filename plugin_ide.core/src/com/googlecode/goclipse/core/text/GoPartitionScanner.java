package com.googlecode.goclipse.core.text;

import melnorme.lang.ide.core.text.RuleBasedPartitionScannerExt;
import melnorme.utilbox.collections.ArrayList2;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

public class GoPartitionScanner extends RuleBasedPartitionScannerExt implements GoPartitions {
	
	@Override
	protected void addRules(ArrayList2<IPredicateRule> rules) {
		addStandardRules(rules, LINE_COMMENT, BLOCK_COMMENT, null, null, null);
		
		rules.add(new SingleLineRule("'", "'", new Token(CHARACTER), '\\'));
		rules.add(new MultiLineRule("`", "`", new Token(MULTILINE_STRING))); // RAW STRING LITERAL
		rules.add(new SingleLineRule("\"", "\"", new Token(STRING), '\\'));
	}
	
}