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
import static melnorme.utilbox.core.CoreUtil.areEqual;
import static melnorme.utilbox.core.CoreUtil.nullToEmpty;
import static melnorme.utilbox.misc.StringUtil.prefixStr;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.misc.HashcodeUtil;

public class StructureElement implements IStructureElement {
	
	protected final String name;
	protected final SourceRange nameSourceRange;
	protected final SourceRange sourceRange;
	
	protected final StructureElementKind elementKind;
	protected final StructureElementData elementData;
	
	protected final String type;
	
	protected final Indexable<StructureElement> children;
	protected IStructureElementContainer parent;
	
	public StructureElement(
			String name, 
			SourceRange nameSourceRange, SourceRange sourceRange, 
			StructureElementKind elementKind,
			StructureElementData elementData, 
			String type, Indexable<StructureElement> children) {
		this.name = assertNotNull(name);
		this.nameSourceRange = assertNotNull(nameSourceRange);
		this.sourceRange = assertNotNull(sourceRange);
		this.elementKind = assertNotNull(elementKind);
		this.elementData = assertNotNull(elementData);
		this.type = type;
		this.children = nullToEmpty(children);
		
		for (IStructureElement child : this.children) {
			child.setParent(child);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof StructureElement)) return false;
		
		StructureElement other = (StructureElement) obj;
		
		return 
			areEqual(name, other.name) &&
			areEqual(nameSourceRange, other.nameSourceRange) &&
			areEqual(sourceRange, other.sourceRange) &&
			areEqual(elementKind, other.elementKind) &&
			areEqual(elementData, other.elementData) &&
			areEqual(type, other.type) &&
			areEqual(children, other.children)
			;
	}
	
	@Override
	public int hashCode() {
		// This should be enough to provide a good hash code
		return HashcodeUtil.combinedHashCode(name, sourceRange, elementKind, elementData, children.size());
	}
	
	protected String toStringNode() {
		return "StructureElem " + name + sourceRange + " " + elementKind + prefixStr(" : ", type);
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public SourceRange getNameSourceRange() {
		return nameSourceRange;
	}
	
	@Override
	public SourceRange getSourceRange() {
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
	public Indexable<IStructureElement> getChildren() {
		return children.upcastTypeParameter();
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
	
	/* -----------------  ----------------- */
	
	@Override
	public String toString() {
		return toString(true);
	}
	
	public String toString(boolean printChildren) {
		if(printChildren) {
			return new StructureElementPrinter().printElement(this);
		} else {
			return toStringNode();
		}
	}
	
}