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
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.utilbox.misc.MiscUtil;

import org.eclipse.cdt.internal.ui.text.util.CColorManager;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.BackingStoreException;


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
		
		doCustomStart_initialStage(context);
		doCustomStart_startTwinPlugins();
		doCustomStart_finalStage();
	}
	
	/** Do initial stage of plugin start: load static resources, etc.. 
	 * This is usually initialization that does not require disposing. */
	@SuppressWarnings("unused")
	protected void doCustomStart_initialStage(BundleContext context) {
		// Load immediately and fail fast if resources not found
		MiscUtil.loadClass(LangUIPlugin_Actual.PLUGIN_IMAGES_CLASS); 
	}
	
	/** Start twined plugins after initial stage. */
	protected void doCustomStart_startTwinPlugins() {
		// Force start of debug plugin, if present, so that UI contributions will be fully active.
		// ATM, some UI contributions that dynamically manipulate enablement and state don't work correctly
		// unless underlying plugin is started.
		EclipseUtils.startOtherPlugin(LangUIPlugin_Actual.DEBUG_PLUGIN_ID);
	}
	
	/** Do final stage of plugin start: activate services, listeners, etc. */
	protected void doCustomStart_finalStage() {
		LangCore.getInstance().initializeAfterUIStart();
		
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
		log(createErrorStatus(LangUIMessages.LangPlugin_error, throwable));
	}
	
	/** Creates a status describing an error in this plugin, with given message and given throwable. */
	public static Status createErrorStatus(String message, Throwable throwable) {
		return new Status(IStatus.ERROR, PLUGIN_ID, message, throwable); 
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
	
	public static IPreferenceStore getCorePrefStore() {
		return getInstance().getCorePreferenceStore();
	}
	
	private IPreferenceStore corePreferenceStore;
	
    public IPreferenceStore getCorePreferenceStore() {
        // Create the preference store lazily.
        if (corePreferenceStore == null) {
        	corePreferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, LangCore.PLUGIN_ID);
        }
        return corePreferenceStore;
    }
    
    /* ----------------- other singletons ----------------- */
	
	protected CColorManager fColorManager = new CColorManager(true);
	
	public org.eclipse.cdt.ui.text.IColorManager getColorManager() {
		return fColorManager;
	}
    
	/* -------- JDT copied stuff -------- */
	
	public static void flushInstanceScope() {
		try {
			InstanceScope.INSTANCE.getNode(PLUGIN_ID).flush();
		} catch (BackingStoreException e) {
			log(e);
		}
	}
	
	private FormToolkit fDialogsFormToolkit;
	
	public FormToolkit getDialogsFormToolkit() {
		if (fDialogsFormToolkit == null) {
			FormColors colors= new FormColors(Display.getCurrent());
			colors.setBackground(null);
			colors.setForeground(null);
			fDialogsFormToolkit= new FormToolkit(colors);
		}
		return fDialogsFormToolkit;
	}
	
}