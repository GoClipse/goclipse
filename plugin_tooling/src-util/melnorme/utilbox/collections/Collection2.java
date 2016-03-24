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

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
import java.util.function.Predicate;

import melnorme.utilbox.core.fntypes.FunctionX;
import melnorme.utilbox.misc.ArrayUtil;
import melnorme.utilbox.misc.CollectionUtil;

/**
 * An {@link Iterable} with size information.
 * 
 * This is essentially the same as {@link Collection}, but without the methods that modify the underlying collection.
 * Indeed, its main purpose is as a replacement for {@link Collection}, 
 * providing <b>statically</b> checked read-only access. 
 * This is preferable to {@link Collections#unmodifiableCollection(Collection)} because that one only checks at runtime.
 */
public interface Collection2<E> extends Iterable<E> {
	
	/** @return the number of elements in this collection */
	int size();
	
	/** @return <tt>true</tt> if this collection contains no elements */
	boolean isEmpty();
	
	/** @return the receiver (this), with the type parameter recast. 
	 * WARNING: This operation is only safe if: 
	 * - The bound T is a supertype of E, and the returned collection is used only for reading. */
	<T> Collection2<T> upcastTypeParameter();
	
	
	/* ----------------- utility methods ----------------- */
	
	default <R> ArrayList2<R> map(Function<? super E, ? extends R> evalFunction) {
		return CollectionUtil.map(this, evalFunction);
	}
	
	default <R, EXC extends Exception> ArrayList2<R> mapx(FunctionX<? super E, ? extends R, EXC> evalFunction) 
			throws EXC {
		return CollectionUtil.mapx(this, evalFunction);
	}
	
	/**
	 * @return the index in the iteration order of the first element that matches given predicate, or -1 otherwise.
	 * Note that if the iteration order of this collection is not stable, then the index isn't either.
	 */
	default int indexUntil(Predicate<? super E> predicate) {
		return CollectionUtil.indexUntil(iterator(), predicate);
	}
	
	/* ----------------- Some array utility methods ----------------- */
	
	default ArrayList2<E> toArrayList() {
		return new ArrayList2<>(this);
	}
	
	default Object[] toArray() {
		return this.<Object>upcastTypeParameter().toArray(Object.class);
	}
	
	@SuppressWarnings("unchecked")
	default <T> T[] toArray(T[] a) {
		Class<T> componentType = (Class<T>) a.getClass().getComponentType();
		return this.<T>upcastTypeParameter().toArray(componentType);
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