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
package melnorme.utilbox.fields;

import java.util.Iterator;

import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;

/**
 * Helper to manage a listener list, then used to fire events.
 * This class is designed to be thread safe.
 * @see also {@link org.eclipse.core.runtime.ListenerList} 
 */
public class ListenerListHelper<LISTENER> {
	
	private volatile Indexable<LISTENER> listeners;
	
	public ListenerListHelper() {
		this.listeners = new ArrayList2<LISTENER>();
	}
	
	protected ListenerListHelper(Indexable<LISTENER> listeners) {
		this.listeners = listeners; // listeners must be unmodifiable
	}
	
	
	public synchronized Indexable<LISTENER> getListeners() {
		return listeners;
	}
	
	public synchronized void addListener(LISTENER listener) {
		ArrayList2<LISTENER> newListeners = new ArrayList2<>(listeners);
		newListeners.add(listener);
		
		setNewListeners(newListeners);
	}
	
	public synchronized void removeListener(LISTENER listener) {
		ArrayList2<LISTENER> newListeners = new ArrayList2<LISTENER>(listeners);
		for (Iterator<LISTENER> iter = newListeners.iterator(); iter.hasNext(); ) {
			LISTENER iterElem = iter.next();
			if(iterElem == listener) {
				iter.remove();
				break;
			}
		}
		
		setNewListeners(newListeners);
	}
	
	public synchronized void setNewListeners(Indexable<LISTENER> newListeners) {
		listeners = newListeners;
	}
	
	public void clear() {
		setNewListeners(new ArrayList2<LISTENER>());
	}
	
}