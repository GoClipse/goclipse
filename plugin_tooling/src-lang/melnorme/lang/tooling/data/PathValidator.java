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

public class PathValidator extends AbstractValidator implements IFieldValidator {

	public final String fieldNamePrefix;
	
	public boolean canBeEmpty;
	public boolean fileOnly = false;
	public boolean directoryOnly = false;
	
	public PathValidator(String fieldNamePrefix) {
		this.fieldNamePrefix = fieldNamePrefix;
		canBeEmpty = false;
	}
	
	@Override
	protected String getFullMessage(String simpleMessage) {
		return fieldNamePrefix + " " + super.getFullMessage(simpleMessage);
	}
	
	@Override
	public Object getValidatedField(String pathString) throws ValidationException {
		return getValidatedPath(pathString);
	}
	
	public Path getValidatedPath(String pathString) throws ValidationException {
		return validatePath(createPath(pathString));
	}
	
	protected Path createPath(String pathString) throws ValidationException {
		if(pathString.isEmpty()) {
			if(canBeEmpty) {
				return null;
			}
			throw createException(StatusLevel.WARNING, ValidationMessages.Path_EmptyPath());
		}
		
		Path path = PathUtil.createPathOrNull(pathString);
		if(path == null) {
			throw createException(StatusLevel.ERROR, ValidationMessages.Path_InvalidPath(pathString));
		}
		return path;
	}
	
	protected Path validatePath(Path path) throws ValidationException {
		if(path.isAbsolute()) {
			return PathUtil.toPath(getValidatedLocation(path));
		}
		return validateRelativePath(path);
	}
	
	protected Path validateRelativePath(Path path) throws ValidationException {
		return path;
	}
	
	public Location getValidatedLocation(String pathString) throws ValidationException {
		return getValidatedLocation(createPath(pathString));
	}
	
	protected Location getValidatedLocation(Path path) throws ValidationException {
		Location location;
		try {
			location = Location.create2(path);
		} catch (CommonException ce) {
			throw error_NotAbsolute(path);
		}
		
		return validateLocation(location);
	}
	
	protected ValidationException error_NotAbsolute(Path path) throws ValidationException {
		return createException(StatusLevel.ERROR, ValidationMessages.Location_NotAbsolute(path));
	}
	
	protected Location validateLocation(Location location) throws ValidationException {
		if(!location.toFile().exists()) {
			throw createException(StatusLevel.WARNING, ValidationMessages.Location_DoesntExist(location));
		}
		
		validateType(location);
		return getValidatedField_rest(location);
	}
	
	public void validateType(Location location) throws ValidationException {
		if(fileOnly && !location.toFile().isFile()) {
			throw createException(StatusLevel.WARNING, ValidationMessages.Location_NotAFile(location));
		}
		if(directoryOnly && !location.toFile().isDirectory()) {
			throw createException(StatusLevel.WARNING, ValidationMessages.Location_NotADirectory(location));
		}
	}
	
	protected Location getValidatedField_rest(Location location) throws ValidationException {
		return location;
	}
	
}