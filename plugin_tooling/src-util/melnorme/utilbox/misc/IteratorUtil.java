/*******************************************************************************
 * Copyright (c) 2007 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial implementation
 *******************************************************************************/
package melnorme.utilbox.misc;


import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;

import melnorme.utilbox.collections.ChainedIterable;

public class IteratorUtil { 
	
	public static final Iterator<?> EMPTY_ITERATOR = Collections.EMPTY_LIST.iterator();
	
	/** @return an empty iterator */
	@SuppressWarnings("unchecked")
	public static <E> Iterator<E> emptyIterator() {
		return (Iterator<E>) EMPTY_ITERATOR;
	}
	
	/** @return an iterable with no elements. */
	public static <E> Iterable<E> emptyIterable() {
		return Collections.EMPTY_LIST;
	}
	
	/** Recasts the type parameter of given iterator to a more specific type.
	 * Safe to do if the returned iterator is used in a read only way with regards to the underlying collection.
	 * @return the recasted iterator. */
	@SuppressWarnings("unchecked")
	public static <E> Iterator<E> recast(Iterator<? extends E> iterator) {
		return ((Iterator<E>) iterator);
	}
	
	public static <E> Iterator<E> singletonIterator(E obj) {
		return Collections.singletonList(obj).iterator();
	}
	
	/** @return the iterator for given iterable, or an empty {@link Iterator} if iterable is null */
	public static <E> Iterator<E> nonNullIterator(Iterable<E> iterable) {
		return iterable == null ? Collections.<E>emptyIterator() : iterable.iterator();
	}
	
	/** @return the given iterable, or an empty {@link Iterable} if given iterable is null */
	public static <E> Iterable<E> nonNullIterable(Iterable<E> iterable) {
		return iterable == null ? Collections.<E>emptyList() : iterable;
	}
	
	/** @return an {@link Iterable} for given array. A null array is treated as if it's an empty one. */
	@SafeVarargs
	public static <E> Iterable<E> iterable(E... array) {
		if(array == null) {
			return Collections.EMPTY_LIST;
		}
		return Arrays.asList(array);
	}
	
	/** @return the given iterable if non-null, an empty iterable otherwise. */
	public static <E> Iterable<E> iterable(Iterable<E> iterable) {
		if(iterable == null) {
			return Collections.EMPTY_LIST;
		}
		return iterable;
	}
	
	/** @return combine two {@link Iterable}s to form a single chained iterable. 
	 * Each given {@link Iterable} can be null, in which case they will be treated as empty. */
	public static <E> Iterable<? extends E> chainedIterable(
		Iterable<? extends E> firstIter, Iterable<? extends E> secondIter) {
		
		if(firstIter != null && secondIter != null) {
			return new ChainedIterable<E>(firstIter, secondIter);
		} else if(firstIter != null) {
			return firstIter;
		} else if(secondIter != null) {
			return secondIter;
		} else {
			return emptyIterable();
		}
	}
	
	/* -----------------  ----------------- */
	
	/** @return the element the given listIterator currently refers to. (uses #previous and #next) */
	public static <E> E getCurrentElement(ListIterator<E> listIterator) {
		listIterator.previous();
		return listIterator.next();
	}
	
}