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

import java.io.File;

import melnorme.lang.ide.core.utils.prefs.StringPreference;

import org.eclipse.core.resources.IProject;

import com.googlecode.goclipse.tooling.GoEnvironment;
import com.googlecode.goclipse.tooling.GoEnvironmentConstants;

public class GoProjectEnvironment {
	
	public static String getEffectiveGoRoot(IProject project) {
		return getPrefValue(GoEnvironmentPrefConstants.GO_ROOT, project, GoEnvironmentConstants.GOROOT);
	}
	
	public static String getEffectiveGoArch(IProject project) {
		return getPrefValue(GoEnvironmentPrefConstants.GO_ARCH, project, GoEnvironmentConstants.GOARCH);
	}
	
	public static String getEffectiveGoOs(IProject project) {
		return getPrefValue(GoEnvironmentPrefConstants.GO_OS, project, GoEnvironmentConstants.GOOS);
	}
	
	public static String getEffectiveGoPath(IProject project) {
		String goPathPref = getPrefValue(GoEnvironmentPrefConstants.GO_PATH, project, GoEnvironmentConstants.GOPATH);
		if(project == null) {
			return goPathPref;
		}
		
		// Implicitly add project location to GOPATH
		return project.getLocation().toOSString() + File.pathSeparator + goPathPref;
	}
	
	protected static String getPrefValue(StringPreference stringPref, IProject project, String envAlternative) {
		String prefValue = stringPref.get(project);
		if(prefValue == null || prefValue.isEmpty()) {
			return System.getenv(envAlternative);
		}
		return prefValue;
	}
	
	/**
	 * @param project - can be null.
	 */
	public static GoEnvironment getGoEnvironment(IProject project) {
		String goRoot = getEffectiveGoRoot(project);
		String goArch = getEffectiveGoArch(project);
		String goOs = getEffectiveGoOs(project);
		String goPath = getEffectiveGoPath(project);
		return new GoEnvironment(goRoot, goArch, goOs, goPath);
	}
	
}