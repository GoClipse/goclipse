/*******************************************************************************
 * Copyright (c) 2013 Bruno Medeiros and other Contributors.
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


public class ChainedIterable<E> implements Iterable<E> {
	
	public static <E> ChainedIterable<E> create(Iterable<? extends E> firstIter, Iterable<? extends E> secondIter) {
		return new ChainedIterable<E>(firstIter, secondIter);
	}
	
	protected final Iterable<? extends E> first;
	protected final Iterable<? extends E> second;
	
	public ChainedIterable(Iterable<? extends E> first, Iterable<? extends E> second) {
		this.first = assertNotNull(first);
		this.second = assertNotNull(second);
	}
	
	@Override
	public Iterator<E> iterator() {
		return new SimpleChainedIterator<>(first.iterator(), second.iterator());
	}
	
	/** Cast the type parameter to something. This operation is only safe if the underlying collection 
	 * is used only for reading (for a type parameter upcast) or writing (for a type parameter upcast).*/
	@SuppressWarnings("unchecked")
	public <T> ChainedIterable<T> castTypeParam() {
		return (ChainedIterable<T>) this;
	}
	
}