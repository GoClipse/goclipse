/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 		IBM Corporation - initial implementation
 *      Bruno Medeiros - Lang refactoring
 *******************************************************************************/
package melnorme.lang.ide.core;

import melnorme.utilbox.misc.ArrayUtil;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public abstract class LangNature implements IProjectNature {
	
	public static final String NATURE_ID = LangCore_Actual.NATURE_ID;
	
	protected IProject project;
	
	@Override
	public void setProject(IProject project) {
		this.project = project;
	}
	
	@Override
	public IProject getProject() {
		return this.project;
	}
	
	@Override
	public void configure() throws CoreException {
		addToBuildSpec(getBuilderId());
	}
	
	@Override
	public void deconfigure() throws CoreException {
		removeFromBuildSpec(getBuilderId());
	}
	
	protected abstract String getBuilderId();
	
	/**
	 * Adds a builder to the build spec for the configured project.
	 */
	protected void addToBuildSpec(String builderID) throws CoreException {
		IProjectDescription description = project.getDescription();
		ICommand[] commands = description.getBuildSpec();
		int commandIndex = getCommandIndex(commands, builderID);
		if (commandIndex == -1) {
			ICommand command = description.newCommand();
			command.setBuilderName(builderID);
			command.setBuilding(IncrementalProjectBuilder.AUTO_BUILD, false);
			
			// Add a build command to the build spec
			ICommand[] newCommands = ArrayUtil.prepend(command, commands);
			description.setBuildSpec(newCommands);
			project.setDescription(description, null);
		}
	}
	
	/** Removes the given builder from the build spec for the configured project. */
	protected void removeFromBuildSpec(String builderID) throws CoreException {
		IProjectDescription description = project.getDescription();
		
		ICommand[] commands = description.getBuildSpec();
		int commandIndex = getCommandIndex(commands, builderID);
		if(commandIndex != -1) {
			commands = ArrayUtil.removeAt(commands, commandIndex);
			description.setBuildSpec(commands);
			project.setDescription(description, null);
		}
	}
	
	public static void disableAutoBuildMode(IProject project, String builderId, IProgressMonitor monitor) 
			throws CoreException {
		IProjectDescription description = project.getDescription();
		ICommand[] buildSpec = description.getBuildSpec();
		for(ICommand command : buildSpec) {
			if(command.getBuilderName().equals(builderId)) {
				command.setBuilding(IncrementalProjectBuilder.AUTO_BUILD, false);
			}
		}
		description.setBuildSpec(buildSpec);
		project.setDescription(description, monitor);
	}
	
	protected static int getCommandIndex(ICommand[] buildSpec, String builderID) {
		for (int i = 0; i < buildSpec.length; ++i) {
			if (buildSpec[i].getBuilderName().equals(builderID)) {
				return i;
			}
		}
		return -1;
	}
	
	/* ----------------- util methods ----------------- */
	
	public static boolean isAccessible(IProject project) throws CoreException {
		return project.isAccessible() && project.hasNature(NATURE_ID);
	}
	
	public static boolean isAccessible(IProject project, boolean logOnError){
		try {
			return project.isAccessible() && project.hasNature(NATURE_ID);
		} catch (CoreException e) {
			if(logOnError) {
				LangCore.logError("Error trying to determine project nature.", e);
			}
			return false;
		}
	}
	
}