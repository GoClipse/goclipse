/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.text;


import melnorme.utilbox.collections.ArrayList2;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

/**
 * A note about using scanner rules: Each rule should map to a unique partition type.
 * This is because when partitioning with resuming, the partition type will be used to determine
 * which rule to try to find the end of the partition. 
 * As such the same rule that was used to start the partition, should be used to find the end of it.
 * The only way using multiple rules for the same partition type is ok, is when the rules all use the same terminator.
 */
public abstract class RuleBasedPartitionScannerExt extends RuleBasedPartitionScanner {
	
	protected static final char NO_ESCAPE_CHAR = (char) -1;
	
	public RuleBasedPartitionScannerExt() {
		ArrayList2<IPredicateRule> rules = new ArrayList2<>();
		addRules(rules);
		setPredicateRules(rules.toArray(IPredicateRule.class));
	}
	
	protected abstract void addRules(ArrayList2<IPredicateRule> rules);
	
	/***
	 * Add some partition rules common to C-style languages.
	 * All rules are optional, if an id is null, the rule will not be added.
	 */
	protected void addStandardRules(ArrayList2<IPredicateRule> rules, 
			String lineCommentId, String blockCommentId, 
			String docLineCommentId, String docBlockCommentId,
			String stringId) {
		if(docLineCommentId != null) {
			rules.add(new PatternRule("///", null, new Token(docLineCommentId), NO_ESCAPE_CHAR, true, true));
		}
		if(docBlockCommentId != null) {
			rules.add(new PatternRule("/**", "*/", new Token(docBlockCommentId), NO_ESCAPE_CHAR, false, true));
		}
		
		if(lineCommentId != null) {
			rules.add(new PatternRule("//", null, new Token(lineCommentId), NO_ESCAPE_CHAR, true, true));
		}
		if(blockCommentId != null) {
			rules.add(new PatternRule("/*", "*/", new Token(blockCommentId), NO_ESCAPE_CHAR, false, true));
		}
		
		if(stringId != null) {
			rules.add(new PatternRule("\"", "\"", new Token(stringId), '\\', false, true));
		}
		
	}
	
}