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
package com.googlecode.goclipse.builder;

import melnorme.lang.ide.core.utils.process.IExternalProcessListener;

import org.eclipse.core.resources.IProject;

public interface IGoBuildListener extends IExternalProcessListener {
	
	public void handleBuildStarted(IProject project);
	
	public void handleBuildTerminated(IProject project);
	
}