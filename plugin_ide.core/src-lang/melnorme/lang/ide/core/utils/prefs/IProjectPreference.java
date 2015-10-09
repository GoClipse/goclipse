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

import org.eclipse.core.resources.IProject;
import org.osgi.service.prefs.BackingStoreException;


public interface IProjectPreference<T> {
	
	PreferenceHelper<T> getGlobalPreference();
	
	T getDefault();
	
	T getStoredValue(IProject project);
	
	void setValue(IProject project, T value) throws BackingStoreException;
	
	T getEffectiveValue(IProject project);
	
}