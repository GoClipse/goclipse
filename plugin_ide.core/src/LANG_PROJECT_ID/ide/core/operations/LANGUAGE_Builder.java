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

import melnorme.lang.ide.core.operations.LangProjectBuilderExt;
import melnorme.lang.ide.core.operations.SDKLocationValidator;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.data.LocationValidator;
import melnorme.lang.tooling.ops.ToolSourceMessage;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.runtime.CoreException;

public class LANGUAGE_Builder extends LangProjectBuilderExt {
	
	public static class LANGUAGE_SDKLocationValidator extends SDKLocationValidator {
		@Override
		protected String getSDKExecutable_append() {
			return "bin/ls"; // TODO: LANG 
		}
		
		@Override
		protected String getSDKExecutableErrorMessage(Location exeLocation) {
			return "Foo executable not found."; // TODO: LANG
		}
	}
	
	public LANGUAGE_Builder() {
	}
	
	@Override
	protected LocationValidator getSDKLocationValidator() {
		return new LANGUAGE_SDKLocationValidator();
	}
	
	@Override
	protected ProcessBuilder createBuildPB() throws CoreException {
		return createSDKProcessBuilder("build"); // TODO: Lang
	}
	
	@Override
	protected void processBuildResult(ExternalProcessResult buildAllResult) throws CoreException {
		ArrayList2<ToolSourceMessage> buildErrors = new ArrayList2<>(); // TODO: Lang
		
		addErrorMarkers(buildErrors, ResourceUtils.getProjectLocation(getProject()));
	}
	
	@Override
	protected ProcessBuilder createCleanPB() throws CoreException {
		return createSDKProcessBuilder("clean"); // TODO: Lang
	}
	
}