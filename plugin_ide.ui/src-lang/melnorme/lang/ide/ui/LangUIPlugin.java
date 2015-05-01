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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.array;
import melnorme.lang.ide.core.ILangOperationsListener_Actual;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCore.StatusExt;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.ui.templates.TemplateRegistry;
import melnorme.util.swt.jface.resources.ImageDescriptorRegistry;
import melnorme.utilbox.misc.MiscUtil;

import org.eclipse.cdt.internal.ui.text.util.CColorManager;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.BackingStoreException;


public abstract class LangUIPlugin extends AbstractUIPlugin {
	
	public static String PLUGIN_ID = LangUIPlugin_Actual.PLUGIN_ID;
	
	protected static LangUIPlugin pluginInstance;
	
	public static LangUIPlugin getInstance() {
		return pluginInstance;
	}
	
	public static LangUIPlugin getDefault() {
		return getInstance();
	}
	
	protected ILangOperationsListener_Actual operationsListener;
	
	public LangUIPlugin() {
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
		operationsListener = createOperationsConsoleListener();
		if(operationsListener != null) {
			LangCore.getToolManager().addListener(operationsListener);
		}
		
		LangCore.getInstance().initializeAfterUIStart();
		
		new InitializeAfterLoadJob(this).schedule();
	}
	
	protected abstract ILangOperationsListener_Actual createOperationsConsoleListener();
	
	@SuppressWarnings("unused")
	protected void doInitializeAfterLoad(IProgressMonitor monitor) throws CoreException {
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		if(operationsListener != null) {
			LangCore.getToolManager().removeListener(operationsListener);
		}
		
		doCustomStop(context);
		super.stop(context);
		pluginInstance = null;
	}
	
	protected abstract void doCustomStop(BundleContext context);
	
	/* ######################################################################## */
	
	/* ----------------- logging helpers ----------------- */
	
	/** Creates an OK status with given message. */
	public static Status createOkStatus(String message) {
		return createStatus(IStatus.OK, message, null);
	}
	
	/** Creates a status describing an error in this plugin, with given message. */
	public static IStatus createErrorStatus(String message) {
		return createErrorStatus(message, null);
	}
	
	/** Creates a status describing an error in this plugin, with given message and given throwable. */
	public static StatusExt createErrorStatus(String message, Throwable throwable) {
		return createStatus(IStatus.ERROR, message, throwable);
	}
	
	/** Creates a Status with given status code and message. */
	public static StatusExt createStatus(int statusCode, String message, Throwable throwable) {
		return new StatusExt(statusCode, LangCore.getInstance(), message, throwable);
	}
	
	/** Creates a CoreException describing an error in this plugin. */
	public static CoreException createCoreException(String message, Throwable throwable) {
		return new CoreException(createErrorStatus(message, throwable));
	}
	
	/* ----------------- Logging ----------------- */
	
	/** Logs given status. */
	public static void logStatus(IStatus status) {
		getInstance().getLog().log(status);
	}
	
	/** Logs status of given CoreException. */
	public static void logStatus(CoreException ce) {
		getInstance().getLog().log(ce.getStatus());
	}
	
	/** Logs an error status with given message. */
	public static void logError(String message) {
		getInstance().getLog().log(createErrorStatus(message, null));
	}
	
	/** Logs an error status with given message and given throwable. */
	public static void logError(String message, Throwable throwable) {
		getInstance().getLog().log(createErrorStatus(message, throwable));
	}
	
	public static void logInternalError(Throwable throwable) {
		logError("Internal Error!", throwable);
	}
	
	/* -------- Services and other singletons -------- */
	
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
	
	protected ImageDescriptorRegistry fImageDescriptorRegistry;
	
	public ImageDescriptorRegistry getImageDescriptorRegistry() {
		assertTrue(getStandardDisplay() != null);
		if (fImageDescriptorRegistry == null) {
			fImageDescriptorRegistry = new melnorme.util.swt.jface.resources.ImageDescriptorRegistry();
		}
		return fImageDescriptorRegistry;
	}
	
	protected static TemplateRegistry instance;
	
	public static TemplateRegistry getTemplateRegistry() {
		if(instance == null) {
			instance = new TemplateRegistry();
		}
		return instance;
	}
	
	public TemplateStore getTemplateStore() {
		return getTemplateRegistry().getTemplateStore();
	}
	
	public ContextTypeRegistry getTemplateContextTypeRegistry() {
		return getTemplateRegistry().getContextTypeRegistry();
	}
	
	/* -------- JDT/DLTK copied stuff -------- */
	
	public static Display getStandardDisplay() {
		return PlatformUI.getWorkbench().getDisplay();
	}
	
	public static IDialogSettings getDialogSettings(String sectionName) {
		IDialogSettings settings = getInstance().getDialogSettings().getSection(sectionName);
		if (settings == null) {
			settings = getInstance().getDialogSettings().addNewSection(sectionName);
		}
		return settings;
	}
	
	public static void flushInstanceScope() {
		try {
			InstanceScope.INSTANCE.getNode(PLUGIN_ID).flush();
		} catch (BackingStoreException e) {
			logError("Error saving instance preferences: ", e);
		}
	}
    
	private IPreferenceStore fCombinedPreferenceStore;
	
	public IPreferenceStore getCombinedPreferenceStore() {
		if(fCombinedPreferenceStore == null) {
			IPreferenceStore generalTextStore = EditorsUI.getPreferenceStore();
			fCombinedPreferenceStore = new ChainedPreferenceStore(array(
					getPreferenceStore(),
					getCorePreferenceStore(),
					generalTextStore
			));
		}
		return fCombinedPreferenceStore;
	}
	
}