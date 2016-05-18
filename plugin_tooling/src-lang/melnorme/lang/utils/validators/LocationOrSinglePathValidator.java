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
package melnorme.lang.utils.validators;

import java.nio.file.Path;

import melnorme.utilbox.fields.validation.ValidationException;
import melnorme.utilbox.status.Severity;

public class LocationOrSinglePathValidator extends PathValidator {
	
	public LocationOrSinglePathValidator(String fieldNamePrefix) {
		super(fieldNamePrefix);
		fileOnly = true;
	}
	
	@Override
	protected Path validateRelativePath(Path path) throws ValidationException {
		if(path.getNameCount() != 1) {
			throw createException(Severity.ERROR, ValidationMessages.Path_NotAbsoluteNorSingle(path));
		}
		
		return path;
	}
	
}