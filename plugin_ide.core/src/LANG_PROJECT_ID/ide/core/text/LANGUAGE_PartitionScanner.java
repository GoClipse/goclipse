/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package LANG_PROJECT_ID.ide.core.text;

import org.eclipse.jface.text.rules.IPredicateRule;

import melnorme.lang.ide.core.TextSettings_Actual.LangPartitionTypes;
import melnorme.lang.ide.core.text.LangPartitionScanner;
import melnorme.lang.tooling.parser.lexer.CharacterLexingRule;
import melnorme.utilbox.collections.ArrayList2;

public class LANGUAGE_PartitionScanner extends LangPartitionScanner {
	
	public LANGUAGE_PartitionScanner() {
		super();
	}
	
	@Override
	protected void initPredicateRules(ArrayList2<IPredicateRule> rules) {
		addStandardRules(rules, 
			LangPartitionTypes.LINE_COMMENT.getId(), 
			LangPartitionTypes.BLOCK_COMMENT.getId(), 
			LangPartitionTypes.DOC_LINE_COMMENT.getId(),
			LangPartitionTypes.DOC_BLOCK_COMMENT.getId(), 
			LangPartitionTypes.STRING.getId()
		);
		
		rules.add(new PredicateRule_Adapter(LangPartitionTypes.CHARACTER.getId(), new CharacterLexingRule()));
	}
	
}