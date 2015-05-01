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

import melnorme.lang.tooling.ElementAttributes;
import melnorme.lang.tooling.ast.SourceRange;


interface IStructureElement_Default extends IStructureElementContainer {
	
	/** @return the name of this element. */
	String getName();
	
	/** @return the source range of this element, that is, 
	 * the range in the source file that this elements corresponds to. */
	SourceRange getSourceRange();
	
	/** @return the source range of the name of this element. */
	SourceRange getNameSourceRange();
	
	/** @return an enum like instance indicating the kind of element this is. */
	StructureElementKind getKind();
	
	/** @return the additional data object for this element. */
	ElementAttributes getAttributes();
	
	/** @return the type of this element, or type-like text. Can be null.*/
	String getType();
	
	/** @return the container of this element. Can be null.*/
	IStructureElementContainer getParent();
	
	/** Set the parent. Only the parent code should call this method. */
	void setParent(IStructureElementContainer parent);
	
}