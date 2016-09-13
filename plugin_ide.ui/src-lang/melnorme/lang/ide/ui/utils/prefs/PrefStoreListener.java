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
package melnorme.lang.ide.ui.utils.prefs;

import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import melnorme.utilbox.ownership.IDisposable;

public abstract class PrefStoreListener implements IPropertyChangeListener, IDisposable {
	
	public static void addBoundPrefStoreListener(IPreferenceStore preferenceStore, List<IDisposable> owned,
			IPropertyChangeListener listener) {
		PrefStoreListener boundListener = new PrefStoreListener(preferenceStore) {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				listener.propertyChange(event);
			}
		};
		
		if(owned != null) {
			owned.add(boundListener);
		}
	}
	
	protected final IPreferenceStore preferenceStore;
	
	public PrefStoreListener(IPreferenceStore preferenceStore) {
		this.preferenceStore = preferenceStore;
		
		preferenceStore.addPropertyChangeListener(this);
	}
	
	@Override
	public void dispose() {
		preferenceStore.removePropertyChangeListener(this);
	}
	
	@Override
	public abstract void propertyChange(PropertyChangeEvent event);
	
}