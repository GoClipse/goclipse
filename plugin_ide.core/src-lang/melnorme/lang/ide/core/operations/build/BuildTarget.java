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
package melnorme.lang.ide.core.operations.build;

import static melnorme.utilbox.core.CoreUtil.areEqual;

import java.nio.file.Path;

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.AbstractToolManager;
import melnorme.lang.ide.core.operations.OperationInfo;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.HashcodeUtil;

public class BuildTarget {
	
	protected final boolean enabled;
	protected final String targetName;
	
	public BuildTarget(boolean enabled, String targetName) {
		this.enabled = enabled;
		this.targetName = targetName;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public String getTargetName() {
		return targetName;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof BuildTarget)) return false;
		
		BuildTarget other = (BuildTarget) obj;
		
		return 
				areEqual(enabled, other.enabled) &&
				areEqual(targetName, other.targetName);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(enabled, targetName);
	}
	
	/* -----------------  ----------------- */
	
	protected Path getSDKToolPath() throws CommonException {
		return getToolManager().getSDKToolPath();
	}
	
	public AbstractToolManager getToolManager() {
		return LangCore.getToolManager();
	}
	
	public CommonBuildTargetOperation newBuildTargetOperation(OperationInfo parentOpInfo, IProject project,
			boolean fullBuild) throws CommonException {
		Path buildToolPath = getToolManager().getSDKToolPath();
		BuildManager buildMgr = LangCore.getBuildManager();
		return buildMgr.createBuildTargetOperation(parentOpInfo, project, buildToolPath, this, fullBuild);
	}
	
}