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
package melnorme.lang.ide.core.project_model;

public interface BuildManagerMessages {

	public String ERROR_MODEL_OUT_OF_DATE = 
			"Could not perform operation, target has changed in the meanwhile";
	
	public String MSG_BuildingProject = "Building {0} project: {1}";
	public String MSG_BuildTerminated = "Build terminated.";
	public String MSG_NoBuildTargetsEnabled = "No build targets enabled.";
	
	public String TITLE_ToggleEnabledAction = "Enable for workspace build";
	public String TITLE_RunBuildTargetAction = "Build target";
	public String INFO_BuildTargetAction = "Building target: {0}";
	
	public String MSG_Starting_LANG_Build = "Starting {0} build";
	
}
