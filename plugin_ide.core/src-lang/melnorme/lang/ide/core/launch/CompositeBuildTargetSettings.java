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
package melnorme.lang.ide.core.launch;

import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.operations.build.BuildTargetData;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

/** 
 * A {@link BuildTarget} source, derived from a parent/original BuildTarget, and overriden settings
 */
public abstract class CompositeBuildTargetSettings extends BuildTargetSource 
	implements IBuildTargetSettings {
	
	public CompositeBuildTargetSettings() {
	}
	
	
	public void validate() throws CommonException {
		getValidBuildTarget();
		getEffectiveBuildArguments();
		getValidExecutableLocation();
	}
	
	
	/* -----------------  ----------------- */
	
	public BuildTarget getValidBuildTarget() throws CommonException {
		BuildTarget originalBuildTarget = getOriginalBuildTarget();
		
		return getValidBuildTarget2(originalBuildTarget);
	}
	
	public BuildTarget getValidBuildTarget2(BuildTarget originalBuildTarget) throws CommonException {
		BuildTargetData data = originalBuildTarget.getDataCopy();
		if(getBuildArguments() != null) {
			data.buildArguments = getBuildArguments();
		}
		/* FIXME: review*/
		if(getExecutablePath() != null) {
			data.executablePath = getExecutablePath();
		}
		return getBuildManager().createBuildTarget3(getValidProject(), data);
	}
	
	
	/* -----------------  ----------------- */ 
	
	public String getEffectiveBuildArguments() throws CommonException {
		return getValidBuildTarget().getEffectiveBuildArguments();
	}
	
	public Location getValidExecutableLocation() throws CommonException {
		return getValidBuildTarget().getValidExecutableLocation2(getValidBuildTarget().getEffectiveValidExecutablePath());
	}
	
	
}