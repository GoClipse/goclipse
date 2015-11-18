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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.concurrent.ExecutorService;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.engine.SourceModelManager.StructureInfo;
import melnorme.lang.ide.core.utils.CoreExecutors;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.ast.ParserError;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.ownership.IDisposable;

public class ProblemMarkerUpdater implements IDisposable {
	
	protected final ExecutorService problemsExecutor = CoreExecutors.newExecutorTaskAgent(getClass());
	protected SourceModelManager sourceModelManager;
	
	public ProblemMarkerUpdater() {
	}
	
	public void install(SourceModelManager sourceModelManager) {
		this.sourceModelManager = sourceModelManager;
		sourceModelManager.addListener(problemUpdaterListener);
		sourceModelManager.asOwner().bind(this);
	}
	
	@Override
	public void dispose() {
		sourceModelManager.removeListener(problemUpdaterListener);
		problemsExecutor.shutdownNow();
	}
	
	protected final IStructureModelListener problemUpdaterListener = new IStructureModelListener() {
		@Override
		public void dataChanged(StructureInfo structureInfo) {
			Location location = structureInfo.getLocation();
			if(location == null)
				return;
			
			assertTrue(Job.getJobManager().currentRule() == null);
			
			problemsExecutor.submit(new UpdateProblemMarkersTask(structureInfo));
		}
	};
	
	protected static class UpdateProblemMarkersTask implements Runnable {
		
		protected final StructureInfo structureInfo;
		protected final Location location;
		protected final SourceFileStructure structure;
		
		public UpdateProblemMarkersTask(StructureInfo structureInfo) {
			this.structureInfo = assertNotNull(structureInfo);
			this.location = assertNotNull(structureInfo.getLocation());
			this.structure = structureInfo.getStoredData();
		}
		
		@Override
		public void run() {
			try {
				checkIsStillValid();
				
				updateProblemMarkers();
			} catch(CoreException ce) {
				LangCore.logStatus(ce);
			} catch(OperationCancellation e) {
				return;
			}
		}
		
		protected void checkIsStillValid() throws OperationCancellation {
			if(structureInfo.isStale(structure)) {
				// A new update is on the way, so ignore these marker updates
				throw new OperationCancellation();
			}
		}
		
		protected void updateProblemMarkers() throws CoreException {
			// Review if this can run outside lock
			IFile[] files = ResourceUtils.getWorkspaceRoot().findFilesForLocationURI(location.toUri());
			if(files.length == 0) {
				return;
			}
			
			final IFile file = files[0];
			
			ResourceUtils.getWorkspace().run(new IWorkspaceRunnable() {
				@Override
				public void run(IProgressMonitor monitor) throws CoreException {
					try {
						doCreateProblemMarkers(file);
					} catch(OperationCancellation e) {
						return;
					}
				}
			}, file, IWorkspace.AVOID_UPDATE, null);
		}
	
		protected void doCreateProblemMarkers(IFile file) throws CoreException, OperationCancellation {
			checkIsStillValid();
			
			if(!file.exists()) {
				return; // It could have been removed in the meanwhile.
			}
			
			file.deleteMarkers(LangCore_Actual.SOURCE_PROBLEM_ID, true, IResource.DEPTH_ZERO);
			
			if(structure == null) {
				return;
			}
			
			for (ParserError problem : structure.getParserProblems()) {
	//			checkIsStillValid();
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
}