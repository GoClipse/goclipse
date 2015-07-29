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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.function.Function;

import melnorme.utilbox.misc.ArrayUtil;
import melnorme.utilbox.misc.CollectionUtil;

/**
 * An {@link Iterable} with size information.
 */
public interface Collection2<E> extends Iterable<E> {
	
	/** @return the number of elements in this collection */
	int size();
	
	/** @return <tt>true</tt> if this collection contains no elements */
	boolean isEmpty();
	
	/** @return the receiver (this), with the type parameter recast. 
	 * WARNING: This operation is only safe if: 
	 * - The bound T is a supertype of E, and the returned collection is used only for reading. */
	public <T> Collection2<T> upcastTypeParameter();
	
	
	public default <R> ArrayList2<R> map(Function<? super E, ? extends R> evalFunction) {
		return CollectionUtil.map(this, evalFunction);
	}
	
	/* ----------------- Some array utils ----------------- */
	
	default ArrayList2<E> toArrayList() {
		return new ArrayList2<>(this);
	}
	
	default Object[] toArray() {
		return this.<Object>upcastTypeParameter().toArray(Object.class);
	}
	
	default E[] toArray(Class<E> componentType) {
		E[] newArray = ArrayUtil.create(size(), componentType);
		copyToArray(newArray);
	    return newArray;
	}
	
	// Subclasses can reimplement with more optimized versions (ie, array lists for example)
	default Object[] copyToArray(Object[] destArray) {
		assertTrue(destArray.length == size());
		int i = 0;
		for(E element : this) {
			destArray[i++] = element;
		}
		return destArray;
	}
	
}