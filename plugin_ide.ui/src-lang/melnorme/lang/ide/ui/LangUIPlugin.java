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
package melnorme.lang.ide.ui;

import static melnorme.utilbox.core.CoreUtil.array;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.BackingStoreException;

import _org.eclipse.jdt.internal.ui.viewsupport.ProblemMarkerManager;
import melnorme.lang.ide.core.ILangOperationsListener;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCorePlugin;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.ui.templates.TemplateRegistry;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.util.swt.jface.resources.ImageDescriptorRegistry;
import melnorme.util.swt.jface.text.ColorManager2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.MiscUtil;

public abstract class LangUIPlugin extends AbstractUIPlugin {
	
	public static String PLUGIN_ID = LangUIPlugin_Actual.PLUGIN_ID;
	
	protected static LangUIPlugin pluginInstance;
	
	public static LangUIPlugin getInstance() {
		return pluginInstance;
	}
	
	public static LangUIPlugin getDefault() {
		return getInstance();
	}
	
	protected ILangOperationsListener operationsListener;
	
	public LangUIPlugin() {
	}
	
	/* -------- start/stop methods -------- */
	
	@Override
	public void start(BundleContext context) throws Exception {
		pluginInstance = this;
		LangUI.instance = new LangUI(context);
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
		// Commented out, editor is now the one that starting the debug plugin
//		startDebugPlugin();
	}
	
	// Force start of debug plugin, if present, so that UI contributions will be fully active.
	// ATM, some UI contributions that dynamically manipulate enablement and state don't work correctly
	// unless underlying plugin is started.
	public static void startDebugPlugin() {
		EclipseUtils.startOtherPlugin(LangUIPlugin_Actual.DEBUG_PLUGIN_ID);
	}
	
	/** Do final stage of plugin start: activate services, listeners, etc. */
	protected void doCustomStart_finalStage() {
		operationsListener = createOperationsConsoleListener();
		if(operationsListener != null) {
			LangCore.getToolManager().addListener(operationsListener);
		}
		
		LangCorePlugin.getInstance().initializeAfterUIStart();
		
		new InitializeAfterLoadJob(this).schedule();
	}
	
	protected abstract ILangOperationsListener createOperationsConsoleListener();
	
	@SuppressWarnings("unused")
	protected void doInitializeAfterLoad(IOperationMonitor om) throws CommonException {
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		if(operationsListener != null) {
			LangCore.getToolManager().removeListener(operationsListener);
		}
		
		doCustomStop(context);
		super.stop(context);
		LangUI.instance.dispose();
		pluginInstance = null;
	}
	
	protected abstract void doCustomStop(BundleContext context);
	
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
	
	public ColorManager2 getColorManager() {
		return LangUI.getInstance().getColorManager();
	}
	
	public ImageDescriptorRegistry getImageDescriptorRegistry() {
		return LangUI.getInstance().getImageDescriptorRegistry();
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
		return LangUI.getStandardDisplay();
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
			LangCore.logError("Error saving instance preferences: ", e);
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
	
	protected ProblemMarkerManager fProblemMarkerManager;
	
	public synchronized ProblemMarkerManager getProblemMarkerManager() {
		if(fProblemMarkerManager == null) {
			fProblemMarkerManager = new ProblemMarkerManager();
		}
		return fProblemMarkerManager;
	}
	
}