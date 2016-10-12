/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.collections;

import java.util.Map;
import java.util.Set;

/**
 * A read-only view of a map, based on {@link Map}
 */
public interface MapAccess<K,V> extends Collection2<Map.Entry<K, V>> {
	
    /** See {@link Map#containsKey(Object)}  */
    boolean containsKey(Object key);
    
    /** See {@link Map#containsValue(Object)} */
    boolean containsValue(Object value);
    
    /** See {@link Map#get(Object)}*/
    V get(Object key);
    
    /** See {@link Map#getOrDefault(Object, Object)}*/
    default V getOrDefault(Object key, V defaultValue) {
        V v;
        return (((v = get(key)) != null) || containsKey(key))
            ? v
            : defaultValue;
    }

    /** See {@link Map#entrySet()}. WARNING: Clients must not modify this set. */
    Set<Map.Entry<K, V>> entrySet();
    
    
    /* -----------------  ----------------- */
    
    /**
     * @return a copy of this collection.
     */
    public HashMap2<K, V> copyToHashMap();
    
}