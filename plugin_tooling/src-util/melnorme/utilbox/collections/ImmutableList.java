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
package melnorme.utilbox.collections;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

public interface ImmutableList<E> extends Indexable<E>, List<E> {
	
	/* -----------------  ----------------- */
	
	@Override
	default int indexOf(Object o) {
		return Indexable.super.indexOf(o);
	}
	
	@Override
	default int lastIndexOf(Object o) {
		return Indexable.super.lastIndexOf(o);
	}
	
	@Override
	default boolean contains(Object obj) {
		return Indexable.super.contains(obj);
	}
	
	@Override
	default Object[] toArray() {
		return Indexable.super.toArray();
	}
	
	@Override
	default <T> T[] toArray(T[] a) {
		return Indexable.super.toArray(a);
	}
	
	@Override
	default ListIterator<E> listIterator() {
		return Indexable.super.listIterator();
	}
	
	@Override
	default ListIterator<E> listIterator(int index) {
		return Indexable.super.listIterator(index);
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public default boolean add(E e) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public default boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public default boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public default boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public default boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public default boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public default void clear() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public default E set(int index, E element) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public default void add(int index, E element) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public default E remove(int index) {
		throw new UnsupportedOperationException();
	}
	
	/* ----------------- TODO support these other list methods ----------------- */
	
	@Override
	public default boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public default List<E> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}
	
}