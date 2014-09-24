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
package com.googlecode.goclipse.tooling;

import java.nio.file.Path;

import melnorme.utilbox.misc.MiscUtil;


public class JsonDeserializeHelper {
	
	public JsonDeserializeHelper() {
	}
	
	/* -----------------  ----------------- */
	
	protected Path parsePath(String pathStr) throws StatusException {
		Path path = MiscUtil.createPathOrNull(pathStr);
		if(path == null) {
			throw new StatusException("Invalid path: " + pathStr, null);
		}
		return path;
	}
	
	protected int parseInt(String integerString, String errorMessage) throws StatusException {
		
		try {
			return Integer.parseInt(integerString);
		} catch (NumberFormatException e) {
			throw new StatusException(errorMessage, null);
		}
	}
	
}