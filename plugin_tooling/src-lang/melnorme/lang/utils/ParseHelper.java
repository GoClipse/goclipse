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
import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.misc.MiscUtil.InvalidPathExceptionX;

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
			int integer = Integer.parseInt(str);
			return integer;
		} catch (NumberFormatException e) {
			throw new CommonException("Invalid integer: " + str);
		}
	}
	
	protected Path parsePath(String string) throws CommonException {
		try {
			return MiscUtil.createPath(string);
		} catch (InvalidPathExceptionX e) {
			throw new CommonException("Invalid path. ", e);
		}
	}
	
}