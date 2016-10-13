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
package melnorme.lang.utils.parse;

import java.util.function.Predicate;

import melnorme.lang.tooling.parser.lexer.CharacterReader_SubReader;

public interface ICharacterReader extends ICharSource<RuntimeException> {
	
	// TODO: this could be moved to ICharSource
	
	default String consumeUntil(Predicate<ICharacterReader> untilPredicate) throws RuntimeException {
		CharacterReader_SubReader subReader = new CharacterReader_SubReader(this);
		while(subReader.hasCharAhead() && !untilPredicate.test(subReader)) {
			subReader.consume();
		}
		
		int consumedCount = subReader.getConsumedCount();
		if(consumedCount == 0) {
			return null;
		}
		return this.consumeString(consumedCount);
	}
	
}