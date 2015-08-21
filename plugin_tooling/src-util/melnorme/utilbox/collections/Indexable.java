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
package melnorme.utilbox.collections;

import static melnorme.utilbox.core.CoreUtil.areEqual;

import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

import melnorme.utilbox.collections.iter.ImmutableListIterator;
import melnorme.utilbox.misc.CollectionUtil;

/**
 * Interface for a read-only view of an ordered, indexable, random access collection.
 * 
 * Also specifies a default equals/hashcode semantics, that implementers should use.
 * 
 */
public interface Indexable<E> extends Collection2<E>, RandomAccess {
	
	/** @return the element at given index. */
	E get(int index);
	
	@Override
	public <T> Indexable<T> upcastTypeParameter();
	
	/* ----------------- equals/hashcode ----------------- */
	
	public static boolean equals(Indexable<?> collA, Object other) {
		if(collA == other) return true;
		
		if(other instanceof Indexable) {
			return indexableEquals(collA, (Indexable<?>) other);
		}
		if(collA instanceof List && other instanceof List) {
			return CollectionUtil.listEquals((List<?>) collA, (List<?>) other);
		}
		return false;
	}
	
	public static boolean indexableEquals(Indexable<?> coll1, Indexable<?> coll2) {
		if(coll1.size() != coll2.size()) {
			return false;
		}
		
		return CollectionUtil.iterationEquals(coll1.iterator(), coll2.iterator());
	}
	
	public static int hashCode(Indexable<?> indexable) {
        int hashCode = 1;
        for(Object element : indexable)
            hashCode = 31*hashCode + (element==null ? 0 : element.hashCode());
        return hashCode;
	}
	
	/* -----------------  ----------------- */
	
	public default int indexOf(Object obj) {
		for(int ix = 0; ix < size(); ix++) {
			if(areEqual(obj, get(ix))) {
				return ix;
			}
		}
		return -1;
	}
	
	public default int lastIndexOf(Object obj) {
		for(int ix = size() - 1; ix >= 0; ix--) {
			if(areEqual(obj, get(ix))) {
				return ix;
			}
		}
		return -1;
	}
	
	public default boolean contains(Object obj) {
		return indexOf(obj) != -1;
	}
	

	/* ----------------- Iteration ----------------- */
	
	public default ListIterator<E> listIterator() {
		return new ImmutableListIterator<>(this, 0);
	}
	
	public default ListIterator<E> listIterator(int index) {
		return new ImmutableListIterator<>(this, index);
	}
	
	/* -----------------  ----------------- */
	
	public static <T> Indexable<T> nullToEmpty(Indexable<T> indexable) {
		return indexable == null ? CollectionUtil.EMPTY_INDEXABLE : indexable;
	}
	
	@SuppressWarnings("rawtypes")
	public static final Indexable EMPTY_INDEXABLE = new ArrayList2<>();
	
}