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
package melnorme.lang.ide.core.operations;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.Map;

import org.eclipse.core.resources.IBuildConfiguration;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import melnorme.lang.ide.core.EclipseCore;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.core.utils.operation.EclipseJobOperation;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public abstract class LangProjectBuilder extends IncrementalProjectBuilder {
	
	protected final BuildManager buildManager = LangCore.getBuildManager();
	
	public LangProjectBuilder() {
	}
	
	protected Location getProjectLocation() throws CommonException {
		return ResourceUtils.getProjectLocation2(getProject());
	}
	
	protected ToolManager getToolManager() {
		return LangCore.getToolManager();
	}
	
	/* ----------------- helpers ----------------- */
	
	protected String getBuildProblemId() {
		return LangCore_Actual.BUILD_PROBLEM_ID;
	}
	
	protected boolean isFirstProjectOfKind() throws CoreException {
		for (IBuildConfiguration buildConfig : getContext().getAllReferencedBuildConfigs()) {
			if(buildConfig.getProject().hasNature(LangCore.NATURE_ID)) {
				return false;
			}
		}
		return true;
	}
	
	protected boolean isLastProjectOfKind() throws CoreException {
		for (IBuildConfiguration buildConfig : getContext().getAllReferencingBuildConfigs()) {
			if(buildConfig.getProject().hasNature(LangCore.NATURE_ID)) {
				return false;
			}
		}
		return true;
	}
	
	/* ----------------- Build ----------------- */
	
	@Override
	protected void startupOnInitialize() {
		assertTrue(getProject() != null);
	}
	
	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
		assertTrue(kind != CLEAN_BUILD);
		
		if(kind == IncrementalProjectBuilder.AUTO_BUILD) {
			return null; // Ignore auto build
		}
		
		ArrayList2<IProject> referenced = 
			ArrayList2.createFrom(getContext().getAllReferencedBuildConfigs())
			.map((buildConfig) -> buildConfig.getProject())
			.filterx(new ArrayList2<>(), (project) -> project.hasNature(LangCore.NATURE_ID))
		;
		
		ArrayList2<IProject> referencing = 
			ArrayList2.createFrom(getContext().getAllReferencingBuildConfigs())
			.map((buildConfig) -> buildConfig.getProject())
			.filterx(new ArrayList2<>(), (project) -> project.hasNature(LangCore.NATURE_ID))
		;
		
		boolean firstCall = referenced.isEmpty();
		
		ArrayList2<IProject> allOurProjects = referencing;
		allOurProjects.add(getProject());
		
		if(!firstCall) {
			return null;
		}
		
		try {
			EclipseUtils.execute_asCore(monitor, (om) -> {
				EclipseJobOperation job = buildManager.requestMultiBuild(om, allOurProjects, false);
				if(!runAsynchronousBuild()) {
					try {
						job.join();
					} catch(InterruptedException e) {
						throw new OperationCancellation();
					}
				}
			}); 
			return null;
		} 
		catch(OperationCancellation cancel) {
			return null;
		} catch(CoreException ce) {
			if(monitor.isCanceled()) {
				// This shouldn't usually happen, a OperationCancellation should have been thrown,
				// but sometimes its not wrapped correctly.
				return null;
			}
			EclipseCore.logStatus(ce);
			throw ce;
		}
	}
	
	public boolean runAsynchronousBuild() {
		return true;
	}
	
	/* ----------------- Clean ----------------- */
	
	protected void deleteProjectBuildMarkers() {
		try {
			getProject().deleteMarkers(LangCore_Actual.BUILD_PROBLEM_ID, true, IResource.DEPTH_INFINITE);
		} catch (CoreException ce) {
			EclipseCore.logStatus(ce);
		}
	}
	
	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		deleteProjectBuildMarkers();
		
		try {
			ProcessBuilder pb = createCleanPB();
			doClean(monitor, pb);
		} catch (OperationCancellation e) {
			// return
		} catch (CommonException ce) {
			throw EclipseCore.createCoreException(ce);
		}
	}
	
	protected abstract ProcessBuilder createCleanPB() throws CoreException, CommonException;
	
	protected void doClean(IProgressMonitor pm, ProcessBuilder pb) 
			throws CoreException, CommonException, OperationCancellation {
		getToolManager().newRunBuildToolOperation(pb, EclipseUtils.om(pm)).runProcess();
	}
	
}