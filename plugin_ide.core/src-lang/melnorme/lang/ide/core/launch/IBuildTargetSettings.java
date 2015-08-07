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
package melnorme.lang.ide.core.launch;

import melnorme.utilbox.core.CommonException;

public interface IBuildTargetSettings {
	
	// can be null
	String getProjectName() throws CommonException;
	
	// can be null
	String getBuildTargetName();
	
	// can be null
	String getBuildArguments();
	
	// can be null
	String getArtifactPath();
	
}