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

import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import com.googlecode.goclipse.tooling.GoPackageName;

import melnorme.lang.utils.ProcessUtils;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

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
	
	public GoArch getGoArch() {
		return goArch;
	}
	public GoOs getGoOs() {
		return goOs;
	}
	
	public GoArch getGoArch_NonNull() throws CommonException {
		if(goArch == null) 
			throw new CommonException("GOARCH is undefined");
		return goArch;
	}
	
	public GoOs getGoOs_NonNull() throws CommonException {
		if(goOs == null) 
			throw new CommonException("GOOS is undefined");
		return goOs;
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
	
	
	/* ----------------- validation: TODO could use cleanup ----------------- */
	
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
	
	/* -----------------  ----------------- */
	
	public GoPackageName findGoPackageForSourceFile(Location goSourceFileLoc) throws CommonException {
		Location goPackageLocation = goSourceFileLoc.getParent();
		return findGoPackageForLocation(goPackageLocation);
	}
	
	public GoPackageName findGoPackageForLocation(Location goPackageLocation) throws CommonException {
		GoPackageName goPackage = goRoot.findGoPackageForLocation(goPackageLocation);
		if(goPackage != null) {
			return goPackage;
		}
		
		return goPath.findGoPackageForLocation(goPackageLocation);
	}
	
	protected static GoPackageName getGoPackageForLocation(Location goPackageLocation, Location sourceRoot) {
		if(goPackageLocation == null || !goPackageLocation.startsWith(sourceRoot)) {
			return null;
		}
		Path goPackageRelPath = sourceRoot.relativize(goPackageLocation);
		if(goPackageRelPath.toString().isEmpty()) {
			return null;
		}
		return GoPackageName.fromPath(goPackageRelPath);
	}
	
	public Location getBinFolderLocationForSubLocation(Location goPathSubLocation) throws CommonException {
		GoWorkspaceLocation goWorkspace = getGoPath().findGoPathEntry(goPathSubLocation);
		
		if(goWorkspace == null) {
			throw new CommonException(
				MessageFormat.format("Could not find path `{0}` in a GOPATH entry: ", goPathSubLocation));
		}
		return goWorkspace.getBinLocation();
	}
	
//	protected String getGoOS_GoArch_segment() throws CommonException {
//		return getGoOs().asString() + "_" + getGoArch().asString();
//	}
//	
//	protected Path getGoOSGoArchSegmentPath() throws CommonException {
//		return PathUtil.createPath(getGoOS_GoArch_segment(), "Invalid GOOS-GOARCH: ");
//	}
//	
//	public Location getGoRootToolsDir() throws CommonException {
//		Path subPath = getGoOSGoArchSegmentPath();
//		return goRoot.asLocation().resolve_fromValid("pkg/tool/").resolve(subPath);
//	}
	
	
	/* -----------------  process builder  ----------------- */
	
	public ProcessBuilder createProcessBuilder(List<String> commandLine, Location workingDir) throws CommonException {
		return createProcessBuilder(commandLine, workingDir, true);
	}
	
	public ProcessBuilder createProcessBuilder(List<String> commandLine, Location workingDir, boolean goRootInPath) 
			throws CommonException {
		ProcessBuilder pb = ProcessUtils.createProcessBuilder(commandLine, workingDir);
		setupProcessEnv(pb, goRootInPath);
		return pb;
	}

	public void setupProcessEnv(ProcessBuilder pb, boolean goRootInPath) throws CommonException {
		Map<String, String> env = pb.environment();
		
		putMapEntry(env, GoEnvironmentConstants.GOROOT, goRoot.asString());
		putMapEntry(env, GoEnvironmentConstants.GOPATH, getGoPathString());
		
		if(goArch != null) {
			putMapEntry(env, GoEnvironmentConstants.GOARCH, goArch.asString());
		}
		if(goOs != null) {
			putMapEntry(env, GoEnvironmentConstants.GOOS, goOs.asString());
		}
		
		if(goRootInPath) {
			// Add GoRoot to path. See #113 for rationale
			ProcessUtils.addDirToPathEnv(getGoRoot_Location().toPath(), pb);
		}
	}
	
	protected void putMapEntry(Map<String, String> env, String key, String value) {
		if(value != null) {
			env.put(key, value);
		}
	}
	
}