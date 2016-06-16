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
import static melnorme.utilbox.core.CoreUtil.option;
import static melnorme.utilbox.misc.StringUtil.nullAsEmpty;

import java.util.Optional;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import com.googlecode.goclipse.tooling.GoPackageName;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.tooling.env.GoEnvironmentConstants;
import com.googlecode.goclipse.tooling.env.GoPath;
import com.googlecode.goclipse.tooling.env.GoRoot;
import com.googlecode.goclipse.tooling.env.GoWorkspaceLocation;
import com.googlecode.goclipse.tooling.oracle.GoOperationContext;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.core.utils.prefs.IProjectPreference;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.common.ISourceBuffer;
import melnorme.lang.tooling.toolchain.ops.IToolOperationService;
import melnorme.lang.tooling.toolchain.ops.SourceOpContext;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public class GoProjectEnvironment implements GoEnvironmentConstants {
	
	public static GoOperationContext getGoOperationContext(ISourceBuffer sourceBuffer, int offset) {
		SourceOpContext opContext = sourceBuffer.getSourceOpContext(new SourceRange(offset, 0));
		
		IProject project = ResourceUtils.getProject(opContext.getOptionalFileLocation());
		GoEnvironment goEnv = GoProjectEnvironment.getGoEnvironment(project);
		
		IToolOperationService toolsOpService = LangCore.getToolManager().getEngineToolsOperationService();
		return new GoOperationContext(sourceBuffer, opContext, toolsOpService, goEnv);
	}
	
	public static GoRoot getEffectiveGoRoot(IProject project) {
		String prefValue = LangCore.settings().SDK_LOCATION.getRawValue(project);
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
		
		GoWorkspaceLocation goPathEntry = rawGoPath.findGoPathEntryForSourceLocation(projectLoc);
		if(goPathEntry != null) {
			// GOPATH already contains project location
			return rawGoPath;
		}
		
		if(GoEnvironmentPrefs.APPEND_PROJECT_LOC_TO_GOPATH.getEffectiveValue(option(project))) {
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
		return goPath.findGoPathEntryForSourceLocation(ResourceUtils.getProjectLocation2(project)) != null;
	}
	
	protected static String getEffectiveValueFromEnv(IProjectPreference<String> pref, IProject project, 
			String envAlternative) {
		String prefValue = pref.getEffectiveValue(Optional.ofNullable(project));
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
	
	public static ArrayList2<GoPackageName> findSourcePackages(IProject project, GoEnvironment goEnvironment)
			throws CoreException {
		return goEnvironment.getGoPath().findGoSourcePackages(ResourceUtils.getProjectLocation(project));
	}
	
	public static Location getBinFolderLocation(IProject project) throws CommonException {
		GoEnvironment goEnv = getGoEnvironment(project);
		return goEnv.getBinFolderLocationForSubLocation(loc(project.getLocation()));
	}
	
}