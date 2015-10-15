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

import java.text.MessageFormat;

import melnorme.utilbox.misc.Location;

public interface GoCoreMessages {
	
	public static String ERROR_SrcRootContainsGoFiles(Location sourceRootDir) {
		String pattern = 
				"The Go `src` directory at `{0}` contains .go files. " +
				"This is not allowed, these files will be ignored.\n" + 
				"Instead, all .go files should be in a subdirectory of `src`, " + 
				"so that they will be part of a Go package. " + 
				"This is so they can be built using the `./...` pattern, or imported by other Go files.";
		return MessageFormat.format(pattern, sourceRootDir);
	}
	
	public static String ERROR_ProjectDoesNotHaveSrcFolder(Location location) {
		return MessageFormat.format("Error, using location `{0}` as a Go workspace, "
				+ "but location does not contain a `src` directory. ", location);
	}
	
}
