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

public abstract class AbstractStructureElementKindVisitor<RET> {
	
	protected abstract RET visitVariable();
	
	protected abstract RET visitFunction();
	
	protected abstract RET visitConstructor();
	
	protected abstract RET visitClass();
	
	protected abstract RET visitInterface();
	
	protected abstract RET visitStruct();
	
	protected abstract RET visitModuleDeclaration();
	
	protected abstract RET visitTemplate();
	
	protected abstract RET visitAlias();
	
}