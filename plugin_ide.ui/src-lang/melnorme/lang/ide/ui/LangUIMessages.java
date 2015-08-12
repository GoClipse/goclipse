/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui;

import java.text.MessageFormat;

public abstract class LangUIMessages extends LangUIMessages_Actual {
	
	public static String getFormattedString(String message, Object... args) {
		return MessageFormat.format(message, args);
	}	
	
	public static String InternalError = 
			"Internal Error";
	
	public static String LangPlugin_error = 
			"UI plugin Error";
	public static String ExceptionDialog_seeErrorLogMessage = 
			"See error log for more details.";
	
	
	public static String mainLaunchTab_title = 
			"Main";
	
	public static final String mainTab_projectGroup =
			"Project:";
	public static String mainTab_projectButton =
			"Browse...";
	
	public static String projectField_chooseProject_title = "Project selection";
	public static String projectField_chooseProject_message = "Select a project";
	
	/* -----------------  ----------------- */
	
	public static final String BuildTargetField_title = 
			"Build Target:";
	
	public static final String BuildTargetSettings_ProgramPathField_title = 
			"Program path (for launches):";
	
	public static final String Fields_BuildArguments = "Build Arguments:";

	
	public static final String Fields_VariablesButtonLabel = 
			"Variables...";
	public static final String LaunchTab_Fields_useBuildTargetSettings = 
			"Use Build Target settings";
	
	public static final String LaunchTab_ProgramPathField_title = 
			"Program path:";
	public static String LaunchTab_ProgramPathField__searchButton = 
			"Browse...";
	public static String ProgramPathDialog_title = 
			"Program selection";
	public static String ProgramPathDialog_message =
			"Select a binary to run";
	
	
	public static String error_CannotBrowse =
			"Cannot open browse dialog.";

	
	public static final String LangArgumentsTab_Program_Arguments = 
			"Program Arguments";
	public static final String Launch_ErrorReadingConfigurationAttribute = 
			"Exception occurred reading launch configuration attribute";
	public static final String LangArgumentsTab_Arguments = 
			"Arguments";
	
	/* ----------------------------------- */
	
	
	public static final String LaunchShortcut_selectionContainsNoLaunchables = 
			"Selection contains no launchables";
	
	public static final String LaunchShortcut_selectLaunchableToLaunch = 
			"Select a launchable resource to launch";
	
	public static final String LaunchShortcut_selectLaunch_title = 
			"Launch configurations";
	public static final String LaunchShortcut_selectLaunch_message = 
			"&Select existing configuration:";
	
}