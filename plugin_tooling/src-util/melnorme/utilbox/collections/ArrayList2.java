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

import java.util.ArrayList;
import java.util.Collection;

import melnorme.utilbox.misc.ArrayUtil;

/**
 * Extension to {@link java.util.ArrayList}, with some helper methods,
 * and implementing a read-only interface.
 */
public class ArrayList2<E> extends ArrayList<E> implements Indexable<E> {
	
	private static final long serialVersionUID = -9212035453691203439L;
	
	public ArrayList2() {
		super();
	}
	
	public ArrayList2(Collection<? extends E> c) {
		super(c);
	}
	
	public ArrayList2(int initialCapacity) {
		super(initialCapacity);
	}
	
	@SafeVarargs
	public ArrayList2(E... elements) {
		super();
		addElements(elements);
	}
	
	/* -----------------  ----------------- */
	
	@SafeVarargs
	public final ArrayList2<E> addElements(E... elements) {
		for (E element : elements) {
			add(element);
		}
		return this;
	}
	
	public final ArrayList2<E> addElements(Collection<? extends E> elements) {
		for (E element : elements) {
			add(element);
		}
		return this;
	}
	
	public void addIfNotNull(E element) {
		if(element != null) {
			add(element);
		}
	}
	
	public E[] toArray(Class<E> componentType) {
		return ArrayUtil.createFrom(this, componentType);
	}
	
	/** Cast the type parameter to something. This operation is only safe if the underlying collection 
	 * is used only for reading (for a type parameter upcast) or writing (for a type parameter upcast).*/
	@SuppressWarnings("unchecked")
	public <T> ArrayList2<T> castTypeParam() {
		return (ArrayList2<T>) this;
	}
	
	@SuppressWarnings("unused")
	private static void _generics_test() {
		ArrayList2<Integer> arrayListExt = new ArrayList2<Integer>();
		arrayListExt.<Number>castTypeParam().toArray(Number.class);
		ArrayUtil.createFrom(arrayListExt, Number.class);
	}
	
}