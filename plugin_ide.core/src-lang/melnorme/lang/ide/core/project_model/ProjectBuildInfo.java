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
package melnorme.lang.ide.core.project_model;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.nullToEmpty;

import java.util.Collections;
import java.util.Map;

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.core.BundleInfo;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildManagerMessages;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.operations.build.BuildTargetData;
import melnorme.lang.tooling.bundle.BuildConfiguration;
import melnorme.lang.tooling.data.Severity;
import melnorme.lang.tooling.data.StatusException;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Collection2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.collections.LinkedHashMap2;
import melnorme.utilbox.core.CommonException;

public class ProjectBuildInfo {
	
	protected final BuildManager buildMgr;
	protected final IProject project;
	protected final LinkedHashMap2<String, BuildTarget> buildTargets = new LinkedHashMap2<>();
	protected final BundleInfo bundleInfo;
	
	public ProjectBuildInfo(BuildManager buildManager, IProject project, 
			BundleInfo bundleInfo, Indexable<BuildTarget> buildTargets) {
		this.buildMgr = buildManager;
		this.project = project;
		this.bundleInfo = assertNotNull(bundleInfo);
		
		for(BuildTarget buildTarget : nullToEmpty(buildTargets)) {
			this.buildTargets.put(buildTarget.getBuildTargetName(), buildTarget);
		}
	}
	
	public BuildManager getBuildManager() {
		return buildMgr;
	}
	
	public IProject getProject() {
		return project;
	}
	
	public BundleInfo getBundleInfo() {
		return bundleInfo;
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
	
	public Map<String, BuildTarget> getBuildTargetsMap() {
		return Collections.unmodifiableMap(buildTargets);
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
	
	public Indexable<BuildConfiguration> getBuildConfigs() {
		return getBundleInfo().getBuildConfigurations();
	}
	
	
	/* -----------------  ----------------- */
		
	public void changeEnable(BuildTarget oldBuildTarget, boolean newEnabledValue) throws CommonException {
		BuildTargetData buildTargetData = oldBuildTarget.getDataCopy();
		buildTargetData.enabled = newEnabledValue;
		changeBuildTarget(oldBuildTarget, buildTargetData);
	}
	
	public void changeBuildTarget(String buildTargetName, BuildTargetData buildTargetData)
			throws CommonException {
		
		BuildTarget oldBuildTarget = buildMgr.getDefinedBuildTarget(project, buildTargetName);
		
		changeBuildTarget(oldBuildTarget, buildMgr.createBuildTarget3(project, buildTargetData));
	}
	
	public void changeBuildTarget(BuildTarget oldBuildTarget, BuildTargetData buildTargetData)
			throws CommonException {
		changeBuildTarget(oldBuildTarget, buildMgr.createBuildTarget3(project, buildTargetData));
	}
	
	protected void changeBuildTarget(BuildTarget buildTargetToChange, BuildTarget newBuildTarget) throws CommonException {
		boolean mutated = false;
		ArrayList2<BuildTarget> newBuildTargets = new ArrayList2<>(buildTargets.size());
		
		for(BuildTarget buildTargetCursor : getBuildTargets()) {
			if(buildTargetCursor == buildTargetToChange) {
				newBuildTargets.add(newBuildTarget);
				mutated = true;
				continue;
			}
			newBuildTargets.add(buildMgr.createBuildTarget3(project, buildTargetCursor.getData()));
		}
		if(!mutated) {
			throw new StatusException(Severity.WARNING, BuildManagerMessages.ERROR_MODEL_OUT_OF_DATE);
		}
		
		ProjectBuildInfo newProjectBuildInfo = new ProjectBuildInfo(buildMgr, project, bundleInfo, newBuildTargets);
		buildMgr.setProjectBuildInfoAndSave(project, newProjectBuildInfo);
	}
	
}