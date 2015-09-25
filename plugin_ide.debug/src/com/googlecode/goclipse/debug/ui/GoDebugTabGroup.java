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

import org.eclipse.debug.ui.ILaunchConfigurationTab;

import com.googlecode.goclipse.ui.launch.GoMainLaunchConfigurationTab;

import melnorme.lang.ide.debug.ui.AbstractLangDebugTabGroup;

public class GoDebugTabGroup extends AbstractLangDebugTabGroup {
	
	@Override
	protected ILaunchConfigurationTab createMainLaunchConfigTab() {
		return new GoMainLaunchConfigurationTab();
	}
	
}