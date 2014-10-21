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

import java.util.Map;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.tooling.ops.ToolSourceError;
import melnorme.utilbox.collections.ArrayList2;

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
	
	protected void deleteBuildMarkers() {
		try {
			getProject().deleteMarkers(getBuildProblemId(), true, IResource.DEPTH_INFINITE);
		} catch (CoreException ce) {
			LangCore.logStatus(ce);
		}
	}
	
	protected abstract String getBuildProblemId();
	
	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
		assertTrue(kind != CLEAN_BUILD);
		
		IProject project = assertNotNull(getProject());
		
		deleteBuildMarkers();
		
		try {
			return doBuild(project, kind, args, monitor);
		} finally {
			
		}
	}
	
	protected abstract IProject[] doBuild(IProject project, int kind, Map<String, String> args, IProgressMonitor pm)
		throws CoreException;
	
	
	protected void addErrorMarkers(ArrayList2<ToolSourceError> buildErrors) throws CoreException {
		for (ToolSourceError buildError : buildErrors) {
			String filePath = buildError.getFilePath().toString();
			IResource resource = getProject().findMember(filePath);
			if(resource == null)
				continue;
			
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
	
}