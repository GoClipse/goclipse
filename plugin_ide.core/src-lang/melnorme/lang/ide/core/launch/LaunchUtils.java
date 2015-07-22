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

public class LaunchUtils {
	
	public static String[] getParsedArguments(String extraOptionsString) throws CoreException {
		IStringVariableManager varMgr = VariablesPlugin.getDefault().getStringVariableManager();
		extraOptionsString = varMgr.performStringSubstitution(extraOptionsString, true);
		return DebugPlugin.parseArguments(extraOptionsString);
	}
	
}
