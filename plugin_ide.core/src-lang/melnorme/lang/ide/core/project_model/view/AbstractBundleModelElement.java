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
package melnorme.lang.ide.core.project_model.view;

import static melnorme.utilbox.core.CoreUtil.areEqual;

import melnorme.utilbox.misc.MiscUtil;

public abstract class AbstractBundleModelElement<PARENT> implements IBundleModelElement {
	
	protected final PARENT parent;
	
	public AbstractBundleModelElement(PARENT parent) {
		this.parent = parent;
	}
	
	@Override
	public boolean equals(Object other) {
		if(this == other) {
			return true;
		}
		if(!(other instanceof IBundleModelElement)) {
			return false;
		}
		IBundleModelElement otherElement = (IBundleModelElement) other;
		
		if(this.getElementType() != otherElement.getElementType()) {
			return false;
		}
		if(!areEqual(this.getElementName(), otherElement.getElementName())) {
			return false;
		}
		
		return areEqual(this.getParent(), otherElement.getParent());
	}
	
	@Override
	public int hashCode() {
		return MiscUtil.combineHashCodes(getParent().hashCode(), getElementName().hashCode());
	}
	
	@Override
	public String toString() {
		return getPathString() + "  #" + getClass().getSimpleName();
	}
	@Override
	public PARENT getParent() {
		return parent;
	}
	
	@Override
	public boolean hasChildren() {
		return false;
	}
	@Override
	public Object[] getChildren() {
		return null;
	}
	
}