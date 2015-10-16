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
import melnorme.lang.ide.core.PreferencesOverride;
import melnorme.utilbox.fields.DomainField;
import melnorme.utilbox.fields.IFieldView;

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
	protected final T defaultValue;
	
	protected final IProjectPreference<Boolean> useProjectSettingsPref;
	
	protected final DomainField<T> field = new DomainField<T>();
	
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
		this.key = assertNotNull(PreferencesOverride.getKeyIdentifer(key, this));
		if(ensureUniqueKey) {
			checkUniqueKey(key, this);
		}
		this.qualifier = pluginId;
		this.useProjectSettingsPref = useProjectSettingsPref; // can be null
		
		this.defaultValue = PreferencesOverride.getDefaultValue(defaultValue, this);
		setPreferencesDefaultValue();
		
		field.setFieldValue(getFromPrefStore());
		
		initializeListeners();
	}
	
	public final String getQualifier() {
		return qualifier;
	}
	
	@Override
	public T getDefaultValue() {
		return defaultValue;
	}
	
	@Override
	public IFieldView<T> asField() {
		return field;
	}
	
	/* -----------------  ----------------- */
	
	protected IPreferencesAccess prefScopes() {
		return new PreferencesLookupHelper(getQualifier());
	}
	
	protected IPreferencesAccess prefScopes(IProject project) {
		return new PreferencesLookupHelper(getQualifier(), project);
	}
	
	protected void setPreferencesDefaultValue() {
		doSet(getDefaultNode(), defaultValue);
	}
	
	protected IEclipsePreferences getDefaultNode() {
		return DefaultScope.INSTANCE.getNode(getQualifier());
	}
	
	public final T getFromPrefStore() {
		return assertNotNull(doGet(prefScopes()));
	}
	
	public final T getFrom(IPreferenceStore prefStore) {
		return doGet(new PreferenceStoreAccess(prefStore));
	}
	
	protected abstract T doGet(IPreferencesAccess prefsAccess);
	
	protected abstract void doSet(IEclipsePreferences preferences, T value);
	
	protected void initializeListeners() {
		InstanceScope.INSTANCE.getNode(qualifier).addPreferenceChangeListener(event -> handlePreferenceChange(event));
	}
	
	@Override
	public final void setInstanceScopeValue(T value) throws BackingStoreException {
		IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(getQualifier());
		doSet(prefs, value);
		prefs.flush();
	}
	
	protected void handlePreferenceChange(PreferenceChangeEvent event) {
		if(event.getKey().equals(key)) {
			field.setFieldValue(getFromPrefStore());
		}
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public IProjectPreference<T> getProjectPreference() {
		return projectPreference;
	}
	
	protected final IProjectPreference<T> projectPreference = new IProjectPreference<T>() {
		
		@Override
		public PreferenceHelper<T> getGlobalPreference() {
			return PreferenceHelper.this;
		}
		
		@Override
		public T getDefaultValue() {
			return PreferenceHelper.this.getDefaultValue();
		}
		
		@Override
		public T getStoredValue(IProject project) {
			return getProjectScopeValue(project);
		}
		
		@Override
		public void setValue(IProject project, T value) throws BackingStoreException {
			setProjectScopeValue(project, value);
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
	
	protected final T getProjectScopeValue(IProject project) {
		return assertNotNull(doGet(prefScopes(project)));
	}
	
	protected final void setProjectScopeValue(IProject project, T value) throws BackingStoreException {
		IEclipsePreferences projectPreferences = getProjectNode(project);
		doSet(projectPreferences, value);
		projectPreferences.flush();
	}
	
	protected IEclipsePreferences getProjectNode(IProject project) {
		return new ProjectScope(project).getNode(getQualifier());
	}
	
}