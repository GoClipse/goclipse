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

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.core.BundleInfo;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildManagerMessages;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.operations.build.BuildTarget.BuildTargetData;
import melnorme.lang.tooling.bundle.BuildConfiguration;
import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.data.StatusLevel;
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
			this.buildTargets.put(buildTarget.getTargetName(), buildTarget);
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
	
	public BuildTarget getBuildTargetFor(String name) throws CommonException {
		return buildMgr.getBuildTargetFor2(this, name);
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
	
	public BuildConfiguration getBuildConfiguration_nonNull(String buildConfigName) throws CommonException {
		for(BuildConfiguration buildConfig : getBuildConfigs()) {
			if(buildConfig.getName().equals(buildConfigName)) {
				return buildConfig;
			}
		}
		throw new CommonException(BuildManagerMessages.BuildConfig_NotFound(buildConfigName));
	}
	
	/* -----------------  ----------------- */
		
	public void changeEnable(BuildTarget oldBuildTarget, boolean newEnabledValue) throws StatusException {
		BuildTargetData buildTargetData = oldBuildTarget.getDataCopy();
		buildTargetData.enabled = newEnabledValue;
		changeBuildTarget(oldBuildTarget, buildTargetData);
	}
	
	public void changeBuildArguments(BuildTarget oldBuildTarget, String newBuildArgumentsValue) throws StatusException {
		BuildTargetData buildTargetData = oldBuildTarget.getDataCopy();
		buildTargetData.buildArguments = newBuildArgumentsValue;
		changeBuildTarget(oldBuildTarget, buildTargetData);
	}
	
	public void changeBuildTarget(BuildTarget oldBuildTarget, BuildTargetData buildTargetData)
			throws StatusException {
		changeBuildTarget(oldBuildTarget, buildMgr.createBuildTarget(buildTargetData));
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
		
		ProjectBuildInfo newProjectBuildInfo = new ProjectBuildInfo(buildMgr, project, bundleInfo, newBuildTargets);
		buildMgr.setAndSaveProjectBuildInfo(project, newProjectBuildInfo);
	}
	
}