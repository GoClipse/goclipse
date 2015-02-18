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
package melnorme.lang.ide.core.operations;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.nio.file.Path;
import java.util.Map;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.bundlemodel.SDKPreferences;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.data.LocationValidator;
import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.ops.ToolSourceError;
import melnorme.lang.utils.ProcessUtils;
import melnorme.utilbox.collections.ArrayList2;

import org.eclipse.core.resources.IBuildConfiguration;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public abstract class LangProjectBuilder extends IncrementalProjectBuilder {
	
	public LangProjectBuilder() {
	}
	
	@Override
	protected void startupOnInitialize() {
		assertTrue(getProject() != null);
	}
	
	protected void deleteProjectBuildMarkers() {
		try {
			getProject().deleteMarkers(getBuildProblemId(), true, IResource.DEPTH_INFINITE);
		} catch (CoreException ce) {
			LangCore.logStatus(ce);
		}
	}
	
	protected String getBuildProblemId() {
		return LangCore_Actual.BUILD_PROBLEM_ID;
	}
	
	protected boolean isFirstProjectOfKind() throws CoreException {
		boolean firstOfKind = true;
		for (IBuildConfiguration buildConfig : getContext().getAllReferencedBuildConfigs()) {
			if(buildConfig.getProject().hasNature(LangCore.NATURE_ID)) {
				firstOfKind = false;
			}
		}
		return firstOfKind;
	}
	
	protected boolean isLastProjectOfKind() throws CoreException {
		boolean lastOfKind = true;
		for (IBuildConfiguration buildConfig : getContext().getAllReferencingBuildConfigs()) {
			if(buildConfig.getProject().hasNature(LangCore.NATURE_ID)) {
				lastOfKind = false;
			}
		}
		return lastOfKind;
	}
	
	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
		assertTrue(kind != CLEAN_BUILD);
		
		IProject project = assertNotNull(getProject());
		
		if(isFirstProjectOfKind()) {
			handleFirstOfKind();
		}
		
		deleteProjectBuildMarkers();
		
		try {
			doBuild(project, kind, args, monitor);
		} 
		catch (CoreException ce) {
			if(!monitor.isCanceled()) {
				LangCore.logStatus(ce);
			}
			
			forgetLastBuiltState();
			throw ce; // Note: if monitor is cancelled, exception will be ignored.
		} 
		finally {
			getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
			
			if(isLastProjectOfKind()) {
				handleLastOfKind();
			}
		}
		
		// no project dependencies (yet)
		return null;
	}
	
	protected void handleFirstOfKind() {
	}
	
	protected void handleLastOfKind() {
	}
	
	protected abstract IProject[] doBuild(IProject project, int kind, Map<String, String> args, IProgressMonitor pm)
			throws CoreException;
	
	
	/* ----------------- Build processes ----------------- */
	
	protected AbstractToolsManager<?> getToolManager() {
		return LangCore.getToolManager();
	}
	
	protected ProcessBuilder createSDKProcessBuilder(String... sdkOptions) throws CoreException {
		ArrayList2<String> commandLine = new ArrayList2<>();
		commandLine.add(getSDKToolPath());
		commandLine.addElements(sdkOptions);
		return ProcessUtils.createProcessBuilder(commandLine, null);
	}
	
	protected String getSDKToolPath() throws CoreException {
		String pathString = SDKPreferences.SDK_PATH.get();
		try {
			return getSDKLocationValidator().getValidatedField(pathString).toPathString();
		} catch (StatusException se) {
			throw LangCore.createCoreException(se);
		}
	}
	
	protected abstract LocationValidator getSDKLocationValidator();
	
	/* ----------------- Problem markers handling ----------------- */
	
	protected void addErrorMarkers(ArrayList2<ToolSourceError> buildErrors, Path rootPath) throws CoreException {
		assertTrue(rootPath.isAbsolute());
		
		for (ToolSourceError buildError : buildErrors) {
			Path path = buildError.getFilePath();
			path = rootPath.resolve(path); // Absolute paths will remain unchanged.
			path = path.normalize();
			
			IFile[] files = ResourceUtils.getWorkspaceRoot().findFilesForLocationURI(path.toUri());
			for (IFile file : files) {
				if(!file.exists())
					continue;
				
				// TODO: check if marker already exists?
				addErrorMarker(file, buildError);
			}
		}
		
	}
	
	protected void addErrorMarker(IResource resource, ToolSourceError buildError) throws CoreException {
		IMarker dubMarker = resource.createMarker(getBuildProblemId());
		
		dubMarker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		dubMarker.setAttribute(IMarker.MESSAGE, buildError.getErrorMessage());
		
		int line = buildError.getFileLineNumber();
		if(line >= 0) {
			dubMarker.setAttribute(IMarker.LINE_NUMBER, line);
		}
		int column = buildError.getFileColumnNumber();
		if(column >= 0) {
			// TODO: map column to position?
			//dubMarker.setAttribute(IMarker.LINE_NUMBER, column);
		}
	}
	
}