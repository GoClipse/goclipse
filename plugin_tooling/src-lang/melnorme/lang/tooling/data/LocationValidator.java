/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.data;

import java.nio.file.Path;

import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.PathUtil;

public class LocationValidator extends AbstractValidator implements IFieldValidator<Location> {

	public boolean canBeEmpty;
	public boolean fileOnly;
	public boolean directoryOnly;
	
	public LocationValidator() {
		canBeEmpty = false;
	}
	
	@Override
	public Location getValidatedField(String pathString) throws StatusException {
		
		if(pathString.isEmpty()) {
			if(canBeEmpty) {
				return null;
			}
			throw new StatusException(StatusLevel.WARNING, ValidationMessages.Path_Error_EmptyPath);
		}
		
		Path path = PathUtil.createPathOrNull(pathString);
		if(path == null) {
			throw new StatusException(StatusLevel.ERROR, ValidationMessages.Path_InvalidPath(pathString));
		}
		
		return validatePath(path);
	}
	
	protected Location validatePath(Path path) throws StatusException {
		Location location;
		try {
			location = Location.create2(path);
		} catch (CommonException ce) {
			throw new StatusException(StatusLevel.ERROR, ValidationMessages.Location_NotAbsolute(path));
		}
		
		if(!location.toFile().exists()) {
			throw new StatusException(StatusLevel.WARNING, ValidationMessages.Location_DoesntExist(location));
		}
		
		validateType(location);
		return getValidatedField_rest(location);
	}
	
	public void validateType(Location location) throws StatusException {
		if(fileOnly && !location.toFile().isFile()) {
			throw new StatusException(StatusLevel.WARNING, ValidationMessages.Location_NotAFile(location));
		}
		if(directoryOnly && !location.toFile().isDirectory()) {
			throw new StatusException(StatusLevel.WARNING, ValidationMessages.Location_NotADirectory(location));
		}
	}
	
	protected Location getValidatedField_rest(Location location) throws StatusException {
		return location;
	}
	
}