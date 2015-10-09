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
package melnorme.lang.ide.core.utils.prefs;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.HashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.osgi.service.prefs.BackingStoreException;

import melnorme.lang.ide.core.LangCore;
import melnorme.utilbox.fields.DomainField;

public abstract class PreferenceHelper<T> implements IGlobalPreference<T> {
	
	protected static final HashMap<String, PreferenceHelper<?>> instances = new HashMap<>();
	
	public static void checkUniqueKey(String key, PreferenceHelper<?> preference) {
		synchronized (instances) {
			// Ensure uniqueness: allow only one instance of a preference helper per key.
			assertTrue(instances.containsKey(key) == false);
			instances.put(key, preference);
		}
	}
	
	public final String key;
	public final String qualifier;
	
	protected T defaultValue;
	
	protected final IProjectPreference<Boolean> useProjectSettingsPref;
	
	protected final DomainField<T> field = new DomainField<>();
	
	public PreferenceHelper(String key, T defaultValue) {
		this(LangCore.PLUGIN_ID, key, defaultValue);
	}
	
	public PreferenceHelper(String pluginId, String key, T defaultValue) {
		this(pluginId, key, defaultValue, true, null);
	}
	
	public PreferenceHelper(String pluginId, String key, T defaultValue, 
			IProjectPreference<Boolean> useProjectSettingsPref) {
		this(pluginId, key, defaultValue, true, useProjectSettingsPref);
	}
	
	public PreferenceHelper(String pluginId, String key, T defaultValue, boolean ensureUniqueKey,
			IProjectPreference<Boolean> useProjectSettingsPref) {
		this.key = assertNotNull(key);
		if(ensureUniqueKey) {
			checkUniqueKey(key, this);
		}
		this.qualifier = pluginId;
		this.useProjectSettingsPref = useProjectSettingsPref; // can be null
		
		setDefaultValue(defaultValue);
		
		initializeListener();
		field.setFieldValue(get());
	}
	
	protected void initializeListener() {
		InstanceScope.INSTANCE.getNode(qualifier).addPreferenceChangeListener(event -> handlePreferenceChange(event));
	}
	
	public final String getQualifier() {
		return qualifier;
	}
	
	public T getDefault2() {
		return defaultValue;
	}
	
	@Override
	public DomainField<T> getGlobalField() {
		return field;
	}
	
	protected IPreferencesAccess combinedScopes() {
		return new PreferencesLookupHelper(getQualifier());
	}
	
	protected IPreferencesAccess combinedScopes(IProject project) {
		return new PreferencesLookupHelper(getQualifier(), project);
	}
	
	public void setDefaultValue(T defaultValue) {
		assertNotNull(defaultValue);
		this.defaultValue = defaultValue;
		doSet(getDefaultNode(), defaultValue);
	}
	
	protected IEclipsePreferences getDefaultNode() {
		return DefaultScope.INSTANCE.getNode(getQualifier());
	}
	
	protected abstract void doSet(IEclipsePreferences projectPreferences, T value);
	
	public final T get() {
		return assertNotNull(doGet(combinedScopes()));
	}
	
	public final T getFrom(IPreferenceStore prefStore) {
		return doGet(new PreferenceStoreAccess(prefStore));
	}
	
	protected abstract T doGet(IPreferencesAccess prefsAccess);
	
	public final void set(T value) {
		doSet(InstanceScope.INSTANCE.getNode(getQualifier()), value);
	}
	
	protected void handlePreferenceChange(PreferenceChangeEvent event) {
		if(event.getKey().equals(key)) {
			field.setFieldValue(get());
		}
	}
	
	/* -----------------  ----------------- */
	
	public IProjectPreference<T> getProjectPreference() {
		return projectPreference;
	}
	
	protected final IProjectPreference<T> projectPreference = new IProjectPreference<T>() {
		
		@Override
		public PreferenceHelper<T> getGlobalPreference() {
			return PreferenceHelper.this;
		}
		
		@Override
		public T getDefault() {
			return PreferenceHelper.this.getDefault2();
		}
		
		@Override
		public T getStoredValue(IProject project) {
			return get(project);
		}
		
		@Override
		public void setValue(IProject project, T value) throws BackingStoreException {
			set(project, value);
		}
		
		@Override
		public T getEffectiveValue(IProject project) {
			assertNotNull(getEnableProjectSettingPref());
			if(project != null && getEnableProjectSettingPref().getStoredValue(project) == true) {
				return getStoredValue(project);
			}
			return getGlobalPreference().get(); 
		}
		
		@Override
		public IProjectPreference<Boolean> getEnableProjectSettingPref() {
			return useProjectSettingsPref;
		}
		
	};
	
	protected final T get(IProject project) {
		return assertNotNull(doGet(combinedScopes(project)));
	}
	
	protected final void set(IProject project, T value) throws BackingStoreException {
		IEclipsePreferences projectPreferences = getProjectNode(project);
		doSet(projectPreferences, value);
		projectPreferences.flush();
	}
	
	protected IEclipsePreferences getProjectNode(IProject project) {
		return new ProjectScope(project).getNode(getQualifier());
	}
	
}