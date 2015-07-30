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

import static melnorme.utilbox.core.CoreUtil.array;

import java.nio.file.Path;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import melnorme.lang.ide.core.operations.OperationInfo;
import melnorme.lang.ide.core.operations.ToolMarkersUtil;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTargetValidator;
import melnorme.lang.ide.core.operations.build.CommonBuildTargetOperation;
import melnorme.lang.ide.core.project_model.AbstractBundleInfo;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.ops.ToolSourceMessage;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
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
		public String getDefaultBuildOptions(BuildTargetValidator buildTargetValidator) throws CommonException {
			return ".";
		}
		
		@Override
		public String getArtifactPath(BuildTargetValidator buildTargetValidator) throws CommonException {
			return "default_artifact.exe"; // TODO: LANG
		}
		
		@Override
		public CommonBuildTargetOperation getBuildOperation(BuildTargetValidator buildTargetValidator,
				OperationInfo opInfo, Path buildToolPath, boolean fullBuild) throws CommonException, CoreException {
			return new LANGUAGE_BuildTargetOperation(buildTargetValidator, opInfo, buildToolPath, fullBuild);
		}
	}
	
	@Override
	public BuildTargetValidator createBuildTargetValidator(IProject project, String buildConfigName,
			String buildTypeName, String buildOptions) throws CommonException {
		return new BuildTargetValidator(project, buildConfigName, buildTypeName, buildOptions);
	}
	
	/* ----------------- Build ----------------- */
	
	protected class LANGUAGE_BuildTargetOperation extends CommonBuildTargetOperation {
		
		public LANGUAGE_BuildTargetOperation(BuildTargetValidator buildTargetValidator, OperationInfo parentOpInfo, 
				Path buildToolPath, boolean fullBuild) throws CommonException, CoreException {
			super(buildTargetValidator.buildMgr, buildTargetValidator, parentOpInfo, buildToolPath, fullBuild);
		}
		
		@Override
		protected void addToolCommand(ArrayList2<String> commands)
				throws CoreException, CommonException, OperationCancellation {
			//super.addToolCommand(commands);
		}
		
		@Override
		protected String[] getMainArguments() throws CoreException, CommonException, OperationCancellation {
			return array(getConfigurationName());
		}
		
		@Override
		protected ProcessBuilder getProcessBuilder(ArrayList2<String> commands)
				throws CommonException, OperationCancellation, CoreException {
			Location projectLocation = ResourceUtils.getProjectLocation(getProject());
			return getToolManager().createToolProcessBuilder(getBuildToolPath(), projectLocation, 
				commands.toArray(String.class));
		}
		
		@Override
		protected void processBuildOutput(ExternalProcessResult processResult) throws CoreException {
			ArrayList2<ToolSourceMessage> buildErrors = new ArrayList2<>(); 
			
			// TODO: Lang process build result
			
			ToolMarkersUtil.addErrorMarkers(buildErrors, ResourceUtils.getProjectLocation(project));
		}
	}
	
}