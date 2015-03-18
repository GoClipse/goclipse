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

import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.PathUtil;

import com.googlecode.goclipse.tooling.GoPackageName;

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
	
	public Location getGoRoot_Location() throws CommonException {
		return goRoot.asLocation();
	}
	
	public GoArch getGoArch() throws CommonException{
		validateGoArch();
		return goArch;
	}
	public void validateGoArch() throws CommonException {
		if(goArch == null || goArch.asString().isEmpty()) 
			throw new CommonException("GOARCH is undefined");
	}
	
	public GoOs getGoOs() throws CommonException {
		validateGoOs();
		return goOs;
	}
	public void validateGoOs() throws CommonException {
		if(goOs == null || goOs.asString().isEmpty()) 
			throw new CommonException("GOOS is undefined");
	}
	
	public GoPath getGoPath() {
		return goPath;
	}
	
	public List<String> getGoPathEntries() {
		return goPath.getGoPathEntries();
	}
	
	public String getGoPathString() {
		return goPath.getGoPathWorkspaceString();
	}
	
	public GoPackageName findGoPackageForSourceModule(Location goModuleLoc) throws CommonException {
		GoPackageName goPackage = goRoot.findGoPackageForSourceModule(goModuleLoc);
		if(goPackage != null) {
			return goPackage;
		}
		
		return goPath.findGoPackageForSourceFile(goModuleLoc);
	}
	
	public ProcessBuilder createProcessBuilder(List<String> commandLine, Path workingDir) {
		return createProcessBuilder(commandLine, workingDir.toFile());
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
	
	/* -----------------  ----------------- */
	
	protected String getGoOS_GoArch_segment() throws CommonException {
		return getGoOs().asString() + "_" + getGoArch().asString();
	}
	
	public Location getGoRootToolsDir() throws CommonException {
		Path subPath = PathUtil.createPath(getGoOS_GoArch_segment(), "Invalid GOOS-GOARCH: ");
		return goRoot.asLocation().resolve_fromValid("pkg/tool/").resolve(subPath);
	}
	
	protected static GoPackageName getGoPackageForSourceFile(Location sourceFileLoc, Location sourceRoot) {
		if(!sourceFileLoc.startsWith(sourceRoot)) {
			return null;
		}
		Path sourceFilePath = sourceRoot.relativize(sourceFileLoc);
		return GoPackageName.fromPath(sourceFilePath.getParent()); // Discard file name
	}
	
	public boolean isValid() {
		if (isNullOrEmpty(goRoot.asString())) {
			return false;
		}
		return true;
	}
	
	public void validate() throws CommonException {
		if(!isValid()) {
			// TODO: more specific validation messages.
			throw new CommonException("Go Environment settings are not valid");
		}
	}
	
	public static boolean isNullOrEmpty(String string) {
		return string == null || string.isEmpty();
	}
	
}