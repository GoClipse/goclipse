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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

import melnorme.lang.ide.core.LangCore;
import melnorme.utilbox.core.CommonException;

public class LaunchUtils {
	
	public static String[] getEvaluatedArguments(String extraOptionsString) throws CommonException {
		try {
			IStringVariableManager varMgr = VariablesPlugin.getDefault().getStringVariableManager();
			extraOptionsString = varMgr.performStringSubstitution(extraOptionsString, true);
		} catch(CoreException ce) {
			throw LangCore.createCommonException(ce);
		}
		return DebugPlugin.parseArguments(extraOptionsString);
	}
	
	public static String getOptionalAttribute(ILaunchConfiguration config, String keyIsDefault, String key) 
			throws CoreException {
		boolean isDefault = config.getAttribute(keyIsDefault, true);
		return isDefault ? null: config.getAttribute(key, "");
	}
	
	public static void setOptionalValue(ILaunchConfigurationWorkingCopy config, String keyIsDefault, String key, 
			String value) {
		config.setAttribute(keyIsDefault, value == null);
		config.setAttribute(key, value);
	}
	
}
