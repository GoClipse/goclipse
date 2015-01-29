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

/**
 * Extension to {@link java.util.HashMap}, with some helper methods,
 * and implementing a read-only interface.
 */
public class HashMap2<K, V> extends HashMap<K, V> implements Collection2<Map.Entry<K, V>> {
	
	private static final long serialVersionUID = -7612795787860334443L;
	
	public HashMap2() {
		super();
	}
	
	/**
	 * Possible problem here: modification through {@link Entry#setValue(Object)
	 */
	@Override
	public Iterator<Map.Entry<K, V>> iterator() {
		return entrySet().iterator();
	}
	
	/* -----------------  ----------------- */
	
	protected final Collection2<K> keysView = new Collection2<K>() {
		
		@Override
		public Iterator<K> iterator() {
			return keySet().iterator();
		}
		
		@Override
		public int size() {
			return keySet().size();
		}
		
		@Override
		public boolean isEmpty() {
			return HashMap2.this.isEmpty();
		}
	};
	
	public Collection2<K> getKeysView() {
		return keysView;
	}
	
	protected final Collection2<V> valuesView = new Collection2<V>() {
		
		@Override
		public Iterator<V> iterator() {
			return values().iterator();
		}
		
		@Override
		public int size() {
			return values().size();
		}
		
		@Override
		public boolean isEmpty() {
			return HashMap2.this.isEmpty();
		}
	};
	
	public Collection2<V> getValuesView() {
		return valuesView;
	}
	
}