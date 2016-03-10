/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
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

import melnorme.lang.utils.EnvUtils;
import melnorme.lang.utils.ProcessUtils;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

/**
 * Immutable description of a Go environment, under which Go operations and semantic analysis can be run.
 * (similar to a build path)
 */
public class GoEnvironment {
	
	protected final GoRoot goRoot;
	protected final GoPath goPath;
	
	public GoEnvironment(GoRoot goRoot, GoPath goPath) {
		this.goRoot = assertNotNull(goRoot);
		this.goPath = assertNotNull(goPath);
	}
	
	public GoEnvironment(GoRoot goRoot, String goPath) {
		this(goRoot, new GoPath(goPath));
	}
	
	public GoRoot getGoRoot() {
		return goRoot;
	}
	
	public Location getGoRoot_Location() throws CommonException {
		return goRoot.asLocation();
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
	
	
	/* ----------------- validation: ----------------- */
	
	public void validate() throws CommonException {
		goRoot.validate();
		goPath.validate();
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
	
	
	/* -----------------  process builder  ----------------- */
	
	public ProcessBuilder createProcessBuilder(Indexable<String> commandLine, Location workingDir) 
			throws CommonException {
		return createProcessBuilder(commandLine, workingDir, true);
	}
	
	public ProcessBuilder createProcessBuilder(Indexable<String> commandLine, Location workingDir, boolean goRootInPath) 
			throws CommonException {
		ProcessBuilder pb = ProcessUtils.createProcessBuilder(commandLine, workingDir);
		setupProcessEnv(pb, goRootInPath);
		return pb;
	}

	public void setupProcessEnv(ProcessBuilder pb, boolean goRootInPath) throws CommonException {
		Map<String, String> env = pb.environment();
		
		putMapEntry(env, GoEnvironmentConstants.GOROOT, goRoot.asString());
		putMapEntry(env, GoEnvironmentConstants.GOPATH, getGoPathString());
		
		if(goRootInPath) {
			// Add GoRoot to path. See #113 for rationale
			EnvUtils.addDirToPathEnv(getGoRoot_Location().toPath(), pb);
		}
	}
	
	protected void putMapEntry(Map<String, String> env, String key, String value) {
		if(value != null) {
			env.put(key, value);
		}
	}
	
}