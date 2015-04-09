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
package melnorme.lang.tooling.parser.lexer;


public interface ILexingRule {
	
	/** 
	 * Evaluate the rule using given reader.
	 * If the rule succeeds, the reader position will be at the end of the token.
	 * If the rule fails, the reader will be in an arbitrary position, and must be reset before re-use.  
	 */
	boolean evaluate(ICharacterReader reader);
	
}