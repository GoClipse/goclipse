/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.fields;


import melnorme.utilbox.collections.Indexable;

public class EventSource<T> {
	
	protected final ListenerListHelper<T> listeners = new ListenerListHelper<>();
	
	public EventSource() {
		super();
	}
	
	public void addListener(T listener) {
		listeners.addListener(listener);
	}
	
	public void removeListener(T listener) {
		listeners.removeListener(listener);
	}
	
	protected Indexable<T> getListeners() {
		return listeners.getListeners();
	}
	
}