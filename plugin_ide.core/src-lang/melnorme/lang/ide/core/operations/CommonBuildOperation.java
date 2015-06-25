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
package melnorme.lang.ide.core.operations;


import java.nio.file.Path;

import org.eclipse.core.resources.IProject;

import melnorme.utilbox.core.CommonException;

public abstract class CommonBuildOperation implements IBuildTargetOperation {
	
	protected final IProject project;
	protected final LangProjectBuilder langProjectBuilder;
	
	public CommonBuildOperation(IProject project, LangProjectBuilder langProjectBuilder) {
		this.project = project;
		this.langProjectBuilder = langProjectBuilder;
	}
	
	public IProject getProject() {
		return project;
	}
	
	public Path getBuildToolPath() throws CommonException {
		return langProjectBuilder.getBuildToolPath();
	}
	
}