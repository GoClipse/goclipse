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
package melnorme.lang.ide.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;


public abstract class LangUIPlugin extends AbstractUIPlugin {

	public static String PLUGIN_ID = LangUIPlugin_Actual.PLUGIN_ID;
	
	public static LangUIPlugin getInstance() {
		return LangUIPlugin_Actual.__getInstance();
	}
	
	/** Logs the given status. */
	public static void log(IStatus status) {
		getInstance().getLog().log(status);
	}
	
	/** Logs the given Throwable, wrapping it in a Status. */
	public static void log(Throwable e) {
		log(new Status(IStatus.ERROR, PLUGIN_ID, ILangStatusConstants.INTERNAL_ERROR,
				LangUIMessages.LangPlugin_internal_error, e)); 
	}
	
	/** Gets the plugins preference store. */
	public static IPreferenceStore getPrefStore() {
		return getInstance().getPreferenceStore();
	}
	
	protected static boolean initialized; 
	
	protected void startInitializeAfterLoadJob() {
		(new InitializeAfterLoadJob()).schedule();
	}
	
	public static void initializeAfterLoad(IProgressMonitor monitor) throws CoreException {
		// nothing to do
		monitor.done();
	}
	
}