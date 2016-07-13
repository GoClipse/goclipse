/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import melnorme.lang.ide.core.engine.ILanguageServerHandler;
import melnorme.lang.ide.core.engine.SourceModelManager;
import melnorme.lang.ide.core.operations.ToolManager;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.project_model.BundleModelManager;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.utilbox.misc.ILogHandler;
import melnorme.utilbox.status.StatusException;

public abstract class AbstractLangCore extends LoggingCore {
	
	public static LangCore instance;
	
	public static LangCore get() {
		return instance;
	}
	
	/* ----------------- Owned singletons: ----------------- */
	protected final ILogHandler logHandler;
	protected final CoreSettings coreSettings;
	protected final ToolManager toolManager;
	protected final ILanguageServerHandler languageServerHandler;
	protected final BundleModelManager<? extends LangBundleModel> bundleManager;
	protected final BuildManager buildManager;
	protected final SourceModelManager sourceModelManager;
	
	public AbstractLangCore(ILogHandler logHandler) {
		instance = (LangCore) this;
		
		this.logHandler = logHandler;
		
		coreSettings = assertNotNull(createCoreSettings());
		toolManager = assertNotNull(createToolManager());
		languageServerHandler = assertNotNull(createLanguageServerHandler());
		bundleManager = assertNotNull(LangCore_Actual.createBundleModelManager());
		buildManager = assertNotNull(createBuildManager());
		sourceModelManager = assertNotNull(LangCore_Actual.createSourceModelManager());
	}
	
	protected void shutdown() {
		buildManager.dispose();
		bundleManager.shutdownManager();
		sourceModelManager.dispose();
		languageServerHandler.dispose();
		toolManager.shutdownNow();
	}
	
	/* -----------------  ----------------- */ 
	
	public static ILogHandler log() {
		return instance.logHandler;
	}
	
	protected abstract CoreSettings createCoreSettings();
	
	public static CoreSettings settings() {
		return instance.coreSettings;
	}
	
	protected abstract ToolManager createToolManager();

	public static ToolManager getToolManager() {
		return instance.toolManager;
	}
	
	public static BundleModelManager<? extends LangBundleModel> getBundleModelManager() {
		return instance.bundleManager;
	}
	public static LangBundleModel getBundleModel() {
		return getBundleModelManager().getModel();
	}
	
	protected abstract BuildManager createBuildManager();
	
	public static BuildManager getBuildManager() {
		return instance.buildManager;
	}
	
	public static SourceModelManager getSourceModelManager() {
		return instance.sourceModelManager;
	}
	
	public abstract ILanguageServerHandler createLanguageServerHandler();
	
	public static ILanguageServerHandler getLanguageServerHandler() {
		return instance.languageServerHandler;
	}
	
	/* -----------------  ----------------- */
	
	/** Start core agents, and do other initizaliation after UI is started. */
	public void startAgentsAfterUIStart() {
		bundleManager.startManager();
	}
	
}

class LoggingCore {
	
	public static ILogHandler log() {
		return AbstractLangCore.instance.logHandler;
	}
	
	/** Logs status of given StatusException. */
	public static void logStatusException(StatusException se) {
		log().logStatus(se);
	}
	
	/** Logs an error status with given message. */
	public static void logError(String message) {
		log().logError(message);
	}
	/** Logs an error status with given message and given throwable. */
	public static void logError(String message, Throwable throwable) {
		log().logError(message, throwable);
	}
	
	/** Logs a warning status with given message. */
	public static void logWarning(String message) {
		log().logWarning(message);
	}
	/** Logs a warning status with given message and given throwable. */
	public static void logWarning(String message, Throwable throwable) {
		log().logWarning(message, throwable);
	}
	
	/** Logs an info status with given message. */
	public static void logInfo(String message) {
		log().logInfo(message);
	}
	
	public static void logInternalError(Throwable throwable) {
		log().logError("Internal Error!", throwable);
	}

}