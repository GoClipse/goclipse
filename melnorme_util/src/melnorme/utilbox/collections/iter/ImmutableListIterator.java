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
package melnorme.utilbox.collections.iter;

import java.util.ListIterator;

import melnorme.utilbox.collections.Indexable;

public class ImmutableListIterator<E> implements ListIterator<E> {
	
	protected final Indexable<E> indexable;
	protected int cursor = 0;
	
	public ImmutableListIterator(Indexable<E> indexable, int index) {
		this.indexable = indexable;
		this.cursor = index;
	}
	
	@Override
	public boolean hasNext() {
		return cursor < indexable.size();
	}
	
	@Override
	public E next() {
		return indexable.get(cursor++);
	}
	
	@Override
	public boolean hasPrevious() {
		return cursor > 0;
	}
	
	@Override
	public E previous() {
		return indexable.get(cursor--);
	}
	
	@Override
	public int nextIndex() {
		return cursor;
	}
	
	@Override
	public int previousIndex() {
		return cursor - 1;
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void set(E e) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void add(E e) {
		throw new UnsupportedOperationException();
	}
	
}