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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import melnorme.lang.tooling.ast.SourceRange;

public class StructureElement implements IStructureElement {
	
	protected final String name;
	protected final SourceRange nameSourceRange;
	protected final SourceRange sourceRange;
	
	protected final StructureElementKind elementKind;
	protected final StructureElementData elementData;
	
	protected final String type;
	
	protected final Iterable<IStructureElement> children;
	protected IStructureElementContainer parent;
	
	public StructureElement(
			String name, 
			SourceRange nameSourceRange, SourceRange sourceRange, 
			StructureElementKind elementKind,
			StructureElementData elementData, 
			String type, Iterable<IStructureElement> children) {
		this.name = assertNotNull(name);
		this.nameSourceRange = assertNotNull(nameSourceRange);
		this.sourceRange = assertNotNull(sourceRange);
		this.elementKind = assertNotNull(elementKind);
		this.elementData = assertNotNull(elementData);
		this.type = type;
		this.children = assertNotNull(children);
		
		for (IStructureElement child : children) {
			child.setParent(child);
		}
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public SourceRange getNameSourceRange() {
		return nameSourceRange;
	}
	
	@Override
	public SourceRange getRange() {
		return sourceRange;
	}
	
	@Override
	public StructureElementKind getKind() {
		return elementKind;
	}
	
	@Override
	public StructureElementData getData() {
		return elementData;
	}
	
	@Override
	public String getType() {
		return type;
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public Iterable<IStructureElement> getChildren() {
		return children;
	}
	
	@Override
	public IStructureElementContainer getParent() {
		return parent;
	}
	
	@Override
	public void setParent(IStructureElementContainer parent) {
		assertNotNull(parent);
		assertTrue(this.parent == null); // Can only set parent once
		this.parent = parent;
	}
	
	@Override
	public String getModuleName() {
		IStructureElementContainer parent = getParent();
		return parent == null ? null : parent.getModuleName();
	}
	
}