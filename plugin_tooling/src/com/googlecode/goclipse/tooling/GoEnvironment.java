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

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import melnorme.lang.tooling.ProcessUtils;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.misc.MiscUtil.InvalidPathExceptionX;

/**
 * Immutable description of a Go environment, under which Go operations and semantic analysis can be run.
 * (similar to a build path)
 */
public class GoEnvironment {
	
	protected final String goRoot;
	protected final GoArch goArch;
	protected final GoOs goOs;
	protected final GoPath goPath;
	
	public GoEnvironment(String goRoot, GoArch goArch, GoOs goOs, GoPath goPath) {
		this.goRoot = assertNotNull(goRoot);
		this.goArch = goArch;
		this.goOs = goOs;
		this.goPath = assertNotNull(goPath);
	}
	
	public GoEnvironment(String goRoot, GoArch goArch, GoOs goOs, String goPath) {
		this(goRoot, goArch, goOs, new GoPath(goPath));
	}
	
	public String getGoRoot() {
		return goRoot;
	}
	
	public Path getGoRoot_Path() throws StatusException {
		return createPath(goRoot);
	}
	
	public GoArch getGoArch() {
		return goArch;
	}
	
	public GoOs getGoOs() {
		return goOs;
	}
	
	public GoPath getGoPath() {
		return goPath;
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
		putMapEntry(env, GoEnvironmentConstants.GOPATH, getGoPathString());
		
		if(goArch != null) {
			putMapEntry(env, GoEnvironmentConstants.GOARCH, goArch.asString());
		}
		if(goOs != null) {
			putMapEntry(env, GoEnvironmentConstants.GOOS, goOs.asString());
		}
		
		return pb;
	}
	
	protected void putMapEntry(Map<String, String> env, String key, String value) {
		if(value != null) {
			env.put(key, value);
		}
	}
	
	/* ----------------- helpers ----------------- */
	
	protected static Path createPath(String pathString) throws StatusException {
		try {
			return MiscUtil.createPath(pathString);
		} catch (InvalidPathExceptionX e) {
			throw new StatusException("Invalid path: " + e.getCause().getMessage(), null);
		}
	}
	
	/* -----------------  ----------------- */
	// The following methods, I'm not sure they are really necessary.
	// with some refactoring, we might be able to remove their uses
	
	protected String getGoOS_GoArch_segment() {
		return getGoOs() + "_" + getGoArch();
	}
	
	public Path getGoRootPackageObjectsLocation() throws StatusException {
		return getPackageObjectsFolder(getGoRoot_Path());
	}
	
	public Path getPackageObjectsFolder(Path rootPath) throws StatusException {
		return rootPath.resolve("pkg").resolve(getGoOS_GoArch_segment());
	}
	
	public String getPkgFolderLocations() throws StatusException {
		
		ArrayList2<String> pkgFolders = new ArrayList2<>();
		
		pkgFolders.add(getGoRootPackageObjectsLocation().toString());
		
		for (String goPathElement : getGoPathElements()) {
			Path pkgFolder = getPackageObjectsFolder(createPath(goPathElement));
			pkgFolders.add(pkgFolder.toString());
		}
		
		return StringUtil.collToString(pkgFolders, File.pathSeparator);
	}
	
}