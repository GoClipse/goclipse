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
package melnorme.lang.ide.core.utils.prefs;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.array;

import java.util.Optional;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;

import melnorme.lang.ide.core.utils.prefs.PreferenceHelper.IPreferencesAccess;
import melnorme.utilbox.misc.MiscUtil;

public class PreferencesLookupHelper implements IPreferencesAccess {
	
	public final String qualifier;
	protected final IScopeContext[] contexts;
	
	public PreferencesLookupHelper(String qualifier) {
		this(qualifier, null);
	}
	
	public PreferencesLookupHelper(String qualifier, Optional<IProject> project) {
		this.qualifier = qualifier;
		
		project = MiscUtil.toOptional(project);
		if(project.isPresent()) {
			contexts = array(new ProjectScope(project.get()), InstanceScope.INSTANCE, DefaultScope.INSTANCE);
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
	
	public String getString(String key, String defaultValue) {
		return preferences().getString(qualifier, key, defaultValue, contexts);
	}
	
}