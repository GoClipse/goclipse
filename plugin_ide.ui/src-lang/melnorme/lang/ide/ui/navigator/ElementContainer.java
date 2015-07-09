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
package melnorme.lang.ide.ui.navigator;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;

public class ElementContainer<ELEM extends ElementContainer<?>> implements INavigatorElement_Actual {
	
	protected final ArrayList2<ELEM> children;
	
	protected Object parent;
	
	public ElementContainer(ArrayList2<ELEM> children) {
		children = children == null ? new ArrayList2<>() : children;
		this.children = children;
		
		parentizeChildren(children);
	}
	
	protected void parentizeChildren(ArrayList2<ELEM> children) {
		for(ELEM buildTargetElement : children) {
			buildTargetElement.setParent(this);
		}
	}
	
	public Object getParent() {
		return parent;
	}
	
	public void setParent(Object newParent) {
		assertTrue(this.parent == null); // Cannot reset parent
		this.parent = newParent;
	}
	
	public Indexable<ELEM> getChildren() {
		return children;
	}
	
	public Object[] getChildren_toArray() {
		return children.toArray();
	}
	
}