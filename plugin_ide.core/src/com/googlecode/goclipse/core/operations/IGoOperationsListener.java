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
package com.googlecode.goclipse.core.operations;

import melnorme.lang.ide.core.operations.ILangOperationsListener;
import org.eclipse.core.resources.IProject;

public interface IGoOperationsListener extends ILangOperationsListener {
	
	public void handleBuildStarted(IProject project, boolean clearConsole);
	
	public void handleBuildTerminated(IProject project);
	
}