/*******************************************************************************
 * Copyright (c) 2013 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.collections.iter;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.List;
import java.util.RandomAccess;

/**
 * A simple no-modification list iterator that implements the {@link ICopyableIterator} API.
 * Will likely exhibit bad performance if underlying list is not {@link RandomAccess}.
 */
public class CopyableListIterator<T> implements ICopyableIterator<T> {
	
	public static <U> CopyableListIterator<U> create(List<U> list) {
		return new CopyableListIterator<U>(list);
	}
	
	protected final List<T> list;
	protected int index = 0;
	
	public CopyableListIterator(List<T> list) {
		this(list, 0);
	}
	
	public CopyableListIterator(List<T> list, int index) {
		this.list = assertNotNull(list);
		this.index = index;
	}
	
	@Override
	public boolean hasNext() {
		return index < list.size();
	}
	
	@Override
	public T next() {
		return list.get(index++);
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ICopyableIterator<T> copyState() {
		return new CopyableListIterator<T>(list, index);
	}
	
	@Override
	public ICopyableIterator<T> optimizedSelf() {
		return this;
	}
	
}