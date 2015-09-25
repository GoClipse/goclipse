/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
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
import java.util.Map;

/**
 * A map that automatically creates its entries. Not synchronized.
 */
public abstract class EntriesMap<KEY, ENTRY> {
	
	protected final Map<KEY, ENTRY> map = createBackingMap();
	
	protected HashMap<KEY, ENTRY> createBackingMap() {
		return new HashMap<>();
	}
	
	/** @return the underlying map, for direct access. */
	public Map<KEY, ENTRY> getMap() {
		return map;
	}
	
	public ENTRY getEntry(KEY key) {
		
		ENTRY entry;
		
		if(map.containsKey(key)) {
			entry = map.get(key);
		} else {
			entry = createEntry(key);
			map.put(key, entry);
		}
		
		return entry;
	}
	
	public ENTRY getEntryOrNull(KEY key) {
		return map.get(key);
	}
	
	protected abstract ENTRY createEntry(KEY key);
	
}