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

import melnorme.lang.tooling.data.IValidator;
import melnorme.lang.tooling.data.StatusException;

public class DerivedValuePreference<VALUE> extends StringPreference {
	
	protected final IValidator<String, VALUE> validator;
	
	public DerivedValuePreference(String pluginId, String key, String defaultValue, 
			IProjectPreference<Boolean> useProjectSettings /* can be null*/,
			IValidator<String, VALUE> validator) {
		super(pluginId, key, defaultValue, useProjectSettings);
		this.validator = assertNotNull(validator);
	}
	
	public IValidator<String, VALUE> getValidator() {
		return validator;
	}
	
	public VALUE getDerivedValue(IProject project) throws StatusException {
		String stringValue = getProjectPreference().getEffectiveValue(project);
		return validator.getValidatedField(stringValue);
	}
	
}