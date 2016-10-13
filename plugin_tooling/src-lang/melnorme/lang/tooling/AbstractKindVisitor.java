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

public interface AbstractKindVisitor<RET> {
	
	public abstract RET visitUnknown();
	public abstract RET visitKeyword();
	public abstract RET visitAlias();
	
	/* -----------------  ----------------- */
	
	public abstract RET visitModule();
	
	public abstract RET visitVariable();
	
	public abstract RET visitFunction();
	public abstract RET visitConstructor();
	
	public abstract RET visitClass();
	public abstract RET visitInterface();
	public abstract RET visitStruct();
	public abstract RET visitEnum();
	public abstract RET visitNative();
	
}