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

import com.googlecode.goclipse.tooling.GoArch;
import com.googlecode.goclipse.tooling.GoEnvironment;
import com.googlecode.goclipse.tooling.GoEnvironmentConstants;
import com.googlecode.goclipse.tooling.GoOs;
import com.googlecode.goclipse.tooling.GoPath;
import com.googlecode.goclipse.tooling.GoRoot;

public class GoProjectEnvironment implements GoEnvironmentConstants {
	
	public static GoRoot getEffectiveGoRoot(IProject project) {
		return new GoRoot(getEffectiveValue_NonNull(GoEnvironmentPrefConstants.GO_ROOT, project, GOROOT));
	}
	
	public static GoArch getEffectiveGoArch(IProject project) {
		return new GoArch(getEffectiveValue_NonNull(GoEnvironmentPrefConstants.GO_ARCH, project, GOARCH));
	}
	
	public static GoOs getEffectiveGoOs(IProject project) {
		return new GoOs(getEffectiveValue_NonNull(GoEnvironmentPrefConstants.GO_OS, project, GOOS));
	}
	
	public static GoPath getEffectiveGoPath(IProject project) {
		return new GoPath(getEffectiveGoPathString(project));
	}
	
	public static String getEffectiveGoPathString(IProject project) {
		String goPathPref = getEffectiveValue_NonNull(GoEnvironmentPrefConstants.GO_PATH, project, GOPATH);
		if(project == null) {
			return goPathPref;
		}
		
		// Implicitly add project location to GOPATH
		return project.getLocation().toOSString() + File.pathSeparator + goPathPref;
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
	
}