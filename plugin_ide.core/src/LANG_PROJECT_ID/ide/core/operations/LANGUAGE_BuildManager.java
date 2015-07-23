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

import melnorme.lang.ide.core.launch.LaunchUtils;
import melnorme.lang.ide.core.operations.OperationInfo;
import melnorme.lang.ide.core.operations.ToolMarkersUtil;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.operations.build.BuildTarget.BuildType;
import melnorme.lang.ide.core.operations.build.CommonBuildTargetOperation;
import melnorme.lang.ide.core.project_model.AbstractBundleInfo;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.ops.ToolSourceMessage;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public final class LANGUAGE_BuildManager extends BuildManager {
	
	public LANGUAGE_BuildManager(LangBundleModel<? extends AbstractBundleInfo> bundleModel) {
		super(bundleModel);
	}
	
	@Override
	protected Indexable<BuildType> getBuildTypes_do() {
		return ArrayList2.create(
			new LANGUAGE_BuildType("<default>")
		);
	}
	
	protected class LANGUAGE_BuildType extends BuildType {
		public LANGUAGE_BuildType(String name) {
			super(name);
		}
		
		@Override
		public String getDefaultBuildOptions(BuildTarget buildTarget, IProject project) throws CommonException {
			return ".";
		}
		
		@Override
		public Path getArtifactPath(BuildTarget buildTarget, IProject project) throws CommonException {
			throw new CommonException("No default program path available");
		}
	}

	@Override
	public CommonBuildTargetOperation createBuildTargetSubOperation(OperationInfo parentOpInfo, IProject project,
			Path buildToolPath, BuildTarget buildTarget, boolean fullBuild) {
		return new LANGUAGE_BuildTargetOperation(this, parentOpInfo, project, buildToolPath, buildTarget, fullBuild);
	}
	
	/* ----------------- Build ----------------- */
	
	protected class LANGUAGE_BuildTargetOperation extends CommonBuildTargetOperation {
		
		public LANGUAGE_BuildTargetOperation(BuildManager buildManager, OperationInfo parentOpInfo, IProject project,
				Path buildToolPath, BuildTarget buildTarget, boolean fullBuild) {
			super(buildManager, parentOpInfo, project, buildToolPath, buildTarget, fullBuild);
		}
		
		@Override
		public void execute(IProgressMonitor pm) throws CoreException, CommonException, OperationCancellation {
			ProcessBuilder pb = createBuildPB();
			
			ExternalProcessResult buildResult = runBuildTool(pm, pb);
			doBuild_processBuildResult(buildResult);
		}
		
		protected ProcessBuilder createBuildPB() throws CoreException, CommonException {
			ArrayList2<String> buildCmdLine = new ArrayList2<>();
			buildCmdLine.addElements(buildTarget.getTargetName());
			buildCmdLine.addElements(LaunchUtils.getParsedArguments(buildTarget.getBuildOptions()));
			
			return getToolManager().createSDKProcessBuilder(getProject(), buildCmdLine.toArray(String.class));
		}
		
		@SuppressWarnings("unused")
		protected void doBuild_processBuildResult(ExternalProcessResult buildResult) throws CoreException {
			ArrayList2<ToolSourceMessage> buildErrors = new ArrayList2<>(); 
			
			// TODO: Lang process build result
			
			ToolMarkersUtil.addErrorMarkers(buildErrors, ResourceUtils.getProjectLocation(project));
		}
	}
	
}