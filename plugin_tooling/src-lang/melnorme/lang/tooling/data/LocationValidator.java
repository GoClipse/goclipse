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
package melnorme.lang.tooling.data;


public class LocationValidator extends PathValidator implements IFieldValidator {
	
	public LocationValidator(String fieldNamePrefix) {
		super(fieldNamePrefix);
	}
	
	public LocationValidator(String fieldNamePrefix, LocationKind locKind) {
		super(fieldNamePrefix, locKind);
	}
	
	@Override
	public Object getValidatedField(String pathString) throws ValidationException {
		return getValidatedLocation(pathString);
	}
	
}