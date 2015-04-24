/*******************************************************************************
 * Copyright (c) 2014, 2014 IBM Corporation and others.
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
import java.util.HashSet;

import melnorme.utilbox.core.CoreUtil;
import melnorme.utilbox.misc.ArrayUtil;

/**
 * Extension to {@link java.util.HashSet}, with some helper methods,
 * and implementing a read-only interface.
 */
public class HashSet2<E> extends HashSet<E> implements Collection2<E> {
	
	private static final long serialVersionUID = -7612795787860334443L;

	public HashSet2() {
		super();
	}
	
	public HashSet2(Collection<? extends E> c) {
		super(c);
	}
	
	public HashSet2(int initialCapacity) {
		super(initialCapacity);
	}
	
	@SafeVarargs
	public HashSet2(E... elements) {
		super();
		addElements(elements);
	}
	
	/* -----------------  ----------------- */
	
	@SafeVarargs
	public final HashSet2<E> addElements(E... elements) {
		for (E element : elements) {
			add(element);
		}
		return this;
	}
	
	public final HashSet2<E> addElements(Collection<? extends E> elements) {
		for (E element : elements) {
			add(element);
		}
		return this;
	}
	
	public E[] toArray(Class<E> componentType) {
		return ArrayUtil.createFrom(this, componentType);
	}
	
	@Override
	public <T> HashSet2<T> upcastTypeParameter() {
		return CoreUtil.blindCast(this);
	}
	
	@SuppressWarnings("unused")
	private static void _generics_test() {
		HashSet2<Integer> arrayListExt = new HashSet2<Integer>();
		arrayListExt.<Number>upcastTypeParameter().toArray(Number.class);
		ArrayUtil.createFrom(arrayListExt, Number.class);
	}
	
}