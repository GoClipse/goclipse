/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.core;

import org.eclipse.core.resources.IProject;

public class GoEnvironmentPrefs2 {
	
	protected static final String GOPATH_EnvName = "GOPATH";
	
	protected static String getEffectiveGoPath(IProject project) {
		String goPathPref = GoEnvironmentPrefConstants.GO_PATH.get(project);
		if(goPathPref.isEmpty()) {
			return System.getenv(GOPATH_EnvName);
		}
		return goPathPref;
	}
	
}