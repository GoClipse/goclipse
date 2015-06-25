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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.resources.IProject;

public class UpdateEvent<INFO> {
	
	public final IProject project;
	public final INFO newProjectInfo;
	
	public UpdateEvent(IProject project, INFO newProjectInfo) {
		this.project = assertNotNull(project);
		this.newProjectInfo = newProjectInfo;
	}
	
}