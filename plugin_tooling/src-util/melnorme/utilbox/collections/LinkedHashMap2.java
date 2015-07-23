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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import melnorme.utilbox.core.CoreUtil;

/**
 * Extension to {@link java.util.LinkedHashMap}, with some helper methods,
 * and implementing the read-only interface {@link Collection2}.
 */
public class LinkedHashMap2<K, V> extends LinkedHashMap<K, V> implements Collection2<Map.Entry<K, V>> {
	
	private static final long serialVersionUID = -7612795787860334443L;
	
	public LinkedHashMap2() {
		super();
	}
	
	/**
	 * Possible problem here: modification through Entry {@link Entry#setValue(Object) }
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