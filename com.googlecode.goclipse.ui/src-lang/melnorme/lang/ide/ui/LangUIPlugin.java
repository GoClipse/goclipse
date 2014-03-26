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

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCore.ILangConstants;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.utilbox.misc.MiscUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.BundleContext;


public abstract class LangUIPlugin extends AbstractUIPlugin {
	
	public static String PLUGIN_ID = LangUIPlugin_Actual.PLUGIN_ID;
	
	protected static LangUIPlugin pluginInstance;
	
	public static LangUIPlugin getInstance() {
		return pluginInstance;
	}
	
	/* -------- start/stop methods -------- */
	
	@Override
	public void start(BundleContext context) throws Exception {
		pluginInstance = this;
		super.start(context);
		
		MiscUtil.loadClass(doCustomStart_getImagesClass()); // Fail fast if resources not found
		doCustomStart(context);
		
		// Force start of debug plugin, if present, so that UI contributions will be fully active.
		// ATM, some UI contributions that dynamically manipulate enablement and state don't work correctly
		// unless underlying plugin is started.
		startDebugPlugin();
		
		startInitializeAfterLoadJob();
	}
	
	protected abstract Class<?> doCustomStart_getImagesClass();
	
	protected abstract void doCustomStart(BundleContext context);
	
	protected static void startDebugPlugin() {
		EclipseUtils.startOtherPlugin(LangUIPlugin_Actual.DEBUG_PLUGIN_ID);
	}
	
	protected void startInitializeAfterLoadJob() {
		(new InitializeAfterLoadJob()).schedule();
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		doCustomStop(context);
		super.stop(context);
		pluginInstance = null;
	}
	
	protected abstract void doCustomStop(BundleContext context);
	
	/* --------  -------- */
	
	public static void initializeAfterLoad(IProgressMonitor monitor) throws CoreException {
		// nothing to do
		monitor.done();
	}
	
	/* ----------------- logging helpers ----------------- */
	
	/** Logs the given status. */
	public static void log(IStatus status) {
		getInstance().getLog().log(status);
	}
	
	/** Logs the given throwable, wrapping it in a Status. */
	public static void log(Throwable throwable) {
		log(createErrorStatus(LangUIMessages.LangPlugin_internal_error, throwable));
	}
	
	/** Creates a status describing an error in this plugin, with given message and given throwable. */
	public static Status createErrorStatus(String message, Throwable throwable) {
		return new Status(IStatus.ERROR, PLUGIN_ID, ILangConstants.INTERNAL_ERROR, message, throwable); 
	}
	
	/** Logs an error status with given message. */
	public static void logError(String message) {
		getInstance().getLog().log(createErrorStatus(message, null));
	}
	
	public static void logWarning(String message) {
		log(new Status(IStatus.WARNING, PLUGIN_ID, IStatus.ERROR, message, null));
	}
	
	public static void logWarning(Throwable throwable) {
		log(new Status(IStatus.WARNING, PLUGIN_ID, IStatus.ERROR, throwable.getMessage(), throwable));
	}
	
	public static void logWarning(String message, Throwable throwable) {
		log(new Status(IStatus.WARNING, PLUGIN_ID, IStatus.ERROR, message, throwable));
	}
	
	/* --------  -------- */
	
	/** Gets the plugins preference store. */
	public static IPreferenceStore getPrefStore() {
		return getInstance().getPreferenceStore();
	}
	
	private IPreferenceStore corePreferenceStore;
	
    public IPreferenceStore getCorePreferenceStore() {
        // Create the preference store lazily.
        if (corePreferenceStore == null) {
        	corePreferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, LangCore.PLUGIN_ID);
        }
        return corePreferenceStore;
    }
	
}