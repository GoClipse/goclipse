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
package com.googlecode.goclipse.core;

import static melnorme.lang.ide.core.utils.ResourceUtils.loc;
import static melnorme.utilbox.misc.StringUtil.nullAsEmpty;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import com.googlecode.goclipse.tooling.GoPackageName;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.tooling.env.GoEnvironmentConstants;
import com.googlecode.goclipse.tooling.env.GoPath;
import com.googlecode.goclipse.tooling.env.GoRoot;
import com.googlecode.goclipse.tooling.env.GoWorkspaceLocation;

import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.core.utils.prefs.IProjectPreference;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public class GoProjectEnvironment implements GoEnvironmentConstants {
	
	public static GoRoot getEffectiveGoRoot(IProject project) {
		String prefValue = GoEnvironmentPrefs.GO_ROOT.getEffectiveValue(project);
		return new GoRoot(nullAsEmpty(prefValue));
	}
	
	public static GoPath getEffectiveGoPath(IProject project) {
		String goPathPref = getEffectiveValueFromEnv(GoEnvironmentPrefs.GO_PATH, project, GOPATH);
		GoPath rawGoPath = new GoPath(goPathPref);
		if(project == null) {
			return rawGoPath;
		}
		
		Location projectLoc = ResourceUtils.getResourceLocation(project);
		if(projectLoc == null) {
			return rawGoPath;
		}
		
		GoWorkspaceLocation goPathEntry = rawGoPath.findGoPathEntryForSourcePath(projectLoc);
		if(goPathEntry != null) {
			// GOPATH already contains project location
			return rawGoPath;
		}
		
		if(GoEnvironmentPrefs.APPEND_PROJECT_LOC_TO_GOPATH.getEffectiveValue(project)) {
			// Add project location to the end of GOPATH
			ArrayList2<String> newGoPathEntries = new ArrayList2<>(rawGoPath.getGoPathEntries());
			newGoPathEntries.add(projectLoc.toPathString());
			return new GoPath(newGoPathEntries);
		}
		return rawGoPath;
	}
	
	public static boolean isProjectInsideGoPathSourceFolder(IProject project) throws CommonException {
		GoPath goPath = getEffectiveGoPath(project);
		return isProjectInsideGoPathSourceFolder(project, goPath);
	}
	
	public static boolean isProjectInsideGoPathSourceFolder(IProject project, GoPath goPath) throws CommonException {
		return goPath.findGoPathEntryForSourcePath(ResourceUtils.getProjectLocation2(project)) != null;
	}
	
	protected static String getEffectiveValueFromEnv(IProjectPreference<String> pref, IProject project, 
			String envAlternative) {
		String prefValue = pref.getEffectiveValue(project);
		if(prefValue == null) {
			return nullAsEmpty(System.getenv(envAlternative));
		}
		return prefValue;
	}
	
	public static GoEnvironment getGoEnvironmentFromLocation(Location fileLocation) {
		IProject project = ResourceUtils.getProject(fileLocation);
		return getGoEnvironment(project);
	}
	
	/**
	 * @return {@link GoEnvironment} for given project.
	 * @param project - can be null.
	 */
	public static GoEnvironment getGoEnvironment(IProject project) {
		GoRoot goRoot = getEffectiveGoRoot(project);
		GoPath goPath = getEffectiveGoPath(project);
		return new GoEnvironment(goRoot, goPath);
	}

	public static GoEnvironment getValidatedGoEnvironment(final IProject project) throws CommonException {
		GoEnvironment goEnv = getGoEnvironment(project);
		goEnv.validate();
		return goEnv;
	}
	
	public static Location getAssociatedSourceFolder(GoPath goPath, Location projectLoc) throws CommonException {
		
		if(goPath.findExactGoPathEntry(projectLoc) != null) {
			return projectLoc.resolve_valid("src");
		}
		
		if(goPath.findGoPathEntryForSourcePath(projectLoc) != null) {
			return projectLoc;
		}
		
		throw new CommonException(GoCoreMessages.ERROR_GOPATH_DoesNotContainProject());
	}
	
	public static ArrayList2<GoPackageName> findSourcePackages(IProject project, GoEnvironment goEnvironment)
			throws CoreException {
		return goEnvironment.getGoPath().findGoSourcePackages(ResourceUtils.getProjectLocation(project));
	}
	
	public static Location getBinFolderLocation(IProject project) throws CommonException {
		GoEnvironment goEnv = getGoEnvironment(project);
		return goEnv.getBinFolderLocationForSubLocation(loc(project.getLocation()));
	}
	
}