package com.googlecode.goclipse.core_text;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

import melnorme.lang.ide.core.TextSettings_Actual.LangPartitionTypes;
import melnorme.lang.ide.core_text.LangPartitionScanner;
import melnorme.utilbox.collections.ArrayList2;

public class GoPartitionScanner extends LangPartitionScanner {
	
	@Override
	protected void initPredicateRules(ArrayList2<IPredicateRule> rules) {
		addStandardRules(rules, 
			LangPartitionTypes.LINE_COMMENT.getId(), 
			LangPartitionTypes.BLOCK_COMMENT.getId(), 
			null, 
			null, 
			null
		);
		
		rules.add(new SingleLineRule("'", "'", new Token(LangPartitionTypes.CHARACTER.getId()), '\\'));
		rules.add(new MultiLineRule("`", "`", new Token(LangPartitionTypes.MULTILINE_STRING.getId())));
		rules.add(new SingleLineRule("\"", "\"", new Token(LangPartitionTypes.STRING.getId()), '\\'));
	}
	
}