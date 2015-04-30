/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.util.swt.jface;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.jface.viewers.ITreeContentProvider;


public abstract class AbstractTreeContentProvider extends AbstractContentProvider implements ITreeContentProvider {
	
	protected Object[] EMPTY_CHILDREN = new Object[0];
	
	@Override
	public Object[] getElements(Object inputElement) {
		assertTrue(input == inputElement);
		Object[] elements = getChildren(inputElement);
		return elements == null ? EMPTY_CHILDREN : elements;
	}
	
	@Override
	public abstract Object[] getChildren(Object parentElement);
	
	@Override
	public abstract boolean hasChildren(Object parentElement);
	
}