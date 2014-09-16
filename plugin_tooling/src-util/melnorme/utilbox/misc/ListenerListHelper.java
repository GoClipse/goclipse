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
package melnorme.utilbox.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Helper to manage a listener list, then used to fire events.
 * This class is designed to be thread safe.
 * @see also {@link org.eclipse.core.runtime.ListenerList} 
 */
public class ListenerListHelper<LISTENER> implements IListenerList<LISTENER> {
	
	private volatile List<LISTENER> listeners;
	
	public ListenerListHelper() {
		this.listeners = Collections.unmodifiableList(new ArrayList<LISTENER>());
	}
	
	protected ListenerListHelper(List<LISTENER> listeners) {
		this.listeners = listeners; // listeners must be unmodifiable
	}
	
	@Override
	public void addListener(LISTENER listener) {
		ArrayList<LISTENER> newListeners = new ArrayList<>(listeners);
		newListeners.add(listener);
		
		setNewListeners(newListeners);
	}
	
	@Override
	public void removeListener(LISTENER listener) {
		ArrayList<LISTENER> newListeners = new ArrayList<LISTENER>(listeners);
		for (Iterator<LISTENER> iter = newListeners.iterator(); iter.hasNext(); ) {
			LISTENER iterElem = iter.next();
			if(iterElem == listener) {
				iter.remove();
				break;
			}
		}
		
		setNewListeners(newListeners);
	}
	
	private void setNewListeners(ArrayList<LISTENER> newListeners) {
		synchronized(this) {
			listeners = newListeners;
		}
	}
	
	@Override
	public List<LISTENER> getListeners() {
		return listeners;
	}
	
}