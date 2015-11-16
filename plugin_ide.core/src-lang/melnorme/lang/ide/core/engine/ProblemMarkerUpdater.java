/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.engine;

import java.util.concurrent.ExecutorService;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.engine.StructureModelManager.StructureInfo;
import melnorme.lang.ide.core.utils.CoreExecutors;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.ast.ParserError;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.ownership.IDisposable;

public class ProblemMarkerUpdater implements IDisposable {
	
	protected final ExecutorService problemsExecutor = CoreExecutors.newExecutorTaskAgent(getClass());
	protected StructureModelManager structureManager;
	
	public ProblemMarkerUpdater() {
	}
	
	public void install(StructureModelManager structureManager) {
		this.structureManager = structureManager;
		structureManager.addListener(problemUpdaterListener);
	}
	
	@Override
	public void dispose() {
		structureManager.removeListener(problemUpdaterListener);
		problemsExecutor.shutdownNow();
	}
	
	protected final IStructureModelListener problemUpdaterListener = new IStructureModelListener() {
		@Override
		public void structureChanged(StructureInfo structureInfo, SourceFileStructure sourceFileStructure) {
			Object key = structureInfo.key;
			
			if(key instanceof Location) {
				Location location = (Location) key;
				queueUpdateProblemMarkers(location, structureInfo.getStoredStructure());
			}
		}
	};
	
	public void queueUpdateProblemMarkers(final Location location, 
			final SourceFileStructure sourceFileStructure /** Can be null. */) {
		problemsExecutor.submit(new Runnable() {
			@Override
			public void run() {
				updateProblemMarkers(location, sourceFileStructure);
			}
		});
	}
	
	public void updateProblemMarkers(final Location location, final SourceFileStructure sourceFileStructure) {
		IFile[] files = ResourceUtils.getWorkspaceRoot().findFilesForLocationURI(location.toUri());
		if(files.length == 0) {
			return;
		}
		final IFile file = files[0];
		
		NullProgressMonitor monitor = new NullProgressMonitor();
		
		try {
			ResourceUtils.getWorkspace().run(new IWorkspaceRunnable() {
				@Override
				public void run(IProgressMonitor monitor) throws CoreException {
					doCreateMarkers(location, file, sourceFileStructure);
				}
			}, file, IWorkspace.AVOID_UPDATE, monitor);
		} catch(CoreException ce) {
			LangCore.logStatus(ce);
		}
	}
	
	protected void doCreateMarkers(final Location location, IFile file, final SourceFileStructure sourceFileStructure)
			throws CoreException {
		if(!file.exists()) {
			return; // It could have been removed in the meanwhile.
		}
		
		file.deleteMarkers(LangCore_Actual.SOURCE_PROBLEM_ID, true, IResource.DEPTH_ZERO);
		
		if(sourceFileStructure == null) {
			return;
		}
		
		for (ParserError problem : sourceFileStructure.getParserProblems()) {
			createMarker(location, file, problem);
		}
	}
	
	protected void createMarker(final Location location, IFile file, ParserError problem) throws CoreException {
		IMarker marker = file.createMarker(LangCore_Actual.SOURCE_PROBLEM_ID);
		marker.setAttribute(IMarker.LOCATION, location.toPathString());
		marker.setAttribute(IMarker.MESSAGE, problem.getUserMessage());
		marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		marker.setAttribute(IMarker.CHAR_START, problem.getStartPos());
		marker.setAttribute(IMarker.CHAR_END, problem.getEndPos());
	}
	
}