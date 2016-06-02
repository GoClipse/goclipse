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
package melnorme.lang.tooling;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertUnreachable;

@LANG_SPECIFIC
public enum CompletionProposalKind {
	
	UNKNOWN,
	KEYWORD,
	ERROR,
	
	PACKAGE,
	
	VARIABLE,
	
	FUNCTION,
	//CONSTRUCTOR, 
	
	//CLASS, 
	INTERFACE,
	STRUCT,
	
	TYPE_DECL,
	
	//ALIAS 
	
	/* ----------------- Language-specific: ----------------- */
	;
	
	
	public <RET> RET switchOnKind(ProposalKindVisitor<RET> visitor) {
		switch(this) {
		case UNKNOWN: return visitor.visitUnknown();
		case KEYWORD: return visitor.visitKeyword();
		case ERROR: return visitor.visitError();
		
		case PACKAGE: return visitor.visitPackage();
		
		case VARIABLE: return visitor.visitVariable();
		
		case FUNCTION: return visitor.visitFunction();
//		case CONSTRUCTOR: return visitor.visitConstructor();
		
		case STRUCT: return visitor.visitStruct();
		case INTERFACE: return visitor.visitInterface();
		case TYPE_DECL: return visitor.visitTypeDecl();
		
//		case ALIAS: return visitor.visitAlias();
		
		/* ----------------- Language-specific: ----------------- */
		
		}
		throw assertUnreachable();
	}
	
	public static interface ProposalKindVisitor<RET> extends AbstractKindVisitor<RET> {
		
		RET visitError();
		
		RET visitPackage();
		
		RET visitTypeDecl();
		
	}
	
}