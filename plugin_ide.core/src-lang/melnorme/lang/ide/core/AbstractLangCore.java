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

import melnorme.lang.ide.core.engine.SourceModelManager;
import melnorme.lang.ide.core.operations.ToolManager;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.project_model.BundleModelManager;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.utilbox.misc.ILogHandler;

public abstract class AbstractLangCore {
	
	public static LangCore instance;
	
	public static LangCore get() {
		return instance;
	}
	
	/* ----------------- Owned singletons: ----------------- */
	protected final ILogHandler logHandler;
	protected final CoreSettings coreSettings;
	protected final ToolManager toolManager;
	protected final BundleModelManager<? extends LangBundleModel> bundleManager;
	protected final BuildManager buildManager;
	protected final SourceModelManager sourceModelManager;
	
	public AbstractLangCore(ILogHandler logHandler) {
		instance = (LangCore) this;
		
		this.logHandler = logHandler;
		
		coreSettings = createCoreSettings();
		toolManager = createToolManager();
		bundleManager = LangCore_Actual.createBundleModelManager();
		buildManager = createBuildManager();
		sourceModelManager = LangCore_Actual.createSourceModelManager();
	}
	
	protected void shutdown() {
		buildManager.dispose();
		bundleManager.shutdownManager();
		sourceModelManager.dispose();
		toolManager.shutdownNow();
	}
	
	/* -----------------  ----------------- */ 
	
	public static ILogHandler logHandler() {
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
	
	/* -----------------  ----------------- */
	
	/** Start core agents, and do other initizaliation after UI is started. */
	public void startAgentsAfterUIStart() {
		bundleManager.startManager();
	}
	
}