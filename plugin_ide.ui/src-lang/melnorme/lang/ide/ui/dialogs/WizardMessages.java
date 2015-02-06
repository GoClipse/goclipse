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
package melnorme.lang.ide.ui.dialogs;

import melnorme.lang.ide.ui.WizardMessages_Actual;


public final class WizardMessages extends WizardMessages_Actual {
	
	private WizardMessages() {
	}
	
	public static final String LangNewProject_createProjectError_title = 
			"Error creating project.";
	
	public static final String LangNewProject_removeProjectError_title = 
			"Error removing temporary project.";

	
	public static final String LangNewProject_NameGroup_label = 
			"&Project name:";
	public static final String LangNewProject_Name_error_emptyProjectName = 
			"Enter a project name.";
	public static final String LangNewProject_Name_error_projectAlreadyExists = 
			"A project with given name already exists.";
	
	public static final String LangNewProject_LocationGroup_label = 
			"Location:";
	public static final String LangNewProject_Location_UseDefault_Label = 
			"Use default location.";
	public static final String LangNewProject_Location_UseCustom_Label = 
			"Use custom location:";
	public static String LangNewProject_Location_Directory_label =
			"Directory: ";
	public static String LangNewProject_Location_Directory_buttonLabel =
			"B&rowse...";
	public static final String LangNewProject_Location_invalidLocation = 
			"Invalid project location.";
	public static final String LangNewProject_Location_projectExistsCannotChangeLocation = 
			"Project already exists, cannot change location.";
	
	public static final String LangNewProject_DetectGroup_projectExists =
			"The specified project already exists in the workspace. " +
			"The wizard will try to adapt the existing workspace project to a "+LangNewProject_projectType+".";
	
	public static final String LangNewProject_DetectGroup_message =
			"A directory already exists at the specified location. " +
			"The wizard will try to detect an existing external project,"
			+ " or convert the existing directory into a new project.";
	
}