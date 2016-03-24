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
import melnorme.lang.ide.core.operations.build.ValidatedBuildTarget;
import melnorme.lang.tooling.ops.util.ValidationMessages;
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
		
		return getValidBuildTarget(originalBuildTarget);
	}
	
	public BuildTarget getValidBuildTarget(BuildTarget originalBuildTarget) {
		BuildTargetData data = originalBuildTarget.getDataCopy();
		if(getBuildArguments() != null) {
			data.buildArguments = getBuildArguments();
		}
		if(getExecutablePath() != null) {
			data.executablePath = getExecutablePath();
		}
		return getBuildManager().createBuildTarget2(data);
	}
	
	protected ValidatedBuildTarget getValidatedBuildTarget() throws CommonException {
		return getBuildManager().getValidatedBuildTarget(getValidProject(), getValidBuildTarget());
	}
	
	/* -----------------  ----------------- */
	
	public String getOriginalBuildArguments() throws CommonException {
		return getValidatedOriginalBuildTarget().getEffectiveBuildArguments();
	}
	
	public String getOriginalExecutablePath() throws CommonException {
		return getValidatedOriginalBuildTarget().getEffectiveValidExecutablePath();
	}
	
	/* -----------------  ----------------- */ 
	
	public String getEffectiveBuildArguments() throws CommonException {
		return getValidatedBuildTarget().getEffectiveBuildArguments();
	}
	
	public Location getValidExecutableLocation() throws CommonException {
		return getValidExecutableLocation(getValidatedBuildTarget().getEffectiveValidExecutablePath());
	}
	
	/* -----------------  ----------------- */ 
	
	public Location getValidExecutableLocation(String exeFilePathString) throws CommonException {
		if(exeFilePathString == null || exeFilePathString.isEmpty()) {
			throw new CommonException(LaunchMessages.BuildTarget_NoArtifactPathSpecified);
		}
		
		Location exeFileLocation = Location.create(getProjectLocation(), exeFilePathString);
		
		if(exeFileLocation.toFile().exists() && !exeFileLocation.toFile().isFile()) {
			error(ValidationMessages.Location_NotAFile(exeFileLocation));
		}
		return exeFileLocation;
	}
	
}