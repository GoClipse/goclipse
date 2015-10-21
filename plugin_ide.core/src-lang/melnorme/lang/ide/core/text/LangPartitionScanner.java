/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.text;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.Token;

import melnorme.lang.tooling.parser.lexer.IPredicateLexingRule;
import melnorme.utilbox.collections.ArrayList2;

/**
 * A note about using scanner rules: Each rule should map to a unique partition type.
 * This is because when partitioning with resuming, the partition type will be used to determine
 * which rule to try to find the end of the partition. 
 * As such the same rule that was used to start the partition, should be used to find the end of it.
 * The only way using multiple rules for the same partition type is ok, is when the rules all use the same terminator.
 */
public abstract class LangPartitionScanner extends RuleBasedPartitionScannerExt {
	
	protected static final char NO_ESCAPE_CHAR = (char) -1;
	
	public LangPartitionScanner() {
	}
	
	/* -----------------  ----------------- */

	public class PredicateRule_Adapter extends DefaultPredicateRule {
		
		protected final IPredicateLexingRule rule;
		
		public PredicateRule_Adapter(String partitionTypeId, IPredicateLexingRule rule) {
			super(new Token(partitionTypeId));
			this.rule = assertNotNull(rule);
		}
		
		@Override
		public IToken evaluate(ICharacterScanner scanner) {
			assertTrue(scanner == LangPartitionScanner.this);
			CharacterScanner_ReaderHelper readerHelper = new CharacterScanner_ReaderHelper(LangPartitionScanner.this);
			
			if(rule.tryMatch(readerHelper)) {
				return token;
			} else {
				return Token.UNDEFINED;
			}
		}
		
	}
	
	/* -----------------  ----------------- */
	
	/***
	 * Add some partition rules common to C-style languages.
	 * All rules are optional, if an id is null, the rule will not be added.
	 */
	protected static void addStandardRules(ArrayList2<IPredicateRule> rules, 
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