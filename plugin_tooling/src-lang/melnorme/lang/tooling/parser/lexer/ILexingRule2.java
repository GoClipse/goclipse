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
package melnorme.lang.tooling.parser.lexer;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import melnorme.lang.utils.parse.ICharacterReader;

public interface ILexingRule2<TOKEN> {
	
	default TOKEN evaluateToken(ICharacterReader reader) {
		CharacterReader_SubReader subReader = new CharacterReader_SubReader(reader);
		
		TOKEN result = doEvaluateToken(subReader);
		if(subReader.getReadPosition() == 0) {
			assertTrue(result == null);
		}
		if(result != null) {
			subReader.consumeInParentReader();
		}
		return result;
	}
	
	TOKEN doEvaluateToken(ICharacterReader subReader);
	
}