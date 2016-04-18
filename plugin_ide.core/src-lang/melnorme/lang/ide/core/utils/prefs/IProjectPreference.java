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

import java.util.Optional;
import java.util.function.Supplier;

import org.eclipse.core.resources.IProject;

import melnorme.utilbox.core.CommonException;


public interface IProjectPreference<T> {
	
	PreferenceHelper<T> getGlobalPreference();
	
	default String getKey() {
		return getGlobalPreference().key;
	}
	
	T getDefaultValue();
	
	T getStoredValue(Optional<IProject> project);
	
	void doSetValue(IProject project, T value);
	
	void setValue(IProject project, T value) throws CommonException;
	
	T getEffectiveValue(Optional<IProject> project);
	
	IProjectPreference<Boolean> getEnableProjectSettingPref();
	
	Supplier<T> getProperty(Optional<IProject> project);
	
}