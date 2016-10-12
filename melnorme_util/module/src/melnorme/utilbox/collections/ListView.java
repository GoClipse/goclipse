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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.List;

import melnorme.utilbox.core.CoreUtil;

public abstract class ListView<E> extends CollectionView<E> implements Indexable<E> {
	
	public static <E> ListView<E> create(List<E> list) {
		return new ListViewImpl<>(list);
	}
	
	/* -----------------  ----------------- */
	
	protected final List<E> list;
	
	protected ListView(List<E> list) {
		super(list);
		this.list = assertNotNull(list);
	}
	
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
	public <T> ListView<T> upcastTypeParameter() {
		return CoreUtil.blindCast(this);
	}

	@Override
	public E get(int index) {
		return list.get(index);
	}
	
}

class ListViewImpl<E> extends ListView<E> implements ImmutableList<E> {
	
	public ListViewImpl(List<E> list) {
		super(list);
	}
	
}