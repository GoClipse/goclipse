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

import melnorme.lang.ide.core.utils.prefs.StringPreference;
import melnorme.utilbox.collections.ArrayList2;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

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
		
		java.nio.file.Path projectPath = project.getLocation().toFile().toPath();
		
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
	
}