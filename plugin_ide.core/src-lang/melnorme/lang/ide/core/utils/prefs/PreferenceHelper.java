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
import org.osgi.service.prefs.BackingStoreException;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.PreferencesOverride;
import melnorme.utilbox.core.CommonException;
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
	
	protected T defaultValue;
	
	protected final IProjectPreference<Boolean> useProjectPreference;
	
	protected final DomainField<T> field = new DomainField<T>();
	
	public PreferenceHelper(String key, T defaultValue) {
		this(LangCore.PLUGIN_ID, key, defaultValue);
	}
	
	public PreferenceHelper(String pluginId, String key, T defaultValue) {
		this(pluginId, key, defaultValue, true, null);
	}
	
	public PreferenceHelper(String pluginId, String key, T defaultValue, 
			IProjectPreference<Boolean> useProjectPref) {
		this(pluginId, key, defaultValue, true, useProjectPref);
	}
	
	public PreferenceHelper(String pluginId, String key, T defaultValue, boolean ensureUniqueKey,
			IProjectPreference<Boolean> useProjectPreference) {
		this.key = assertNotNull(PreferencesOverride.getKeyIdentifer(key, this));
		if(ensureUniqueKey) {
			checkUniqueKey(key, this);
		}
		this.qualifier = pluginId;
		this.useProjectPreference = useProjectPreference; // can be null
		
		setPreferencesDefaultValue(defaultValue);
		
		updateFieldFromPrefStore();
		
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
	
	public void setPreferencesDefaultValue(T defaultValue) {
		this.defaultValue = defaultValue;
		setPrefValue(getDefaultNode(), defaultValue);
		updateFieldFromPrefStore();
	}
	
	protected void updateFieldFromPrefStore() {
		field.setFieldValue(getFromPrefStore());
	}
	
	protected IPreferencesAccess prefScopes() {
		return new PreferencesLookupHelper(getQualifier());
	}
	
	protected IPreferencesAccess prefScopes(IProject project) {
		return new PreferencesLookupHelper(getQualifier(), project);
	}
	
	protected IEclipsePreferences getDefaultNode() {
		return DefaultScope.INSTANCE.getNode(getQualifier());
	}
	
	public final T getFromPrefStore() {
		return getPrefValue(prefScopes());
	}
	
	protected T getPrefValue(IPreferencesAccess prefsAccess) {
		return getPrefValue(prefsAccess.getString(key));
	}
	
	protected T getPrefValue(String savedValue) {
		return assertNotNull(parseString(savedValue));
	}
	
	protected void setPrefValue(IEclipsePreferences preferences, T value) {
		if(value == null) {
			preferences.remove(key);
		} else {
			preferences.put(key, valueToString(value));
		}
	}
	
	protected abstract T parseString(String stringValue);
	
	protected abstract String valueToString(T value);
	
	protected void initializeListeners() {
		InstanceScope.INSTANCE.getNode(qualifier).addPreferenceChangeListener(event -> handlePreferenceChange(event));
	}
	
	@Override
	public final void setInstanceScopeValue(T value) throws CommonException {
		IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(getQualifier());
		setPrefValue(prefs, value);
		try {
			prefs.flush();
		} catch(BackingStoreException e) {
			throw toCommonException(e);
		}
	}
	
	public static CommonException toCommonException(BackingStoreException e) throws CommonException {
		return new CommonException(e.getMessage(), e.getCause());
	}
	
	protected void handlePreferenceChange(PreferenceChangeEvent event) {
		if(event.getKey().equals(key)) {
			updateFieldFromPrefStore();
		}
	}
	
	public interface IPreferencesAccess {
		
		public String getString(String key);
		
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
		public void setValue(IProject project, T value) throws CommonException {
			try {
				setProjectScopeValue(project, value);
			} catch(BackingStoreException e) {
				toCommonException(e);
			}
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
			return useProjectPreference;
		}
		
	};
	
	protected final T getProjectScopeValue(IProject project) {
		return getPrefValue(prefScopes(project));
	}
	
	protected final void setProjectScopeValue(IProject project, T value) throws BackingStoreException {
		IEclipsePreferences projectPreferences = getProjectNode(project);
		setPrefValue(projectPreferences, value);
		projectPreferences.flush();
	}
	
	protected IEclipsePreferences getProjectNode(IProject project) {
		return new ProjectScope(project).getNode(getQualifier());
	}
	
}