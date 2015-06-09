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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import melnorme.utilbox.core.CommonException;


public class PathUtil {
	
	public static final Path DEFAULT_ROOT_PATH = createValidPath("").toAbsolutePath().getRoot();
	public static final Location DEFAULT_ROOT_LOC = Location.fromAbsolutePath(DEFAULT_ROOT_PATH);
	
	/** @return a valid path, 
	 * or null if a valid path could not be created from given pathString. */
	public static Path createPathOrNull(String pathString) {
		try {
			return Paths.get(pathString);
		} catch (InvalidPathException ipe) {
			return null;
		}
	}

	/** @return a valid path. Given pathString must represent a valid path. */
	public static Path createValidPath(String pathString) {
		try {
			return Paths.get(pathString);
		} catch (InvalidPathException ipe) {
			throw assertFail();
		}
	}

	
	public static Path createPath(String pathString) throws CommonException {
		return createPath(pathString, "Invalid path: ");
	}
	
	/** @return a valid path from given pathString. 
	 * @throws CommonException if a valid path could not be created. 
	 * Given errorMessagePrefix will be used as a prefix in {@link CommonException}'s message. */
	public static Path createPath(String pathString, String errorMessagePrefix) throws CommonException {
		try {
			return Paths.get(pathString);
		} catch (InvalidPathException ipe) {
			String pathMessage = ipe.getMessage();
			if(pathMessage == null) {
				pathMessage = ipe.toString();
			}
			throw new CommonException(errorMessagePrefix + pathMessage);
		}
	}
	
	public static Path getParentOrEmpty(Path path) throws CommonException {
		Path parent = path.getParent();
		return parent == null ? createValidPath("") : parent;
	}
	
	public static Path toPath(Location location) {
		return location == null ? null : location.toPath();
	}
	
}