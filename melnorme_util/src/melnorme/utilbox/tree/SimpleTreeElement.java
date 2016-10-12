/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.tree;


public class SimpleTreeElement implements IElement {
	
	protected final IElement[] children; // Can be null
	protected final IElement parent;  // Can be null
	
	public SimpleTreeElement(IElement parent, IElement[] children) {
		this.parent = parent;
		this.children = children;
	}
	
	@Override
	public boolean hasChildren() {
		return children != null && children.length > 0;
	}
	
	@Override
	public IElement[] getChildren() {
		return children == null ? NO_ELEMENTS : children;
	}
	
	@Override
	public IElement getParent() {
		return parent;
	}
	
}