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
package melnorme.lang.utils;

import java.nio.file.Path;

import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.MiscUtil;

/**
 * Fairly basic for now, more methods can be added.
 */
public class ParseHelper {
	
	protected int parsePositiveInt(String str) throws CommonException {
		int integer = parseInt(str);
		if(integer < 0) {
			throw new CommonException("Integer is not positive: " + str);
		}
		return integer;
	}
	
	protected int parseInt(String str) throws CommonException {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			throw new CommonException("Invalid integer: " + str);
		}
	}
	
	protected int parseInt(String integerString, String errorMessage) throws CommonException {
		try {
			return Integer.parseInt(integerString);
		} catch (NumberFormatException e) {
			throw new CommonException(errorMessage, null);
		}
	}
	
	/* -----------------  ----------------- */
	
	protected Path parsePath(String pathString) throws CommonException {
		return MiscUtil.createPath2(pathString);
	}
	
	protected Location parseLocation(String pathString) throws CommonException {
		return Location.create2(pathString);
	}
	
}