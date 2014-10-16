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
package com.googlecode.goclipse.tooling.env;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.misc.MiscUtil.InvalidPathExceptionX;
import melnorme.utilbox.misc.StringUtil;

/**
 * Immutable description of a Go environment, under which Go operations and semantic analysis can be run.
 * (similar to a build path)
 */
public class GoEnvironment {
	
	public static final String ENV_BIN_FOLDER = "bin";
	public static final String ENV_PKG_FOLDER = "pkg";
	
	protected final GoRoot goRoot;
	protected final GoArch goArch;
	protected final GoOs goOs;
	protected final GoPath goPath;
	
	public GoEnvironment(GoRoot goRoot, GoArch goArch, GoOs goOs, GoPath goPath) {
		this.goRoot = assertNotNull(goRoot);
		this.goArch = goArch;
		this.goOs = goOs;
		this.goPath = assertNotNull(goPath);
	}
	
	public GoEnvironment(GoRoot goRoot, GoArch goArch, GoOs goOs, String goPath) {
		this(goRoot, goArch, goOs, new GoPath(goPath));
	}
	
	public GoRoot getGoRoot() {
		return goRoot;
	}
	
	public Path getGoRoot_Path() throws CommonException {
		return goRoot.asPath();
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
	
	// FIXME: change return type to a specific module class
	public Path getGoPackageFromSourceModule(Path goModulePath) throws CommonException {
		Path goPackage = goRoot.getGoPackageFromSourceModule(goModulePath);
		if(goPackage != null) {
			return goPackage;
		}
		
		return goPath.getGoPackageFromSourceModule(goModulePath);
	}
	
	public ProcessBuilder createProcessBuilder(List<String> commandLine, File workingDir) {
		ProcessBuilder pb = createProcessBuilder(commandLine);
		if(workingDir != null) {
			pb.directory(workingDir);
		}
		return pb;
	}
	
	public ProcessBuilder createProcessBuilder(List<String> commandLine) {
		ProcessBuilder pb = melnorme.lang.utils.ProcessUtils.createProcessBuilder(commandLine, null);
		
		Map<String, String> env = pb.environment();
		
		putMapEntry(env, GoEnvironmentConstants.GOROOT, goRoot.asString());
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
	
	protected static Path createPath(String pathString) throws CommonException {
		try {
			return MiscUtil.createPath(pathString);
		} catch (InvalidPathExceptionX e) {
			throw new CommonException("Invalid path: " + e.getCause().getMessage(), null);
		}
	}
	
	/* -----------------  ----------------- */
	
	public String getGoOS_GoArch_segment() {
		return getGoOs().asString() + "_" + getGoArch().asString();
	}
	
	public Path getGoRootPackageObjectsDir() throws CommonException {
		return getPackageObjectsDir(getGoRoot_Path());
	}
	
	public Path getPackageObjectsDir(Path basePath) throws CommonException {
		return basePath.resolve(getPackageObjectsRelativePath());
	}
	
	public Path getPackageObjectsRelativePath() throws CommonException {
		return MiscUtil.createValidPath("pkg").resolve(createPath(getGoOS_GoArch_segment()));
	}
	
	public Path getGoRootToolsDir() throws CommonException {
		return goRoot.asPath().resolve("pkg/tool/").resolve(createPath(getGoOS_GoArch_segment()));
	}
	
	
	// The following methods, I'm not sure they are really necessary.
	// with some refactoring, we might be able to remove their uses
	
	public String getPackageObjectsPathString() throws CommonException {
		
		ArrayList2<String> pkgFolders = new ArrayList2<>();
		
		pkgFolders.add(getGoRootPackageObjectsDir().toString());
		
		for (String goPathElement : getGoPathElements()) {
			Path pkgFolder = getPackageObjectsDir(createPath(goPathElement));
			pkgFolders.add(pkgFolder.toString());
		}
		
		return StringUtil.collToString(pkgFolders, File.pathSeparator);
	}
	
	public boolean isValid() {
		if (isNullOrEmpty(goRoot.asString()) || isNullOrEmpty(goOs.asString()) || isNullOrEmpty(goArch.asString())) {
			return false;
		}
		return true;
	}
	
	public static boolean isNullOrEmpty(String string) {
		return string == null || string.isEmpty();
	}
	
}