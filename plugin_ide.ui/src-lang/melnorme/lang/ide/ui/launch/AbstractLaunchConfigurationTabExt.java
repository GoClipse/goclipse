/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
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
import melnorme.lang.tooling.data.StatusException;
import melnorme.utilbox.core.CommonException;

public abstract class AbstractLaunchConfigurationTabExt extends AbstractLaunchConfigurationTab {
	
	public static String getConfigAttribute(ILaunchConfiguration config, String key, String defaultValue) {
		try {
			return config.getAttribute(key, defaultValue);
		} catch (CoreException ce) {
			LangUIPlugin.logError(LangUIMessages.Launch_ErrorReadingConfigurationAttribute, ce);
		}
		return defaultValue;
	}
	
	public static boolean getConfigAttribute(ILaunchConfiguration config, String key, boolean defaultValue) {
		try {
			return config.getAttribute(key, defaultValue);
		} catch (CoreException ce) {
			LangUIPlugin.logError(LangUIMessages.Launch_ErrorReadingConfigurationAttribute, ce);
		}
		return defaultValue;
	}
	
	/* ---------- validation ---------- */
	
	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		setErrorMessage(null);
		setMessage(null);
		
		try {
			doValidate();
		} catch(StatusException se) {
			String message = se.getMessage();
			
			switch (se.getStatusLevel()) {
			case ERROR: setErrorMessage(message); break;
			case WARNING: setWarningMessage(message); break;
			case INFO: 
			case OK: setMessage(message);break;
			}
		} catch(CommonException e) {
			setErrorMessage(e.getMessage());
		} catch(CoreException e) {
			setErrorMessage(e.getMessage());
		}
		
		return getErrorMessage() == null;
	}
	
	protected abstract void doValidate() throws StatusException, CommonException, CoreException;
	
}