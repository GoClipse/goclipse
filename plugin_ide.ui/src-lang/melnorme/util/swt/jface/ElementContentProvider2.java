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
package melnorme.util.swt.jface;

import melnorme.utilbox.tree.IElement;

public class ElementContentProvider2 extends AbstractTreeContentProvider {
	
	@Override
	public boolean hasChildren(Object parentElement) {
		if(parentElement instanceof IElement) {
			IElement element = (IElement) parentElement;
			return element.hasChildren();
		}
		return false;
	}
	
	@Override
	public Object[] getChildren(Object inputElement) {
		if(inputElement instanceof IElement) {
			IElement element = (IElement) inputElement;
			return element.getChildren();
		}
		return null;
	}
	
	@Override
	public Object getParent(Object object) {
		if(object instanceof IElement) {
			IElement element = (IElement) object;
			return element.getParent();
		}
		return null;
	}
	
}