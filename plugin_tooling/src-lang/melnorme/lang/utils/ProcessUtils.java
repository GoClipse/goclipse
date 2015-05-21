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

import melnorme.utilbox.collections.ArrayList2;
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
	
	public static String getExecutableSuffix() {
		return MiscUtil.OS_IS_WINDOWS ? ".exe" : "";
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
	
	public static void addDirToPathEnv(Path cmdExePath, ProcessBuilder pb) {
		String pathEnv = pb.environment().get("PATH");
		
		Path cmdDir = cmdExePath.getParent();
		if(cmdDir == null || !cmdDir.isAbsolute()) {
			return;
		}
		
		// Add the command dir to the PATH. This helps with certain tool issues on certain OSes, 
		// like OS X 10.10. See for example: https://github.com/GoClipse/goclipse/issues/91#issuecomment-82555504
		pathEnv = pathEnv + File.pathSeparator + cmdDir.toString();
		pb.environment().put("PATH", pathEnv);
	}
	
}