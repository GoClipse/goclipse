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
package melnorme.lang.ide.core.utils;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCoreMessages;
import melnorme.lang.tooling.data.AbstractValidator2;
import melnorme.lang.tooling.data.StatusException;

public class ProjectValidator extends AbstractValidator2 {
	
	protected final String natureId;
	
	public ProjectValidator() {
		this(null);
	}
	
	public ProjectValidator(String natureId) {
		this.natureId = natureId;
	}
	
	public IProject getProject(String projectName) throws StatusException {
		return getProject(projectName, natureId);
	}
	
	public IProject getProject(String projectName, String natureID) throws StatusException {
		if(projectName.isEmpty()) {
			throw error(msg_NoProjectName());
		}
		
		IProject project = ResourceUtils.getWorkspaceRoot().getProject(projectName);
		if(!project.exists()) {
			throw error(msg_ProjectDoesNotExist());
		}
		try {
			if(natureID != null && !project.hasNature(natureID)) {
				throw error(msg_NotAValidLangProject());
			}
			
			return project;
		} catch (CoreException ce) {
			LangCore.logStatus(ce);
			throw error(ce.getMessage());
		}
	}
	
	protected String msg_NoProjectName(){
		return LangCoreMessages.error_selectProject;
	}
	
	protected String msg_ProjectDoesNotExist() {
		return LangCoreMessages.error_projectDoesNotExist;
	}
	
	protected String msg_NotAValidLangProject() {
		return LangCoreMessages.error_notAValidProject;
	}
	
}