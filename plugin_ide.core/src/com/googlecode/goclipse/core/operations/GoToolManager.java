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
package com.googlecode.goclipse.core.operations;

import com.googlecode.goclipse.tooling.GoSDKLocationValidator;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.ToolManager;
import melnorme.lang.tooling.ops.util.PathValidator;

public class GoToolManager extends ToolManager {
	
	public static GoToolManager getDefault() {
		return (GoToolManager) LangCore.getToolManager();
	}
	
	@Override
	public PathValidator getSDKToolPathValidator() {
		return new GoSDKLocationValidator();
	}
	
}