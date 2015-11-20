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
package com.googlecode.goclipse.core.operations;

import java.nio.file.Path;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.googlecode.goclipse.core.GoEnvironmentPrefs;
import com.googlecode.goclipse.tooling.GoSDKLocationValidator;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.AbstractToolManager;
import melnorme.lang.ide.core.utils.process.AbstractRunProcessTask;
import melnorme.lang.tooling.data.IValidatedField;
import melnorme.lang.tooling.ops.util.PathValidator;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

/**
 * Manager for running various go tools, usually for build.
 * Note that running such tools under this class will notify Eclipse console listeners.
 */
public class GoToolManager extends AbstractToolManager {
	
	public static GoToolManager getDefault() {
		return (GoToolManager) LangCore.getToolManager();
	}
	
	@Override
	protected PathValidator getSDKToolPathValidator() {
		return new GoSDKLocationValidator();
	}
	
	@Override
	protected IValidatedField<Path> getSDKToolPathField(IProject project) {
		return new ValidatedSDKToolPath(project, getSDKToolPathValidator()) {
			@Override
			protected String getRawFieldValue2() {
				return GoEnvironmentPrefs.GO_ROOT.getEffectiveValue(project);
			}
		};
	}
	
	/* -----------------  ----------------- */
	
	public AbstractRunProcessTask newRunToolTask(GoEnvironment goEnv, List<String> commandLine, Location workingDir,
			IProgressMonitor pm) throws CoreException, CommonException {
		ProcessBuilder pb = goEnv.createProcessBuilder(commandLine, workingDir, true);
		return newRunToolOperation2(pb, pm);
	}
	
}