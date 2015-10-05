/*******************************************************************************
 * Copyright (c) 2014, 2014 IBM Corporation and others.
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
import static melnorme.utilbox.core.CoreUtil.array;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;

public class PreferencesLookupHelper implements IPreferencesAccess {
	
	public final String qualifier;
	protected final IScopeContext[] contexts;
	
	public PreferencesLookupHelper(String qualifier) {
		this(qualifier, null);
	}
	
	public PreferencesLookupHelper(String qualifier, IProject project) {
		this.qualifier = qualifier;
		
		if(project != null) {
			contexts = array(new ProjectScope(project), InstanceScope.INSTANCE, DefaultScope.INSTANCE);
		} else {
			contexts = array(InstanceScope.INSTANCE, DefaultScope.INSTANCE);
		}
	}
	
	protected static IPreferencesService preferences() {
		return Platform.getPreferencesService();
	}
	
	protected String assertKeyHasDefault(String key) {
		return assertNotNull(DefaultScope.INSTANCE.getNode(qualifier).get(key, null));
	}
	
	@Override
	public String getString(String key) {
		assertKeyHasDefault(key);
		return getString(key, "");
	}
	
	@Override
	public int getInt(String key) {
		assertKeyHasDefault(key);
		return getInt(key, 0);
	}
	
	@Override
	public boolean getBoolean(String key) {
		assertKeyHasDefault(key);
		return getBoolean(key, false);
	}
	
	public String getString(String key, String defaultValue) {
		return preferences().getString(qualifier, key, defaultValue, contexts);
	}
	
	public int getInt(String key, int defaultValue) {
		return preferences().getInt(qualifier, key, defaultValue, contexts);
	}
	
	public boolean getBoolean(String key, boolean defaultValue) {
		return preferences().getBoolean(qualifier, key, defaultValue, contexts);
	}
	
}