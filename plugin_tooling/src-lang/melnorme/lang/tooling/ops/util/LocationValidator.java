/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.ops.util;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.nio.file.Path;

import melnorme.lang.tooling.data.IValidator;
import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.data.ValidationException;
import melnorme.utilbox.misc.Location;

public class LocationValidator extends PathValidator implements IValidator<String, Path> {
	
	public final IValidator<String, Location> asLocationValidator = new IValidator<String, Location>() {
		@Override
		public Location getValidatedField(String value) throws StatusException {
			return getValidatedLocation(value);
		}
	};
	
	public LocationValidator(String fieldNamePrefix) {
		super(fieldNamePrefix);
	}
	
	public LocationValidator(String fieldNamePrefix, LocationKind locKind) {
		super(fieldNamePrefix, locKind);
	}
	
	@Override
	public Path getValidatedField(String pathString) throws ValidationException {
		return getValidatedLocation(pathString).path;
	}
	
	public IValidator<String, Location> asLocationValidator() {
		return asLocationValidator;
	}
	
	public static class LocationValidator2 implements IValidator<String, Location> {
		
		protected final LocationValidator validator;
		
		public LocationValidator2(String fieldNamePrefix) {
			this(new LocationValidator(fieldNamePrefix));
		}
		
		public LocationValidator2(LocationValidator pathValidator) {
			this.validator = assertNotNull(pathValidator);
		}
		
		@Override
		public Location getValidatedField(String pathString) throws StatusException {
			return validator.getValidatedLocation(pathString);
		}
		
	}
	
}