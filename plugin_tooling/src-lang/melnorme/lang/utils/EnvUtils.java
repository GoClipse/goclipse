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
package melnorme.lang.utils;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.misc.StringUtil;

public class EnvUtils {
	
	/** 
	 * Add given cmdExePath to the PATH. This helps with certain tool issues on certain OSes, 
	 * like OS X 10.10. See for example: https://github.com/GoClipse/goclipse/issues/91#issuecomment-82555504
	 */
	public static void addDirToPathEnv(Path cmdExePath, ProcessBuilder pb) {
		Map<String, String> environment = pb.environment();
		
		String pathEnv = getVarFromEnvMap(environment, "PATH");
		
		Path cmdDir = cmdExePath.getParent();
		if(cmdDir == null || !cmdDir.isAbsolute()) {
			return;
		}
		
		String newPathEnv = cmdDir.toString() + File.pathSeparator + StringUtil.nullAsEmpty(pathEnv);
		
		putVarInEnvMap(environment, "PATH", newPathEnv);
	}
	
	public static String getVarFromEnvMap(Map<String, String> envMap, String key) {
		key = getCorrectEnvKey(envMap, key);
		return envMap.get(key);
	}
	
	public static void putVarInEnvMap(Map<String, String> envMap, String key, String value) {
		key = getCorrectEnvKey(envMap, key);
		envMap.put(key, value);
	}
	
	public static String getCorrectEnvKey(Map<String, String> envMap, String key) {
		boolean containsKey = envMap.containsKey(key);
		
		if(!containsKey && MiscUtil.OS_IS_WINDOWS) {
			// Search for var under a different key, because in Windows its case-insensitive
			for(String otherKey : envMap.keySet()) {
				if(otherKey.equalsIgnoreCase(key)) {
					return otherKey;
				}
			}
		}
		return key;
	}
	
	/* -----------------  ----------------- */
	
	public static Location getLocationFromEnvVar(String envVar) throws CommonException {
		String home = getVarFromEnvMap(System.getenv(), envVar);
		if(home == null) {
			throw new CommonException(envVar + " environment variable not set.");
		}
		try {
			return Location.create(home);
		} catch(CommonException ce) {
			throw new CommonException("Error with " + envVar + " environment variable: " + ce.getMessage(), ce);
		}
	}
	
}
