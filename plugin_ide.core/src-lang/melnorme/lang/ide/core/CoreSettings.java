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
package melnorme.lang.ide.core;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.option;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.core.operations.ToolchainPreferences;
import melnorme.lang.ide.core.utils.prefs.IProjectPreference;
import melnorme.lang.ide.core.utils.prefs.PreferenceHelper;
import melnorme.lang.tooling.data.IValidator;
import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.ops.util.PathValidator;
import melnorme.utilbox.misc.Location;

public abstract class CoreSettings {
	
	public final SettingsField<Path> SDK_LOCATION;
	
	public CoreSettings() {
		SDK_LOCATION = newPathPreference(ToolchainPreferences.SDK_PATH, getSDKLocationValidator());
	}
	
	/**
	 * @return the SDK location validator. In most cases this value is a location, 
	 * but for some languages a relative path may be allowed.
	 */
	public abstract PathValidator getSDKLocationValidator();
	
	public static SettingsField<Path> newPathPreference(
			IProjectPreference<String> pref, IValidator<String, Path> validator) {
		return new SettingsField<>(pref, validator, (path) -> path.toString());
	}
	
	public static SettingsField<Location> newLocationPreference(
			IProjectPreference<String> pref, IValidator<String, Location> validator) {
		return new SettingsField<>(pref, validator, (loc) -> loc.toString());
	}
	
	public static class SettingsField<TYPE> {
		// TODO: actually make this a Field
		
		public final IProjectPreference<String> preference;
		public final IValidator<String, TYPE> validator;
		public final IValidator<String, String> validator_toString;
		
		public SettingsField(IProjectPreference<String> preference, IValidator<String, TYPE> validator, 
				Function<TYPE, String> backToString) {
			this.preference = assertNotNull(preference);
			this.validator = assertNotNull(validator);
			this.validator_toString = (value) -> backToString.apply(validator.getValidatedField(value));
		}
		
		public PreferenceHelper<String> getGlobalPreference() {
			return preference.getGlobalPreference();
		}
		public IProjectPreference<String> getProjectPreference() {
			return preference;
		}
		public IProjectPreference<Boolean> getEnableProjectSettingsPref() {
			return getProjectPreference().getEnableProjectSettingPref();
		}
		
		public IValidator<String, TYPE> getValidator() {
			return validator;
		}
		
		public IValidator<String, String> getValidator_toString() {
			return validator_toString;
		}
		
		/* ----------------- ----------------- */
		
		public TYPE getValue() throws StatusException {
			return validator.getValidatedField(preference.getGlobalPreference().get());
		}
		public TYPE getValue(IProject project) throws StatusException {
			return getValue(option(project));
		}
		public TYPE getValue(Optional<IProject> project) throws StatusException {
			return validator.getValidatedField(preference.getEffectiveValue(project));
		}
		
		public final String getRawValue(IProject project) {
			return getRawValue(option(project));
		}
		public String getRawValue(Optional<IProject> project) {
			return preference.getEffectiveValue(project);
		}
		
		public Supplier<String> getRawValueSupplier(Optional<IProject> project) {
			if(project.isPresent()) {
				return preference.getEffectiveValueProperty(project);
			} else {
				return preference.getGlobalPreference();
			}
		}
		
		public void doSetRawValue(IProject project, String value) {
			preference.doSetValue(project, value);
		}
		
	}
	
}