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
package melnorme.lang.tooling.ops.util;

import java.nio.file.Path;

import melnorme.lang.tooling.data.AbstractValidatorExt;
import melnorme.lang.tooling.data.IValidator;
import melnorme.lang.tooling.data.Severity;
import melnorme.lang.tooling.data.ValidationException;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.PathUtil;

public class PathValidator extends AbstractValidatorExt implements IValidator<String, Path> {
	
	public static enum LocationKind { ANY, FILE_ONLY, DIR_ONLY }
	
	/* -----------------  ----------------- */
	
	public final String fieldNamePrefix;
	
	public boolean canBeEmpty;
	public boolean fileOnly = false;
	public boolean directoryOnly = false;
	
	public PathValidator(String fieldNamePrefix) {
		this(fieldNamePrefix, LocationKind.ANY);
	}
	
	public PathValidator(String fieldNamePrefix, LocationKind locKind) {
		this.fieldNamePrefix = fieldNamePrefix;
		this.canBeEmpty = false;
		
		this.fileOnly = locKind == LocationKind.FILE_ONLY;
		this.directoryOnly = locKind == LocationKind.DIR_ONLY;
	}
	
	public PathValidator setFileOnly(boolean fileOnly) {
		this.fileOnly = fileOnly;
		return this;
	}
	
	public PathValidator setDirectoryOnly(boolean directoryOnly) {
		this.directoryOnly = directoryOnly;
		return this;
	}
	
	protected LocationKind getLocationKind() {
		return fileOnly ?
				LocationKind.FILE_ONLY :
				directoryOnly ? 
				LocationKind.DIR_ONLY :
				LocationKind.ANY;
	}
	
	@Override
	protected String getFullMessage(String simpleMessage) {
		return fieldNamePrefix + " " + super.getFullMessage(simpleMessage);
	}
	
	@Override
	public Path getValidatedField(String pathString) throws ValidationException {
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
			throw createException(Severity.ERROR, ValidationMessages.Path_EmptyPath());
		}
		
		Path path = PathUtil.createPathOrNull(pathString);
		if(path == null) {
			throw createException(Severity.ERROR, ValidationMessages.Path_InvalidPath(pathString));
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
			location = Location.create(path);
		} catch (CommonException ce) {
			throw error_NotAbsolute(path);
		}
		
		return validateLocation(location);
	}
	
	protected ValidationException error_NotAbsolute(Path path) throws ValidationException {
		return createException(Severity.ERROR, ValidationMessages.Location_NotAbsolute(path));
	}
	
	protected Location validateLocation(Location location) throws ValidationException {
		validateLocation(location, getLocationKind());
		return getValidatedField_rest(location);
	}
	
	protected void validateLocation(Location location, LocationKind locKind) throws ValidationException {
		if(!location.toFile().exists()) {
			throw createException(Severity.WARNING, ValidationMessages.Location_DoesntExist(location));
		}
		
		validateType(location, locKind);
	}
	
	public void validateType(Location location, LocationKind locKind) throws ValidationException {
		if(locKind == LocationKind.FILE_ONLY && !location.toFile().isFile()) {
			throw createException(Severity.WARNING, ValidationMessages.Location_NotAFile(location));
		}
		if(locKind == LocationKind.DIR_ONLY && !location.toFile().isDirectory()) {
			throw createException(Severity.WARNING, ValidationMessages.Location_NotADirectory(location));
		}
	}
	
	protected Location getValidatedField_rest(Location location) throws ValidationException {
		return location;
	}
	
}