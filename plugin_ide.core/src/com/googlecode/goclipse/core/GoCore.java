/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.core;


import org.osgi.framework.BundleContext;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.ToolchainPreferences;

public class GoCore extends LangCore {
	
	@Override
	protected void doCustomStart(BundleContext context) {
		ToolchainPreferences.DAEMON_PATH.setPreferencesDefaultValue("gocode");
	}
	
	@Override
	protected void doCustomStop(BundleContext context) {
	}
	
	public static GoCore getDefault() {
		return (GoCore) getInstance();
	}
	
}