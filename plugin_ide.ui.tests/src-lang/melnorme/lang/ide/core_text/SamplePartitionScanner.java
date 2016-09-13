/*******************************************************************************
 * Copyright (c) 2010 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core_text;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

public class SamplePartitionScanner extends RuleBasedPartitionScanner {
	
	public static final String LANG_PARTITIONING = "___lang_partioning";
	
	private static final String LANG_STRING = "___lang_string";
	private static final String LANG_RAW_STRING = "___lang_raw_string";
	private static final String LANG_CHARACTER = "___lang_character";
	private static final String LANG_SINGLE_COMMENT = "___lang_single_comment";  
	private static final String LANG_MULTI_COMMENT = "___lang_multi_comment";
	
	public static final String[] LEGAL_CONTENT_TYPES = {
		LANG_STRING,
		LANG_RAW_STRING,
		LANG_CHARACTER,
		LANG_SINGLE_COMMENT,
		LANG_MULTI_COMMENT,
	};
	
	private static final char NO_ESCAPE = (char) -1;
	
	/**
	 * Creates the partitioner and sets up the appropriate rules.
	 */
	public SamplePartitionScanner() {
		IToken tkString = new Token(LANG_STRING);
		IToken tkRawString = new Token(LANG_RAW_STRING);
		IToken tkCharacter = new Token(LANG_CHARACTER);
		IToken tkSingleComment = new Token(LANG_SINGLE_COMMENT);
		IToken tkMultiComment = new Token(LANG_MULTI_COMMENT);
		
		List<IPredicateRule> rules = new ArrayList<IPredicateRule>();
		
		rules.add(new MultiLineRule("`", "`", tkRawString, NO_ESCAPE, true));
		rules.add(new MultiLineRule("\"", "\"", tkString, '\\', true));
		rules.add(new SingleLineRule("'", "'", tkCharacter, '\\', true));
		
		rules.add(new EndOfLineRule("//", tkSingleComment, NO_ESCAPE));
		
		rules.add(new MultiLineRule("/*", "*/", tkMultiComment, NO_ESCAPE, true));
		
		
		setPredicateRules(rules.toArray(new IPredicateRule[rules.size()]));
	}
	
}