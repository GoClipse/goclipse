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

import org.eclipse.core.resources.IProject;
import com.googlecode.goclipse.core.GoEnvironmentPrefs;
import com.googlecode.goclipse.tooling.GoSDKLocationValidator;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.AbstractToolManager;
import melnorme.lang.tooling.ops.util.PathValidator;

/**
 * Manager for running various go tools, usually for build.
 * Note that running such tools under this class will notify Eclipse console listeners.
 */
public class GoToolManager extends AbstractToolManager {
	
	public static GoToolManager getDefault() {
		return (GoToolManager) LangCore.getToolManager();
	}
	
	@Override
	public PathValidator getSDKToolPathValidator() {
		return new GoSDKLocationValidator();
	}
	
	@Override
	public String getSDKPathPreference(IProject project) {
		return GoEnvironmentPrefs.GO_ROOT.getEffectiveValue(project);
	}
	
}