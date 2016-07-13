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
package com.googlecode.goclipse.core;

import java.nio.file.Path;

import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.utils.prefs.DerivedValuePreference;
import melnorme.lang.ide.core.utils.prefs.OptionalStringPreference;
import melnorme.lang.utils.validators.LocationOrSinglePathValidator;
import melnorme.lang.utils.validators.PathValidator;
import melnorme.utilbox.fields.validation.ValidationException;
import melnorme.utilbox.status.Severity;

public interface GoToolPreferences {
	
	public class GoToolValidator extends LocationOrSinglePathValidator {
		public GoToolValidator(String fieldNamePrefix) {
			/* FIXME: */
			// TODO: refactor to fieldName
			super(fieldNamePrefix);
		}
		
		@Override
		protected String getFullMessage(String simpleMessage) {
			return super.getFullMessage(simpleMessage);
		}
		
		@Override
		protected ValidationException createIsEmptyException() {
			String baseMsg = fieldNamePrefix + " is not configured."; 
			String detailsMsg = 
					"The " + fieldNamePrefix + " setting needs to be configured in the " + 
					linkGoPreferences() + ".\n" + "See the " + linkUserGuide() + " for more information.";
			return new ValidationException(Severity.WARNING, baseMsg, detailsMsg);
		}
		
		protected String linkGoPreferences() {
			return linkReference("Go preferences", "pref:" + LangCore_Actual.TOOLS_PREF_PAGE_ID);
		}
		
		protected String linkUserGuide() {
			return linkReference("UserGuide", LangCore_Actual.USER_GUIDE_LINK);
		}
		
		public static String linkReference(String text, String target) {
			return "<a href=\"" + target + "\">" + text + "</a>";
		}
	}
	
	PathValidator GURU_PATH_validator = new GoToolValidator("guru path");
	PathValidator GODEF_PATH_validator = new GoToolValidator("godef path");
	PathValidator GOFMT_PATH_validator = new GoToolValidator("gofmt path");
	
	
	DerivedValuePreference<Path> GO_GURU_Path = new DerivedValuePreference<Path>( 
		"GoToolPreferences.GO_GURU_Path", "", null, 
		GURU_PATH_validator);
	
	DerivedValuePreference<Path> GODEF_Path = new DerivedValuePreference<Path>( 
		"GoToolPreferences.godef.Path", "", null, 
		GODEF_PATH_validator);
	
	DerivedValuePreference<Path> GOFMT_Path = new DerivedValuePreference<Path>(
		new OptionalStringPreference("GoToolPreferences.GOFMT_Path", null), 
		GOFMT_PATH_validator);
	
}