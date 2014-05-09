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
package melnorme.lang.ide.launching;

import java.util.Map;

import org.eclipse.core.runtime.IPath;

public class ProcessSpawnInfo {
	
	public IPath programPath;
	public String[] programArguments;
	public IPath workingDir;
	public Map<String, String> environment;
	public boolean appendEnv;
	
	public ProcessSpawnInfo(IPath programPath, String[] programArguments, IPath workingDir, 
			Map<String, String> environment, boolean appendEnv) {
		this.programPath = programPath;
		this.programArguments = programArguments;
		this.workingDir = workingDir;
		this.environment = environment;
		this.appendEnv = appendEnv;
	}
	
}