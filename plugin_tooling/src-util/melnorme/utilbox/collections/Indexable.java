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

import java.util.RandomAccess;

import melnorme.utilbox.misc.ArrayUtil;

/**
 * interface for a read-only view of a random access collection
 */
public interface Indexable<E> extends Collection2<E>, RandomAccess {
	
	/** @return the element at given index. */
	E get(int index);
	
	@Override
	public <T> Indexable<T> upcastTypeParameter();
	
	/* ----------------- Some array utils ----------------- */
	
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
		for (int i = 0; i < destArray.length; i++) {
			destArray[i] = get(i);
		}
		return destArray;
	}
	
}