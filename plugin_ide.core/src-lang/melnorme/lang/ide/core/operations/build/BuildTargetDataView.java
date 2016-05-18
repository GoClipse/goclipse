/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.operations.build;

import melnorme.lang.tooling.commands.CommandInvocation;

public interface BuildTargetDataView {
	
	String getTargetName();
	
	boolean isNormalBuildEnabled();
	
	boolean isAutoBuildEnabled();
	
	CommandInvocation getBuildCommand();
	
	String getExecutablePath();
	
	
	default BuildTargetData copy() {
		return new BuildTargetData(
			getTargetName(),
			isNormalBuildEnabled(),
			isAutoBuildEnabled(),
			getBuildCommand(),
			getExecutablePath()
		);
	}
	
}