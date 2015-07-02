/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.project_model;

import static melnorme.utilbox.core.CoreUtil.nullToEmpty;

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.core.operations.BuildTarget;
import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.data.StatusLevel;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;

public class ProjectBuildInfo {
	
	protected final BuildManager buildManager;
	protected final IProject project;
	protected final Indexable<BuildTarget> buildTargets;
	
	public ProjectBuildInfo(BuildManager buildManager, IProject project, Indexable<BuildTarget> buildTargets) {
		this.buildManager = buildManager;
		this.project = project;
		this.buildTargets = nullToEmpty(buildTargets);
	}
	
	public BuildManager getBuildManager() {
		return buildManager;
	}
	
	public IProject getProject() {
		return project;
	}
	
	public Indexable<BuildTarget> getBuildTargets() {
		return buildTargets;
	}
	
	/* -----------------  ----------------- */
		
	public void changeEnable(BuildTarget oldBuildTarget, boolean newEnabledValue) throws StatusException {
		BuildTarget newBuildTarget = buildManager.createBuildTarget(newEnabledValue, oldBuildTarget.getTargetName());
		
		changeBuildTarget(oldBuildTarget, newBuildTarget);
	}
	
	public void changeBuildTarget(BuildTarget oldBuildTarget, BuildTarget newBuildTarget) throws StatusException {
		boolean mutated = false;
		ArrayList2<BuildTarget> newBuildTargets = new ArrayList2<>(buildTargets.size());
		
		for(BuildTarget buildTargetCursor : buildTargets) {
			if(buildTargetCursor == oldBuildTarget) {
				newBuildTargets.add(newBuildTarget);
				mutated = true;
				continue;
			}
			newBuildTargets.add(buildTargetCursor);
		}
		if(!mutated) {
			throw new StatusException(StatusLevel.WARNING, BuildManagerMessages.ERROR_MODEL_OUT_OF_DATE);
		}
		
		ProjectBuildInfo newProjectBuildInfo = new ProjectBuildInfo(buildManager, project, newBuildTargets);
		buildManager.setBuildProjectInfo(project, newProjectBuildInfo);
	}
	
}