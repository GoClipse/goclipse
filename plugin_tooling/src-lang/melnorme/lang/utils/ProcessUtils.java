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
import java.util.List;

import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.MiscUtil;

public class ProcessUtils {
	
	public static ProcessBuilder createProcessBuilder(List<String> commandLine, Location workingDir) {
		return createProcessBuilder(commandLine, workingDir.toFile()); 
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
	
}
