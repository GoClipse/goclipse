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
package melnorme.lang.ide.core;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.utilbox.misc.ILogHandler;
import melnorme.utilbox.status.StatusException;

public abstract class LangCorePlugin extends Plugin {
	
	public static final String PLUGIN_ID = LangCore_Actual.PLUGIN_ID;
	public static final String TESTS_PLUGIN_ID = PLUGIN_ID + ".tests";
	
	public static final String NATURE_ID = LangCore_Actual.NATURE_ID;
	
	protected static LangCorePlugin pluginInstance;
	
	/** Returns the singleton for this plugin instance. */
	public static LangCorePlugin getInstance() {
		return pluginInstance;
	}
	
	public static final ILogHandler LOG_HANDLER = new ILogHandler() {
		@Override
		public void logStatus(StatusException se) {
			int severity = EclipseUtils.toEclipseSeverity(se);
			ILog log = LangCorePlugin.getInstance().getLog();
			log.log(EclipseCore.createStatus(severity, se.getMessage(), se.getCause()));
		}
	};
	
	protected LangCore langCore;
	
	/* -----------------  ----------------- */
	
	protected boolean initializedAfterUI = false;
	
	@Override
	public final void start(BundleContext context) throws Exception {
		pluginInstance = this;
		langCore = new LangCore(LOG_HANDLER);
		super.start(context);
		doCustomStart(context);
	}
	
	protected abstract void doCustomStart(BundleContext context);
	
	/** 
	 * Initialize services that should only be started after the UI plugin 
	 * (or other application plugin such as test runner) has started.
	 * This is because the UI plugin might register listeners into core services, 
	 * and this ensures that the UI plugin gets all updates, because they will only start after this. 
	 */
	public final void initializeAfterUIStart() {
		if(initializedAfterUI == true) {
			LangCore.logWarning("Atempted initializeAfterUIStart more than once.");
		} else {
			initializedAfterUI = true;
			
			langCore.startAgentsAfterUIStart();
		}
	}
	
	@Override
	public final void stop(BundleContext context) throws Exception {
		doCustomStop(context);
		
		langCore.shutdown();
		
		super.stop(context);
		pluginInstance = null;
	}
	
	protected abstract void doCustomStop(BundleContext context);
	
	
}