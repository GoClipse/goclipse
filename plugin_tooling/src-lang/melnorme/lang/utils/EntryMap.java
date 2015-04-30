/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.utils;

import java.util.HashMap;

/**
 * Map that automatically create entries. 
 * Not synchronized.
 * TODO There likely is be a better, more standard name for this, but I'm aware what.
 */
public abstract class EntryMap<KEY, ENTRY> {
	
	protected final HashMap<KEY, ENTRY> map = new HashMap<>();
	
	public ENTRY getEntry(KEY key) {
		ENTRY entry = map.get(key);
		if(entry == null) {
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