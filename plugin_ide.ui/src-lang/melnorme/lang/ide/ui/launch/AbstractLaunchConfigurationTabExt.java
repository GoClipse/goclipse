/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *     CDT - certain methods
 *******************************************************************************/
package melnorme.lang.ide.ui.launch;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;

import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.LangUIPlugin;

public abstract class AbstractLaunchConfigurationTabExt extends AbstractLaunchConfigurationTab {
	
	public static String getConfigAttribute(ILaunchConfiguration config, String key, String defaultValue) {
		try {
			return config.getAttribute(key, defaultValue);
		} catch (CoreException ce) {
			LangUIPlugin.logError(LangUIMessages.Launch_ErrorReadingConfigurationAttribute, ce);
		}
		return defaultValue;
	}
	
}