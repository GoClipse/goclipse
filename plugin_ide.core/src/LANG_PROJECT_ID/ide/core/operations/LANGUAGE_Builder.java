/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package LANG_PROJECT_ID.ide.core.operations;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import melnorme.lang.ide.core.operations.BuildTarget;
import melnorme.lang.ide.core.operations.IBuildTargetOperation;
import melnorme.lang.ide.core.operations.LangBuildManagerProjectBuilder;
import melnorme.lang.ide.core.operations.LangProjectBuilder;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.data.LANGUAGE_SDKLocationValidator;
import melnorme.lang.tooling.data.LocationValidator;
import melnorme.lang.tooling.ops.ToolSourceMessage;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public class LANGUAGE_Builder extends LangBuildManagerProjectBuilder {
	
	public LANGUAGE_Builder() {
	}
	
	@Override
	protected LocationValidator getBuildToolPathValidator() {
		return new LANGUAGE_SDKLocationValidator();
	}
	
	@Override
	protected ProcessBuilder createCleanPB() throws CoreException, CommonException {
		return createSDKProcessBuilder("clean"); // TODO: Lang
	}
	
	/* ----------------- Build ----------------- */
	
	@Override
	protected IBuildTargetOperation newBuildOperation(IProject project, LangProjectBuilder projectBuilder,
			BuildTarget buildConfig) {
		return new AbstractRunBuildOperation() {
			
			@Override
			protected ProcessBuilder createBuildPB() throws CoreException, CommonException {
				return createSDKProcessBuilder("build"); // TODO: Lang
			}
			
			@Override
			protected void doBuild_processBuildResult(ExternalProcessResult buildAllResult) throws CoreException {
				ArrayList2<ToolSourceMessage> buildErrors = new ArrayList2<>(); // TODO: Lang
				
				addErrorMarkers(buildErrors, ResourceUtils.getProjectLocation(getProject()));
			}
		};
	}
	
}