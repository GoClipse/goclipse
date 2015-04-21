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
package melnorme.lang.tooling;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertUnreachable;

@LANG_SPECIFIC
public enum CompletionProposalKind {
	
	KEYWORD,
	UNKNOWN,
	
	VARIABLE,
	
	FUNCTION,
	CONSTRUCTOR,
	
	CLASS,
	INTERFACE,
	STRUCT,
	
	MODULEDEC;
	
	public static abstract class ProposalKindVisitor<RET> extends AbstractKindVisitor<RET> {
		
		public RET switchOnKind(CompletionProposalKind kind) {
			switch(kind) {
			case KEYWORD: return visitKeyword();
			case UNKNOWN: return visitUnknown();
			
			case VARIABLE: return visitVariable();
			
			case FUNCTION: return visitFunction();
			case CONSTRUCTOR: return visitConstructor();
			
			case STRUCT: return visitStruct();
			case CLASS: return visitClass();
			case INTERFACE: return visitInterface();
			
			case MODULEDEC: return visitModule();
			
			}
			throw assertUnreachable();
		}
		
	}
	
}