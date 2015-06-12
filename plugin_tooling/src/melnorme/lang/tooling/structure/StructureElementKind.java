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
package melnorme.lang.tooling.structure;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertUnreachable;
import melnorme.lang.tooling.AbstractKindVisitor;
import melnorme.lang.tooling.LANG_SPECIFIC;


@LANG_SPECIFIC
// TODO: needs review by a Go expert, this might be missing some Go archetypes
public enum StructureElementKind {
	
	VARIABLE,
	CONST,
	
	FUNCTION,
//	CONSTRUCTOR,
	
//	CLASS,
	INTERFACE,
	STRUCT,
	TYPE_DECL,
	
	MODULEDEC
	;
	
	public <RET> RET switchOnKind(StructureElementKindVisitor<RET> visitor) {
		return switchOnKind(this, visitor);
	}
	
	public static <RET> RET switchOnKind(StructureElementKind kind, StructureElementKindVisitor<RET> visitor) {
		switch(kind) {
		case VARIABLE: return visitor.visitVariable();
		case CONST: return visitor.visitConst();
		
		case FUNCTION: return visitor.visitFunction();
//		case CONSTRUCTOR: return visitor.visitConstructor();
		
//		case CLASS: return visitor.visitClass();
		case INTERFACE: return visitor.visitInterface();
		case STRUCT: return visitor.visitStruct();
		case TYPE_DECL: return visitor.visitTypeDecl();
		
		case MODULEDEC: return visitor.visitModule();
		
		}
		throw assertUnreachable();
	}
	
	public static interface StructureElementKindVisitor<RET> extends AbstractKindVisitor<RET> {
		
		RET visitConst();
		
		RET visitTypeDecl();
		
	}
	
}
