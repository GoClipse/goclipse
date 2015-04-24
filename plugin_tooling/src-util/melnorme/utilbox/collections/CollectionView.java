/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
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
import java.util.Iterator;

import melnorme.utilbox.core.CoreUtil;

public class CollectionView<K> implements Collection2<K> {
	
	protected final Collection<K> coll;
	
	public CollectionView(Collection<K> coll) {
		this.coll = coll;
	}

	@Override
	public Iterator<K> iterator() {
		return coll.iterator();
	}
	
	@Override
	public int size() {
		return coll.size();
	}
	
	@Override
	public boolean isEmpty() {
		return coll.isEmpty();
	}
	
	@Override
	public <T> CollectionView<T> upcastTypeParameter() {
		return CoreUtil.blindCast(this);
	}
	
}