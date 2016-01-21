/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.structure;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.nullToEmpty;
import melnorme.utilbox.collections.Indexable;


public abstract class StructureContainer implements IStructureElementContainer {
	
	protected final Indexable<StructureElement> children;
	
	public StructureContainer(Indexable<? extends StructureElement> _children) {
		this.children = nullToEmpty(_children).upcastTypeParameter();
		
		for(StructureElement child : children) {
			child.setParent(this);
		}
		
		_invariant();
	}
	
	protected void _invariant() {
		for(StructureElement element : getChildren()) {
			assertTrue(element.getParent() == this);
		}
	}
	
	@Override
	public Indexable<StructureElement> getChildren() {
		return children;
	}
	
}