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
package melnorme.lang.ide.core.operations.build;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.nio.file.Path;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import melnorme.lang.ide.core.operations.AbstractToolManagerOperation;
import melnorme.lang.ide.core.operations.OperationInfo;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public abstract class CommonBuildTargetOperation extends AbstractToolManagerOperation {
	
	protected final BuildManager buildManager;
	protected final OperationInfo parentOperationInfo;
	protected final BuildTarget buildTarget;
	protected final Path buildToolPath;
	protected final boolean fullBuild;
	
	public CommonBuildTargetOperation(BuildManager buildManager, OperationInfo parentOpInfo, IProject project,
			Path buildToolPath, BuildTarget buildTarget, boolean fullBuild) {
		super(project);
		this.buildManager = assertNotNull(buildManager);
		this.buildToolPath = buildToolPath;
		this.fullBuild = fullBuild;
		this.parentOperationInfo = assertNotNull(parentOpInfo);
		this.buildTarget = assertNotNull(buildTarget);
	}
	
	protected Path getBuildToolPath() throws CommonException {
		return buildToolPath;
	}
	
	protected String getEffectiveBuildOptions() throws CommonException {
		String buildOptions = buildTarget.getBuildOptions();
		if(buildOptions != null) {
			return buildOptions;
		}
		return buildTarget.getDefaultBuildOptions(project);
	}
	
	@Override
	public abstract void execute(IProgressMonitor pm) throws CoreException, CommonException, OperationCancellation;
	
}