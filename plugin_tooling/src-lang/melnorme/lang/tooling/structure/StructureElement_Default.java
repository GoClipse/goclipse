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
package melnorme.lang.tooling.structure;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.areEqual;
import static melnorme.utilbox.misc.StringUtil.prefixStr;
import melnorme.lang.tooling.ElementAttributes;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.misc.HashcodeUtil;

/**
 * Only {@link StructureElement} can extend this class!
 */
abstract class StructureElement_Default extends AbstractStructureContainer implements IStructureElement {
	
	protected final String name;
	protected final SourceRange nameSourceRange;
	protected final SourceRange sourceRange;
	
	protected final StructureElementKind elementKind;
	protected final ElementAttributes elementAttributes;
	
	protected final String type;
	
	protected IStructureElementContainer parent;
	
	public StructureElement_Default(
			String name, 
			SourceRange nameSourceRange, SourceRange sourceRange, 
			StructureElementKind elementKind,
			ElementAttributes elementAttributes, 
			String type, Indexable<StructureElement> children) {
		super(children);
		this.name = assertNotNull(name);
		this.nameSourceRange = assertNotNull(nameSourceRange);
		this.sourceRange = assertNotNull(sourceRange);
		this.elementKind = assertNotNull(elementKind);
		this.elementAttributes = elementAttributes == null ? new ElementAttributes(null) : elementAttributes;
		this.type = type;
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
			areEqual(elementAttributes, other.elementAttributes) &&
			areEqual(type, other.type) &&
			areEqual(children, other.children)
			;
	}
	
	@Override
	public int hashCode() {
		// This should be enough to provide a good hash code
		return HashcodeUtil.combinedHashCode(name, sourceRange, elementKind, elementAttributes, children.size());
	}
	
	protected String toStringNode() {
		return "ELEM " + name + sourceRange + " " + elementKind + prefixStr(" : ", type) + " " + elementAttributes;
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
	public ElementAttributes getAttributes() {
		return elementAttributes;
	}
	
	@Override
	public String getType() {
		return type;
	}
	
	/* -----------------  ----------------- */
	
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
	
//	@Override
//	public String getModuleName() {
//		IStructureElementContainer parent = getParent();
//		return parent == null ? null : parent.getModuleName();
//	}
	
	@Override
	public ISourceFileStructure getContainingFileStructure() {
		return getFileStructure(this);
	}
	
	public static ISourceFileStructure getFileStructure(IStructureElement element) {
		IStructureElementContainer parent = element.getParent();
		
		if(parent instanceof ISourceFileStructure) {
			return (ISourceFileStructure) parent;
		} else if (parent instanceof IStructureElement) {
			return getFileStructure((IStructureElement) parent);
		} else {
			return null;
		}
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public String toString() {
		return toString(true);
	}
	
	public String toString(boolean printChildren) {
		if(printChildren) {
			return new StructureElementPrinter().printElement((StructureElement) this);
		} else {
			return toStringNode();
		}
	}
	
}