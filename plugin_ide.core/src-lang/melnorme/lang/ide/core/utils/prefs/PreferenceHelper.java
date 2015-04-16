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
import melnorme.lang.ide.core.LangCore;
import melnorme.utilbox.ownership.IDisposable;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;

public abstract class PreferenceHelper<T> extends AbstractPreferenceHelper {
	
	public final String qualifier;
	protected final T defaultValue;
	
	public PreferenceHelper(String key, T defaultValue) {
		this(LangCore.PLUGIN_ID, key, defaultValue);
	}
	
	public PreferenceHelper(String pluginId, String key, T defaultValue) {
		super(key);
		this.qualifier = pluginId;
		this.defaultValue = assertNotNull(defaultValue);
		
		initializeDefaultValueInDefaultScope();
	}
	
	public final String getQualifier() {
		return qualifier;
	}
	
	public T getDefault() {
		return defaultValue;
	}
	
	protected PreferencesLookupHelper combinedScopes() {
		return new PreferencesLookupHelper(getQualifier());
	}
	
	protected PreferencesLookupHelper combinedScopes(IProject project) {
		return new PreferencesLookupHelper(getQualifier(), project);
	}
	
	protected void initializeDefaultValueInDefaultScope() {
		doSet(getDefaultNode(), defaultValue);
	}
	
	protected IEclipsePreferences getDefaultNode() {
		return DefaultScope.INSTANCE.getNode(getQualifier());
	}
	
	protected abstract void doSet(IEclipsePreferences projectPreferences, T value);
	
	public final T get() {
		return assertNotNull(doGet(combinedScopes()));
	}
	
	public final T get(IProject project) {
		return assertNotNull(doGet(combinedScopes(project)));
	}
	
	protected abstract T doGet(PreferencesLookupHelper combinedPrefs);
	
	public final void set(T value) {
		doSet(InstanceScope.INSTANCE.getNode(getQualifier()), value);
	}
	
	public final void set(IProject project, T value) throws BackingStoreException {
		IEclipsePreferences projectPreferences = getProjectNode(project);
		doSet(projectPreferences, value);
		projectPreferences.flush();
	}
	
	public IEclipsePreferences getProjectNode(IProject project) {
		return new ProjectScope(project).getNode(getQualifier());
	}
	
	/* ----------------- listeners ----------------- */
	
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