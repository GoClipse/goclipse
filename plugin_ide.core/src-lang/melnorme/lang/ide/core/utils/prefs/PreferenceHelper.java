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
package melnorme.lang.ide.core.utils.prefs;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.HashMap;

import melnorme.lang.ide.core.LangCore;
import melnorme.utilbox.ownership.IDisposable;

import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.core.runtime.preferences.InstanceScope;

public abstract class PreferenceHelper<T> {
	
	protected static final HashMap<String, PreferenceHelper<?>> instances = new HashMap<>();
	
	public final String pluginId;
	public final String key;
	protected final T defaultValue;
	
	public PreferenceHelper(String key, T defaultValue) {
		this(LangCore.PLUGIN_ID, key, defaultValue);
	}
	
	public PreferenceHelper(String pluginId, String key, T defaultValue) {
		this.pluginId = pluginId;
		this.key = key;
		this.defaultValue = assertNotNull(defaultValue);
		
		initializeDefaultValueInDefaultScope();
		
		synchronized (instances) {
			// Allow only one instance of a preference helper per key.
			assertTrue(instances.containsKey(key) == false);
			instances.put(key, this);
		}
	}
	
	public final String getQualifier() {
		return pluginId;
	}
	
	protected PreferencesLookupHelper prefs() {
		return new PreferencesLookupHelper(pluginId);
	}
	
	public abstract T get();
	
	protected abstract void initializeDefaultValueInDefaultScope();
	
	protected IEclipsePreferences getDefaultPreferences() {
		return DefaultScope.INSTANCE.getNode(getQualifier());
	}
	
	public IPreferenceChangeListener_Ext addPrefChangeListener(boolean initializeChange, 
			final IPrefChangeListener listener) {
		final IEclipsePreferences node = InstanceScope.INSTANCE.getNode(getQualifier());
		IPreferenceChangeListener_Ext prefListener = new IPreferenceChangeListener_Ext() {
			
			@Override
			public void preferenceChange(PreferenceChangeEvent event) {
				if(event.getKey().equals(key)) {
					listener.handleChange();
				}
			}
			
			@Override
			public void dispose() {
				node.removePreferenceChangeListener(this);
			}
		};
		node.addPreferenceChangeListener(prefListener);
		
		if(initializeChange) {
			listener.handleChange();
		}
		
		return prefListener;
	}
	
	public static interface IPreferenceChangeListener_Ext extends IPreferenceChangeListener, IDisposable {
		
	}
	
}