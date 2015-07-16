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
import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.data.StatusLevel;

public class LangProjectValidator {
	
	public IProject validateProject(String projectName, String natureID) throws StatusException {
		if(projectName.isEmpty()) {
			throw new StatusException(StatusLevel.ERROR, msg_NoProjectName());
		}
		
		IProject project = ResourceUtils.getWorkspaceRoot().getProject(projectName);
		if(!project.exists()) {
			throw new StatusException(StatusLevel.ERROR, msg_ProjectDoesNotExist());
		}
		try {
			if(natureID != null && !project.hasNature(natureID)) {
				throw new StatusException(StatusLevel.ERROR, msg_NotAValidLangProject());
			}
			
			return project;
		} catch (CoreException ce) {
			LangCore.logStatus(ce);
			throw new StatusException(StatusLevel.ERROR, ce.getMessage());
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