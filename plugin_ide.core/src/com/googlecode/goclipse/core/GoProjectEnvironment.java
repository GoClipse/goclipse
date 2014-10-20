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
package com.googlecode.goclipse.core;

import java.util.List;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.prefs.StringPreference;
import melnorme.utilbox.collections.ArrayList2;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.googlecode.goclipse.tooling.GoPackageName;
import com.googlecode.goclipse.tooling.env.GoArch;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.tooling.env.GoEnvironmentConstants;
import com.googlecode.goclipse.tooling.env.GoOs;
import com.googlecode.goclipse.tooling.env.GoPath;
import com.googlecode.goclipse.tooling.env.GoRoot;

public class GoProjectEnvironment implements GoEnvironmentConstants {
	
	public static GoRoot getEffectiveGoRoot(IProject project) {
		return new GoRoot(getEffectiveValue_NonNull(GoEnvironmentPrefs.GO_ROOT, project, GOROOT));
	}
	
	public static GoArch getEffectiveGoArch(IProject project) {
		return new GoArch(getEffectiveValue_NonNull(GoEnvironmentPrefs.GO_ARCH, project, GOARCH));
	}
	
	public static GoOs getEffectiveGoOs(IProject project) {
		return new GoOs(getEffectiveValue_NonNull(GoEnvironmentPrefs.GO_OS, project, GOOS));
	}
	
	public static GoPath getEffectiveGoPath(IProject project) {
		String goPathPref = getEffectiveValue_NonNull(GoEnvironmentPrefs.GO_PATH, project, GOPATH);
		GoPath rawGoPath = new GoPath(goPathPref);
		if(project == null) {
			return rawGoPath;
		}
		
		IPath location = project.getLocation();
		if(location == null) {
			return rawGoPath;
		}
		java.nio.file.Path projectPath = location.toFile().toPath();
		
		java.nio.file.Path goPathEntry = rawGoPath.findGoPathEntryForSourceModule(projectPath);
		if(goPathEntry != null) {
			// GOPATH already contains project location
			return rawGoPath;
		}
		
		// Implicitly add project location to GOPATH
		ArrayList2<String> newGoPathEntries = new ArrayList2<>(projectPath.toString());
		newGoPathEntries.addElements(rawGoPath.getGoPathEntries());
		
		return new GoPath(newGoPathEntries);
	}
	
	public static boolean isProjectInsideGoPath(IProject project) throws CoreException {
		GoPath goPath = getEffectiveGoPath(project);
		return isProjectInsideGoPath(project, goPath);
	}
	
	public static boolean isProjectInsideGoPath(IProject project, GoPath goPath) throws CoreException {
		return goPath.findGoPathEntryForSourceModule(getProjectPath(project)) != null;
	}
	
	protected static String getEffectiveValue_NonNull(StringPreference stringPref, IProject project, 
			String envAlternative) {
		String prefValue = stringPref.get(project);
		if(prefValue == null || prefValue.isEmpty()) {
			String envValue = System.getenv(envAlternative);
			return envValue == null ? "" : envValue;
		}
		return prefValue;
	}
	
	/**
	 * @return {@link GoEnvironment} for given project.
	 * @param project - can be null.
	 */
	public static GoEnvironment getGoEnvironment(IProject project) {
		GoRoot goRoot = getEffectiveGoRoot(project);
		GoArch goArch = getEffectiveGoArch(project);
		GoOs goOs = getEffectiveGoOs(project);
		GoPath goPath = getEffectiveGoPath(project);
		return new GoEnvironment(goRoot, goArch, goOs, goPath);
	}

	public static boolean isValid(IProject project) {
		return getGoEnvironment(project).isValid();
	}
	
	public static List<GoPackageName> getSourcePackages(IProject project, GoEnvironment goEnvironment)
			throws CoreException {
		GoPath goPath = goEnvironment.getGoPath();
		
		if(isProjectInsideGoPath(project, goPath)) {
			return GoPath.getSourcePackages(getProjectPath(project));
		} else {
			java.nio.file.Path goPathEntry = goPath.findGoPathEntryForSourceModule(getProjectPath(project));
			return GoPath.getSourcePackages(goPathEntry.resolve(GoPath.SRC_DIR));
		}
	}
	
	public static java.nio.file.Path getProjectPath(IProject project) throws CoreException {
		IPath location = project.getLocation();
		if(location == null) {
			throw LangCore.createCoreException("Invalid project path", null);
		}
		return location.toFile().toPath();
	}
	
	/* ----------------- ----------------- */
	
	@SuppressWarnings("unused") 
	public static boolean isSourceModule(IProject prj, IPath programRelativePath) {
		return Path.fromOSString("src").isPrefixOf(programRelativePath);
	}
	
	public static IPath getBinFolder(IProject project) {
		return project.getLocation().append("bin");
	}
	
	public static IPath getPkgFolder(IProject project) {
		return project.getLocation().append("pkg");
	}
	
	public static IPath getBinFolder(java.nio.file.Path goWorkspaceEntry) {
		return Path.fromOSString(goWorkspaceEntry.resolve("bin").toString());
	}
	
	public static IContainer getSourceFolderRoot(IProject project) throws CoreException {
		
		if(isProjectInsideGoPath(project)) {
			if(project.getLocation() == null) {
				throw GoCore.createCoreException("Invalid project location: " + project.getLocationURI(), null);
			}
			return project;
		}
		
		return project.getFolder("src");
	}
	
}