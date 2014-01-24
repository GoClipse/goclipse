/*******************************************************************************
 * Copyright (c) 2007 DSource.org and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial implementation
 *******************************************************************************/
package melnorme.utilbox.misc;


import java.util.Collections;
import java.util.Iterator;

public class IteratorUtil { 
	
	public static final Iterator<?> EMPTY_ITERATOR = Collections.EMPTY_LIST.iterator();
	
	/** @return an empty iterator */
	@SuppressWarnings("unchecked")
	public static <T> Iterator<T> emptyIterator() {
		return (Iterator<T>) EMPTY_ITERATOR;
	}
	
	/** Recasts the type parameter of given iterator to a more specific type.
	 * Safe to do if the returned iterator is used in a read only way with regards to the underlying collection.
	 * @return the recasted iterator. */
	@SuppressWarnings("unchecked")
	public static <T> Iterator<T> recast(Iterator<? extends T> iterator) {
		return ((Iterator<T>) iterator);
	}
	
	public static <T> Iterator<T> singletonIterator(T obj) {
		return Collections.singletonList(obj).iterator();
	}
	
	/** @return the iterator for given iterable, or an empty {@link Iterator} if iterable is null */
	public static <T> Iterator<T> nonNullIterator(Iterable<T> iterable) {
		return iterable == null ? Collections.<T>emptyIterator() : iterable.iterator();
	}
	
	/** @return the given iterable, or an empty {@link Iterable} if given iterable is null */
	public static <T> Iterable<T> nonNullIterable(Iterable<T> iterable) {
		return iterable == null ? Collections.<T>emptyList() : iterable;
	}
	
}