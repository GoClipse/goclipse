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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.operations.build.BuildTargetData;
import melnorme.lang.tooling.commands.CommandInvocation;
import melnorme.utilbox.core.CommonException;

/** 
 * A {@link BuildTarget} source, derived from a parent/original BuildTarget, and overriden settings
 */
public abstract class CompositeBuildTargetSettings {
	
	protected final BuildTargetSource btSupplier;
	
	public CompositeBuildTargetSettings(BuildTargetSource btSupplier) {
		this.btSupplier = assertNotNull(btSupplier);
	}
	
	// can be null
	public abstract CommandInvocation getBuildCommand();
	
	// can be null
	public abstract String getExecutablePath();
	
	/* -----------------  ----------------- */
	
	public BuildTarget getValidBuildTarget() throws CommonException {
		BuildTarget originalBuildTarget = btSupplier.getBuildTarget();
		
		return getValidBuildTarget2(originalBuildTarget);
	}
	
	protected BuildTarget getValidBuildTarget2(BuildTarget originalBuildTarget) {
		CommandInvocation buildCommand = getBuildCommand();
		String executablePath = getExecutablePath();
		
		BuildTargetData data = originalBuildTarget.getDataCopy();
		
		if(buildCommand != null) {
			data.buildCommand = buildCommand;
		}
		if(executablePath != null) {
			data.executablePath = executablePath;
		}
		return originalBuildTarget.getDerivedBuildTarget(data);
	}
	
}