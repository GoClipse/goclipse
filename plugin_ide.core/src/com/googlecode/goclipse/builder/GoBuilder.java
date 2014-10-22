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
package com.googlecode.goclipse.builder;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.LangProjectBuilder;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.core.GoEnvironmentPrefs;
import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.core.GoProjectPrefConstants;
import com.googlecode.goclipse.core.operations.GoToolManager;
import com.googlecode.goclipse.tooling.GoBuildOutputProcessor;
import com.googlecode.goclipse.tooling.GoPackageName;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

public class GoBuilder extends LangProjectBuilder {
	
	public static final String BUILDER_ID = "com.googlecode.goclipse.goBuilder";
	
	public GoBuilder() {
	}
	
	@Override
	protected String getBuildProblemId() {
		return MarkerUtilities.MARKER_ID;
	}
	
	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
		return super.build(kind, args, monitor);
	}
	
	@Override
	protected IProject[] doBuild(final IProject project, int kind, Map<String, String> args, IProgressMonitor monitor)
			throws CoreException {
		
		GoToolManager.getDefault().notifyBuildStarting(project);
		
		try {
			doBuildAll(project, monitor);
		} 
		catch (CoreException ce) {
			if(!monitor.isCanceled()) {
				LangCore.logStatus(ce.getStatus());
			}
			
			forgetLastBuiltState();
			throw ce; // Note: if monitor is cancelled, exception will be ignored.
		} 
		finally {
			getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
			
			GoToolManager.getDefault().notifyBuildTerminated(project);
		}
		
		// no project dependencies (yet)
		return null;
	}
	
	protected void doBuildAll(final IProject project, IProgressMonitor monitor) throws CoreException {
		
		GoEnvironment goEnv = getValidGoEnvironment(project);
		
		ArrayList2<String> goBuildCmdLine = getGoToolCommandLine();
		goBuildCmdLine.addElements("install", "-v");
		goBuildCmdLine.addElements(GoProjectPrefConstants.GO_BUILD_EXTRA_OPTIONS.getParsedArguments(project));
		addSourcePackagesToCmdLine(project, goBuildCmdLine, goEnv);
		
		ExternalProcessResult buildAllResult = 
			GoToolManager.getDefault().runBuildTool(goEnv, project, monitor, getProjectLocation(), goBuildCmdLine);
		
		GoBuildOutputProcessor buildOutput = new GoBuildOutputProcessor(MiscUtil.createValidPath("")) {
			@Override
			protected void handleParseError(CommonException ce) {
				LangCore.logError(ce.getMessage(), ce.getCause());
			}
		};
		buildOutput.parseOutput(buildAllResult);
		
		addErrorMarkers(buildOutput.getBuildErrors());
	}
	
	protected File getProjectLocation() {
		return getProject().getLocation().toFile();
	}
	
	protected GoEnvironment getValidGoEnvironment(final IProject project) throws CoreException {
		GoEnvironment goEnv = GoProjectEnvironment.getGoEnvironment(project);
		if(!goEnv.isValid()) { /* FIXME: one exception per build. */
			throw GoCore.createCoreException("Go Environment settings are not valid", null);
		}
		return goEnv;
	}
	
	protected ArrayList2<String> getGoToolCommandLine() throws CoreException {
		String compilerPath = GoEnvironmentPrefs.COMPILER_PATH.get();
		
		if(compilerPath.isEmpty()) {
			throw LangCore.createCoreException("Compiler Path not defined.", null);
		}
		return new ArrayList2<>(compilerPath);
	}
	
	protected void addSourcePackagesToCmdLine(final IProject project, ArrayList2<String> goBuildCmdLine,
			GoEnvironment goEnvironment) throws CoreException {
		Collection<GoPackageName> sourcePackages = GoProjectEnvironment.getSourcePackages(project, goEnvironment);
		for (GoPackageName goPackageName : sourcePackages) {
			goBuildCmdLine.add(goPackageName.getFullNameAsString());
		}
	}
	
	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		deleteBuildMarkers();
		
		IProject project = getProject();
		GoEnvironment goEnv = getValidGoEnvironment(project);
		
		ArrayList2<String> goBuildCmdLine = getGoToolCommandLine();
		goBuildCmdLine.addElements("clean");
		addSourcePackagesToCmdLine(project, goBuildCmdLine, goEnv);
		
		GoToolManager.getDefault().runBuildTool(goEnv, project, monitor, getProjectLocation(), goBuildCmdLine);
	}
	
}