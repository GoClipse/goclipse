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
 * Thread-safe version of {@link EntryMap}
 */
public abstract class EntryMapTS<KEY, ENTRY> extends EntryMap<KEY, ENTRY> {
	
	protected final HashMap<KEY, ENTRY> map = new HashMap<>();
	
	@Override
	public ENTRY getEntry(KEY key) {
		synchronized(this) {
			return super.getEntry(key);
		}
	}
	
	@Override
	public ENTRY getEntryOrNull(KEY key) {
		synchronized(this) {
			return super.getEntryOrNull(key);
		}
	}
	
	@Override
	public ENTRY removeEntry(KEY key) {
		synchronized(this) {
			return super.removeEntry(key);
		}
	}
	
}