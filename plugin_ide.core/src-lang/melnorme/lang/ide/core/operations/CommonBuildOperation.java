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

import melnorme.lang.ide.core.operations.IBuildTargetOperation;
import melnorme.lang.ide.core.operations.LangProjectBuilderExt;
import melnorme.utilbox.core.CommonException;

import org.eclipse.core.resources.IProject;

public abstract class CommonBuildOperation implements IBuildTargetOperation{
	
	protected final IProject project;
	protected final LangProjectBuilderExt langProjectBuilder;
	
	public CommonBuildOperation(IProject project, LangProjectBuilderExt langProjectBuilder) {
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