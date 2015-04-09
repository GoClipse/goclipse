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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import melnorme.lang.tooling.parser.lexer.ILexingRule;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class LexingRulePredicateRule extends DefaultPredicateRule {
	
	protected final ILexingRule rule;
	
	public LexingRulePredicateRule(String partitionTypeId, ILexingRule rule) {
		super(new Token(partitionTypeId));
		this.rule = assertNotNull(rule);
	}
	
	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		CharacterScannerHelper helper = new CharacterScannerHelper(scanner);
		
		if(rule.evaluate(helper)) {
			return token;
		} else {
			helper.reset();
			return Token.UNDEFINED;
		}
	}
	
}