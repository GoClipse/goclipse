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
package com.googlecode.goclipse.debug.ui;

import melnorme.lang.ide.debug.ui.AbstractLangDebugTabGroup;
import melnorme.lang.ide.ui.launch.LangArgumentsTab;

import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.sourcelookup.SourceLookupTab;

import com.googlecode.goclipse.ui.launch.GoLaunchConfigurationTab;

public class GoDebugTabGroup extends AbstractLangDebugTabGroup {
	
	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		setTabs(new ILaunchConfigurationTab[] {
				createMainLaunchConfigTab(),
				new LangArgumentsTab(),
				new EnvironmentTab(),
				new org.eclipse.cdt.dsf.gdb.internal.ui.launching.LocalApplicationCDebuggerTab(),
				new SourceLookupTab(),
				new CommonTab(),
		});
	}
	
	@Override
	protected ILaunchConfigurationTab createMainLaunchConfigTab() {
		return new GoLaunchConfigurationTab();
	}
	
}