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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;

import melnorme.utilbox.core.CoreUtil;
import melnorme.utilbox.core.fntypes.Callable2;
import melnorme.utilbox.misc.CollectionUtil;

/**
 * Extension to {@link java.util.ArrayList}, it implements {@link Indexable},
 * and also adds several short utility methods (mostly syntax-sugar).
 */
public class ArrayList2<E> extends ArrayList<E> implements Indexable<E> {
	
	public static <E> ArrayList2<E> create() {
		return new ArrayList2<>();
	}
	
	@SafeVarargs
	public static <E> ArrayList2<E> createFrom(E... elements) {
		ArrayList2<E> arrayList = new ArrayList2<>(elements.length);
		arrayList.addElements(elements);
		return arrayList;
	}
	
	/* -----------------  ----------------- */
	
	private static final long serialVersionUID = -9212035453691203439L;
	
	public ArrayList2() {
		super();
	}
	
	public ArrayList2(Collection<? extends E> coll) {
		super(coll);
	}
	
	public ArrayList2(Collection2<? extends E> coll) {
		super(coll.size());
		addAll2(coll);
	}
	
	public ArrayList2(Iterable<? extends E> coll) {
		super();
		addAll2(coll);
	}
	
	public ArrayList2(int initialCapacity) {
		super(initialCapacity);
	}
	
	@SafeVarargs
	public static <E> ArrayList2<E> create(E... elements) {
		return new ArrayList2<E>(elements.length).addElements(elements);
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public boolean equals(Object obj) {
		return Indexable.equals(this, obj);
	}
	
	@Override
	public int hashCode() {
		return Indexable.hashCode(this);
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public <T> Indexable<T> upcastTypeParameter() {
		return CoreUtil.blindCast(this);
	}
	
	@Override
	public Object[] copyToArray(Object[] destArray) {
		return toArray(destArray);
	}
	
	/* -----------------  ----------------- */
	
	@SafeVarargs
	public final ArrayList2<E> addElements(E... elements) {
		for (E element : elements) {
			add(element);
		}
		return this;
	}
	
	public final ArrayList2<E> addAll2(Iterable<? extends E> iterable) {
		return iteratorAddAll(iterable.iterator());
	}
	
	public final ArrayList2<E> addAll2(Stream<? extends E> stream) {
		return iteratorAddAll(stream.iterator());
	}
	
	public ArrayList2<E> iteratorAddAll(Iterator<? extends E> iterator) {
		return CollectionUtil.addAllFromIterator(this, iterator);
	}
	
	public <SOURCE> ArrayList2<E> addAll(Collection2<SOURCE> source, Function<SOURCE, E> mapper) {
		return CollectionUtil.addAll(this, source, mapper);
	}
	
	public ArrayList2<E> addIfNotNull(E element) {
		if(element != null) {
			add(element);
		}
		return this;
	}
	
	public <EXC extends Throwable> void collectUntilNull(Callable2<? extends E, EXC> supplier)
		throws EXC 
	{
		while(true) {
			E newElement = supplier.invoke();
			if(newElement == null) {
				return;
			} else {
				add(newElement);
			}
		}
	}
	
	@SuppressWarnings("unused")
	private static void _generics_test() {
		ArrayList2<Integer> arrayListExt = new ArrayList2<Integer>();
		Indexable<Number> other = arrayListExt.upcastTypeParameter();
	}
	
}