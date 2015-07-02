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
package LANG_PROJECT_ID.ide.core.operations;

import java.nio.file.Path;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import melnorme.lang.ide.core.operations.BuildMarkersUtil;
import melnorme.lang.ide.core.operations.BuildTarget;
import melnorme.lang.ide.core.operations.CommonBuildTargetOperation;
import melnorme.lang.ide.core.operations.OperationInfo;
import melnorme.lang.ide.core.project_model.BuildManager;
import melnorme.lang.ide.core.project_model.BundleManifestResourceListener;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.ops.ToolSourceMessage;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public final class LANGUAGE_BuildManager extends BuildManager {
	
	@Override
	protected BundleManifestResourceListener init_createResourceListener() {
		return new ManagerResourceListener(new org.eclipse.core.runtime.Path("lang.bundle"));
	}

	@Override
	public CommonBuildTargetOperation createBuildTargetOperation(OperationInfo parentOpInfo, IProject project,
			Path buildToolPath, BuildTarget buildTarget, boolean fullBuild) {
		return new LANGUAGE_BuildTargetOperation(parentOpInfo, project, buildToolPath, buildTarget, fullBuild);
	}
	
	/* ----------------- Build ----------------- */
	
	// TODO: LANG Build operation
	protected class LANGUAGE_BuildTargetOperation extends CommonBuildTargetOperation {
		
		public LANGUAGE_BuildTargetOperation(OperationInfo parentOpInfo, IProject project,
				Path buildToolPath, BuildTarget buildTarget, boolean fullBuild) {
			super(parentOpInfo, project, buildToolPath, buildTarget, fullBuild);
		}
		
		@Override
		public void execute(IProgressMonitor pm) throws CoreException, CommonException, OperationCancellation {
			ProcessBuilder pb = createBuildPB();
			
			ExternalProcessResult buildResult = runBuildTool(pm, pb);
			doBuild_processBuildResult(buildResult);
		}
		
		protected ProcessBuilder createBuildPB() throws CoreException, CommonException {
			return getToolManager().createSDKProcessBuilder(getProject(), "build"); // TODO: Lang
		}
		
		@SuppressWarnings("unused")
		protected void doBuild_processBuildResult(ExternalProcessResult buildResult) throws CoreException {
			ArrayList2<ToolSourceMessage> buildErrors = new ArrayList2<>(); 
			
			// TODO: Lang process result
			
			BuildMarkersUtil.addErrorMarkers(buildErrors, ResourceUtils.getProjectLocation(project));
		}
	}
	
}