/*******************************************************************************
 * Copyright (c) 2013 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.core.launch;


import org.eclipse.debug.core.ILaunchConfiguration;

import melnorme.lang.ide.core.launch.ProcessLaunchInfoValidator;
import melnorme.lang.ide.launching.LangLaunchConfigurationDelegate;

public class GoLaunchConfigurationDelegate extends LangLaunchConfigurationDelegate {
	
	@Override
	protected ProcessLaunchInfoValidator getLaunchValidator(ILaunchConfiguration config) {
		return new GoLaunchConfigurationValidator(config);
	}
	
}