/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
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

import melnorme.lang.tooling.data.IValidatableValue;
import melnorme.lang.tooling.data.IValidator;
import melnorme.lang.tooling.data.StatusException;

public class DerivedValuePreference<VALUE> {
	
	protected final StringPreference preference;
	protected final IValidator<String, VALUE> validator;
	
	public DerivedValuePreference(String pluginId, String key, String defaultValue, 
			IProjectPreference<Boolean> useProjectPref /* can be null*/,
			IValidator<String, VALUE> validator) {
		this(new StringPreference(pluginId, key, defaultValue, useProjectPref), validator);
	}
	
	public DerivedValuePreference(StringPreference preference, IValidator<String, VALUE> validator) {
		this.preference = assertNotNull(preference);
		this.validator = assertNotNull(validator);
	}
	
	public StringPreference getPreference() {
		return preference;
	}
	
	public IValidator<String, VALUE> getValidator() {
		return validator;
	}
	
	public IValidatableValue<VALUE> getValidatableValue(IProject project) {
		return () -> getDerivedValue(project);
	}
	
	public VALUE getDerivedValue() throws StatusException {
		return validator.getValidatedField(preference.get());
	}
	
	public VALUE getDerivedValue(IProject project) throws StatusException {
		String stringValue = preference.getProjectPreference().getEffectiveValue(project);
		return validator.getValidatedField(stringValue);
	}
	
}