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

import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildManagerMessages;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.data.StatusLevel;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Collection2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.collections.LinkedHashMap2;
import melnorme.utilbox.core.CommonException;

public class ProjectBuildInfo {
	
	protected final BuildManager buildManager;
	protected final IProject project;
	protected final LinkedHashMap2<String, BuildTarget> buildTargets = new LinkedHashMap2<>();
	
	public ProjectBuildInfo(BuildManager buildManager, IProject project, Indexable<BuildTarget> buildTargetsArray) {
		this.buildManager = buildManager;
		this.project = project;
		for(BuildTarget buildTarget : nullToEmpty(buildTargetsArray)) {
			buildTargets.put(buildTarget.getTargetName(), buildTarget);
		}
	}
	
	public BuildManager getBuildManager() {
		return buildManager;
	}
	
	public IProject getProject() {
		return project;
	}
	
	public Collection2<BuildTarget> getBuildTargets() {
		return buildTargets.getValuesView();
	}
	
	/**
	 * @return a BuildTarget explicitly defined in this build info. 
	 */
	public BuildTarget getDefinedBuildTarget(String name) {
		return buildTargets.get(name);
	}
	
	public BuildTarget getBuildTargetFor(String name) throws CommonException {
		return buildManager.getBuildTargetFor(this, name);
	}
	
	public BuildTarget getDefaultBuildTarget() throws CommonException {
		if(buildTargets.size() == 0) {
			throw new CommonException("No targets available");
		}
		return buildTargets.iterator().next().getValue();
	}
	
	public ArrayList2<BuildTarget> getEnabledTargets() {
		ArrayList2<BuildTarget> enabledTargets = ArrayList2.create();
		for(BuildTarget buildTarget : nullToEmpty(getBuildTargets())) {
			if(buildTarget.isEnabled()) {
				enabledTargets.add(buildTarget);
			}
		}
		return enabledTargets;
	}
	
	/* -----------------  ----------------- */
		
	public void changeEnable(BuildTarget oldBuildTarget, boolean newEnabledValue) throws StatusException {
		changeBuildTarget(oldBuildTarget, buildManager.createBuildTarget(
			oldBuildTarget.getTargetName(), 
			newEnabledValue,
			oldBuildTarget.getBuildOptions()
		));
	}
	
	public void changeOptions(BuildTarget oldBuildTarget, String newOptionsValue) throws StatusException {
		changeBuildTarget(oldBuildTarget, buildManager.createBuildTarget(
			oldBuildTarget.getTargetName(), 
			oldBuildTarget.isEnabled(),
			newOptionsValue
		));
	}
	
	protected void changeBuildTarget(BuildTarget oldBuildTarget, BuildTarget newBuildTarget) throws StatusException {
		boolean mutated = false;
		ArrayList2<BuildTarget> newBuildTargets = new ArrayList2<>(buildTargets.size());
		
		for(BuildTarget buildTargetCursor : getBuildTargets()) {
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
		buildManager.setAndSaveProjectBuildInfo(project, newProjectBuildInfo);
	}
	
}