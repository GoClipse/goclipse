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

import melnorme.lang.utils.parse.ICharacterReader;

public interface IPredicateLexingRule {
	
	/** 
	 * Evaluate the rule using given reader.
	 * If the rule succeeds, the reader position will be at the end of the token.
	 * If the rule fails, the reader position will remain unchanged.  
	 */
	default boolean tryMatch(ICharacterReader parentReader) {
		CharacterReader_SubReader reader = new CharacterReader_SubReader(parentReader);
		
		boolean sucess = doEvaluate(reader);
		if(sucess) {
			reader.consumeInParentReader();
		}
		return sucess;
	}
	
	public boolean doEvaluate(ICharacterReader reader);
	
}