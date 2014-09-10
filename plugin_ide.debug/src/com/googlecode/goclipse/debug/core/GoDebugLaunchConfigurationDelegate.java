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
package com.googlecode.goclipse.debug.core;


import melnorme.lang.ide.debug.core.AbstractLangDebugLaunchConfigurationDelegate;
import melnorme.lang.ide.debug.core.GdbLaunchDelegateExtension;
import melnorme.lang.ide.debug.core.services.DebugServicesExtensions;

import org.eclipse.cdt.dsf.gdb.launching.GdbLaunch;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ISourceLocator;

public class GoDebugLaunchConfigurationDelegate extends AbstractLangDebugLaunchConfigurationDelegate {
	
	@Override
	protected GdbLaunchDelegateExtension createGdbLaunchDelegate() {
		return new GdbLaunchDelegateExtension() {
			@Override
			protected GdbLaunch createGdbLaunch(ILaunchConfiguration configuration, String mode, ISourceLocator locator)
					throws CoreException {
				return doCreateGdbLaunch(configuration, mode, locator);
			}
			
			@Override
			protected DebugServicesExtensions createServicesExtensions() {
				return new GoDebugServicesExtensions();
			};
		};
	}
	
	@Override
	protected GdbLaunch doCreateGdbLaunch(ILaunchConfiguration configuration, String mode, ISourceLocator locator) {
		return new GoGdbLaunch(configuration, mode, locator);
	}
	
}