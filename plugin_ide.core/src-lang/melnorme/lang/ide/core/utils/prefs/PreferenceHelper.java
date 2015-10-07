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

public abstract class PreferenceHelper<T> extends AbstractPreferenceHelper implements IGlobalPreference<T> {
	
	public final String qualifier;
	protected final T defaultValue;
	
	protected final DomainField<T> field = new DomainField<>();
	
	public PreferenceHelper(String key, T defaultValue) {
		this(LangCore.PLUGIN_ID, key, defaultValue);
	}
	
	public PreferenceHelper(String pluginId, String key, T defaultValue) {
		super(key);
		this.qualifier = pluginId;
		this.defaultValue = assertNotNull(defaultValue);
		
		initializeDefaultValueInDefaultScope();
		
		initializeListener();
		field.setFieldValue(get());
	}
	
	protected void initializeListener() {
		InstanceScope.INSTANCE.getNode(qualifier).addPreferenceChangeListener(event -> handlePreferenceChange(event));
	}
	
	public final String getQualifier() {
		return qualifier;
	}
	
	public T getDefault() {
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
	
	public final void set(IProject project, T value) throws BackingStoreException {
		IEclipsePreferences projectPreferences = getProjectNode(project);
		doSet(projectPreferences, value);
		projectPreferences.flush();
	}
	
	public IEclipsePreferences getProjectNode(IProject project) {
		return new ProjectScope(project).getNode(getQualifier());
	}
	
}