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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import melnorme.lang.tooling.ProcessUtils;

/**
 * Immutable description of a Go environment, under which Go operations and semantic analysis can be run.
 * (similar to a build path)
 */
public class GoEnvironment {
	
	protected final String goRoot;
	protected final String goArch;
	protected final String goOs;
	protected final GoPath goPath;
	
	public GoEnvironment(String goRoot, String goArch, String goOs, GoPath goPath) {
		this.goRoot = assertNotNull(goRoot);
		this.goArch = goArch;
		this.goOs = goOs;
		this.goPath = assertNotNull(goPath);
	}
	
	public GoEnvironment(String goRoot, String goArch, String goOs, String goPath) {
		this(goRoot, goOs, goArch, new GoPath(goPath));
	}
	
	public List<String> getGoPathElements() {
		return goPath.getGoPathElements();
	}
	
	public String getGoPathString() {
		return goPath.getGoPathWorkspaceString();
	}
	
	public Path getGoPackageFromGoModule(Path goModulePath) {
		return goPath.getGoPackageFromGoModule(goModulePath);
	}
	
	public ProcessBuilder createProcessBuilder(List<String> commandLine) {
		ProcessBuilder pb = ProcessUtils.createProcessBuilder(commandLine, null);
		
		Map<String, String> env = pb.environment();
		
		putMapEntry(env, GoEnvironmentConstants.GOROOT, goRoot);
		putMapEntry(env, GoEnvironmentConstants.GOARCH, goArch);
		putMapEntry(env, GoEnvironmentConstants.GOOS, goOs);
		putMapEntry(env, GoEnvironmentConstants.GOPATH, getGoPathString());
		
		return pb;
	}
	
	protected void putMapEntry(Map<String, String> env, String key, String value) {
		if(value != null) {
			env.put(key, value);
		}
	}
	
}