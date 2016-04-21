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
import static melnorme.utilbox.core.CoreUtil.option;

import org.eclipse.core.resources.IProject;

import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.data.validation.ValidatedValueSource;
import melnorme.lang.tooling.data.validation.Validator;

public class DerivedValuePreference<VALUE> {
	
	protected final StringPreference preference;
	protected final Validator<String, VALUE> validator;
	
	public DerivedValuePreference(String pluginId, String key, String defaultValue, 
			IProjectPreference<Boolean> useProjectPref /* can be null*/,
			Validator<String, VALUE> validator) {
		this(new StringPreference(pluginId, key, defaultValue, useProjectPref), validator);
	}
	
	public DerivedValuePreference(StringPreference preference, Validator<String, VALUE> validator) {
		this.preference = assertNotNull(preference);
		this.validator = assertNotNull(validator);
	}
	
	public StringPreference getPreference() {
		return preference;
	}
	
	public Validator<String, VALUE> getValidator() {
		return validator;
	}
	
	public ValidatedValueSource<VALUE> getValidatableValue(IProject project) {
		return () -> getDerivedValue(project);
	}
	
	public VALUE getDerivedValue() throws StatusException {
		return validator.validateField(preference.get());
	}
	
	public VALUE getDerivedValue(IProject project) throws StatusException {
		String stringValue = preference.getProjectPreference().getEffectiveValue(option(project));
		return validator.validateField(stringValue);
	}
	
}