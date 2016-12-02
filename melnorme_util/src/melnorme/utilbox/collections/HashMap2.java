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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import melnorme.utilbox.core.CoreUtil;

/**
 * Extension to {@link java.util.HashMap}, with some helper methods,
 * and implementing the read-only interface {@link Collection2}.
 */
public class HashMap2<K, V> extends HashMap<K, V> implements MapAccess<K, V> {
	
	private static final long serialVersionUID = -7612795787860334443L;
	
	public HashMap2() {
		super();
	}
	
	public HashMap2(Map<? extends K, ? extends V> map) {
		super(map);
	}
	
	public V get0(K key) {
		return get(key);
	}
	
	@Override
	public HashMap2<K, V> copyToHashMap() {
		return copy();
	}
	
	public HashMap2<K, V> copy() {
		return new HashMap2<K, V>(this);
	}
	
	/* -----------------  ----------------- */
	
	/**
	 * Possible problem here: modification through {@link Entry#setValue(Object)
	 */
	@Override
	public Iterator<Map.Entry<K, V>> iterator() {
		return entrySet().iterator();
	}
	
	@Override
	public <T> Collection2<T> upcastTypeParameter() {
		return CoreUtil.blindCast(this);
	}
	
	/* -----------------  ----------------- */
	
	protected final Collection2<K> keysView = new CollectionView<K>(keySet());
	
	public Collection2<K> getKeysView() {
		return keysView;
	}
	
	protected final Collection2<V> valuesView = new CollectionView<V>(values());
	
	public Collection2<V> getValuesView() {
		return valuesView;
	}
	
}