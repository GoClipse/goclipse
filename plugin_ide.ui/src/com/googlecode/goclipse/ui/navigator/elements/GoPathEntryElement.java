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
package com.googlecode.goclipse.ui.navigator.elements;

import java.nio.file.Path;

import org.eclipse.core.resources.IProject;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.tooling.env.GoPath;

public class GoPathEntryElement extends GoPathElement {
	
	protected final Path goPathEntryPath;
	protected final IProject project;
	protected boolean projectInsideGoPath;
	
	public GoPathEntryElement(Path goPathEntryPath, IProject project, GoPath goPath) {
		super("GOPATH", goPathEntryPath.resolve("src").toFile());
		this.goPathEntryPath = goPathEntryPath;
		this.project = project;
		
		this.projectInsideGoPath = GoProjectEnvironment.isProjectInsideGoPath(project, goPath);
	}
	
	public IProject getProject() {
		return project;
	}
	
	public boolean isProjectInsideGoPath() {
		return projectInsideGoPath;
	}
	
}