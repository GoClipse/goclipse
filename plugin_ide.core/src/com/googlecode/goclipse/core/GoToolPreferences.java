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

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import melnorme.lang.ide.core.utils.prefs.StringPreference;

public interface GoToolPreferences {
	public static class GoToolUtils {
		/**
		 * Searches for an executable absolute path in the directories named
		 * by the PATH environment variable.
		 * 
		 * @param name the executable name
		 * @return the executable absolute path. If no path found, an empty string
		 *         will be returned.
		 */
		public static String lookupPath(String name) {
			String pathStr = System.getenv("PATH");
			if (pathStr == null) {
				return "";
			}
			String[] dirs = pathStr.split(File.pathSeparator);
			for (String dir : dirs) {
				Path path = Paths.get(dir, name);
				if (Files.exists(path)) {
					return path.toAbsolutePath().toString();
				}
			}
			return "";
		}
	}
	
	StringPreference GO_CODE_Path = new StringPreference("GoToolPreferences.GO_CODE_Path",
			GoToolUtils.lookupPath("gocode"));
	
	StringPreference GO_ORACLE_Path = new StringPreference("GoToolPreferences.GO_ORACLE_Path",
			GoToolUtils.lookupPath("oracle"));
}