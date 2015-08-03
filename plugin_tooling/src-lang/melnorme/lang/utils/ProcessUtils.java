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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import melnorme.lang.tooling.ToolingMessages;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.MiscUtil;

public class ProcessUtils {
	
	public static ProcessBuilder createProcessBuilder(List<String> commandLine, Location workingDir) {
		File file = workingDir == null ? null : workingDir.toFile();
		return createProcessBuilder(commandLine, file); 
	}
	
	public static ProcessBuilder createProcessBuilder(List<String> commandLine, File workingDir) {
		assertTrue(commandLine.size() > 0);
		
		ProcessBuilder pb = new ProcessBuilder(commandLine);
		if(workingDir != null) {
			pb.directory(workingDir);
		}
		return pb;
	}
	
	public static ProcessBuilder createProcessBuilder(Path cmdExePath, Location workingDir, 
			boolean addCmdDirToPath, String... arguments) {
		
		ArrayList2<String> commandLine = new ArrayList2<>();
		commandLine.add(cmdExePath.toString());
		commandLine.addElements(arguments);
		ProcessBuilder pb = createProcessBuilder(commandLine, workingDir);
		
		if(addCmdDirToPath) {
			addDirToPathEnv(cmdExePath, pb);
		}
		
		return pb;
	}
	
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
		
		pathEnv = (pathEnv == null) ? "" : pathEnv + File.pathSeparator;
		pathEnv += cmdDir.toString();
		
		putVarInEnvMap(environment, "PATH", pathEnv);
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
	
	public static void validateNonZeroExitValue(int exitValue) throws CommonException {
		if(exitValue != 0) {
			throw new CommonException(ToolingMessages.PROCESS_CompletedWithNonZeroVAlue("Process", exitValue));
		}
	}
	
}