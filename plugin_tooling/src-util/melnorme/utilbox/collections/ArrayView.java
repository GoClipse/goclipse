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
package melnorme.utilbox.collections;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

import melnorme.utilbox.core.CoreUtil;
import melnorme.utilbox.misc.ArrayUtil;

/**
 * A simple immutable array collection (RandomAccess, Iterable). 
 */
public class ArrayView<E> implements Indexable<E> {
	
	public static final ArrayView<?> EMPTY_ARRAYVIEW = new ArrayView<Object>(new Object[0]);
	
	@SafeVarargs
	public static <T> ArrayView<T> createFrom(T... arr){
		return new ArrayView<T>(arr);
	}
	
	public static <T> ArrayView<T> create(T[] arr){
		return new ArrayView<T>(arr);
	}
	
	protected final E[] array;
	
	public ArrayView(E[] array) {
		assertNotNull(array);
		this.array = array;
	}
	
	@Override
	public <T> ArrayView<T> upcastTypeParameter() {
		return CoreUtil.blindCast(this);
	}
	
	@Override
	public final int size() {
		return array.length;
	}
	
	@Override
	public final boolean isEmpty() {
		return array.length == 0;
	}
	
	@Override
	public final E get(int index) {
		return array[index];
	}
	
	public final boolean contains(Object o) {
		return ArrayUtil.contains(array, o);
	}
	
	@Override
	public final Iterator<E> iterator() {
		return new ArrayIterator();
	}
	
	public final class ArrayIterator implements Iterator<E> {
		int index = 0;
		
		public ArrayIterator() { }
		
		@Override
		public boolean hasNext() {
			return index < array.length;
		}
		
		@Override
		public E next() throws NoSuchElementException {
			if (!hasNext())
				throw new NoSuchElementException();
			return array[index++];
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	/* -----------------  ----------------- */
	
	/** Accesses and returns the *internal* array backing this {@link ArrayView}, which must not be modified!
	 * Warning: this method is not type safe, it can break if the backing array has a component type that is not E 
	 * and yet it is assigned to a E[] variable. */
	public final E[] getInternalArray() {
		return array;
	}
	
	@SuppressWarnings("unchecked")
	public final <T> T[] _getInternalArray(@SuppressWarnings("unused") Class<T> componentType) {
		return (T[]) array;
	}
	
}