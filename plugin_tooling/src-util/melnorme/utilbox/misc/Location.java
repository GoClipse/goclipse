/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.misc;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import java.io.File;
import java.net.URI;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.PathUtil.InvalidPathExceptionX;

/**
 * A location is a normalized, absolute path, based upon {@link Path}.
 */
public class Location {
	
	public static Location create_fromValid(Path path) {
		return fromAbsolutePath(path);
	}
	
	/** @return a new {@link Location} from given absolutePath, which must be an absolute path. */
	public static Location fromAbsolutePath(Path absolutePath) {
		return new Location(absolutePath);
	}
	
	/** @return a new {@link Location} from given path. 
	 * @throws InvalidPathExceptionX if path is not absolute. */
	@Deprecated
	public static Location create(Path path) throws PathUtil.InvalidPathExceptionX {
		if(!path.isAbsolute()) {
			throw new PathUtil.InvalidPathExceptionX(new InvalidPathException(path.toString(), 
					"Invalid location: path is not absolute"));
		}
		return new Location(path);
	}
	
	public static Location create2(Path path) throws CommonException {
		if(!path.isAbsolute()) {
			throw new CommonException("Invalid location: path is not absolute");
		}
		return new Location(path);
	}
	
	/** @return a new {@link Location} from given path string. 
	 * @throws InvalidPathExceptionX if path is not absolute. */
	@Deprecated
	public static Location create(String pathString) throws PathUtil.InvalidPathExceptionX {
		return create(PathUtil.createPath(pathString));
	}
	
	public static Location create2(String pathString) throws CommonException {
		return create2(PathUtil.createPath2(pathString));
	}
	
	/** @return a new {@link Location} from given path, or null if path is not absolute.  */
	public static Location createValidOrNull(Path path) {
		if(path == null) {
			return null;
		}
		
		try {
			return create2(path);
		} catch(CommonException e) {
			return null;
		}
	}
	
	/* -----------------  ----------------- */
	
	public final Path path;
	
	protected Location(Path absolutePath) {
		assertTrue(absolutePath != null && absolutePath.isAbsolute());
		this.path = absolutePath.normalize();
	}
	
	public Path getPath() {
		return path;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof Location)) return false;
		
		Location other = (Location) obj;
		
		return areEqual(path, other.path);
	}
	
	@Override
	public int hashCode() {
		return path.hashCode();
	}
	
	@Override
	public String toString() {
		return toPathString();
	}
	
	public String toPathString() {
		return path.toString();
	}
	
	public Path toPath() {
		return path;
	}
	
	public File toFile() {
		return path.toFile();
	}
	
	public URI toUri() {
		return path.toUri();
	}
	
	/**
	 * @return an new Location resolved from this Location against the given otherPathString. 
	 * null if the other path is not valid. 
	 */
	public Location resolveOrNull(String otherPathString) {
		Path otherPath = PathUtil.createPathOrNull(otherPathString);
		if(otherPath == null) {
			return null;
		}
		return resolve(otherPath);
	}
	
	/**
	 * @return an new Location resolved from this Location against the given otherPathString. Non-null.
	 * The other path *must* be valid. 
	 * (as such this method is usually used when otherPathString is known at compile-time.) 
	 */
	public Location resolve_fromValid(String otherPathString) {
		Path otherPath = PathUtil.createValidPath(otherPathString);
		return resolve(otherPath);
	}
	
	public Location resolve(Path otherPath) {
		// resolving should always result in a valid path: absolute and non-null
		return Location.create_fromValid(path.resolve(otherPath));
	}
	
	public Location getParent() {
		Path parent = path.getParent();
		if(parent == null) {
			return null;
		}
		return Location.create_fromValid(parent);
	}
	
	public static Location validateLocation(Path filePath, boolean isRequired, String descText) 
			throws CommonException {
		if(filePath == null) {
			if(isRequired) {
				throw new CommonException("Path not specified for " + descText + "."); 
			}
			return null;
		}
		try {
			return create2(filePath);
		} catch (CommonException e) {
			throw new CommonException("Invalid location for " + descText + ", path not absolute: " + filePath);
		}
	}
	
}