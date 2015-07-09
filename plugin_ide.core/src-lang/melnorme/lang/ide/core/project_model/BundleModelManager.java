/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.project_model;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.core.utils.CoreTaskAgent;
import melnorme.utilbox.concurrency.ITaskAgent;
import melnorme.utilbox.concurrency.LatchRunnable;
import melnorme.utilbox.misc.SimpleLogger;

public abstract class BundleModelManager<BUNDLE_MODEL extends LangBundleModel<? extends AbstractBundleInfo>> 
	extends ProjectBasedModelManager implements IBundleModelManager {
	
	/* ----------------------------------- */
	
	protected final BUNDLE_MODEL model;
	protected final SimpleLogger log;
	
	protected final ITaskAgent modelAgent = new CoreTaskAgent(getClass().getSimpleName());
	protected final LatchRunnable startLatch = new LatchRunnable();
	
	public BundleModelManager(BUNDLE_MODEL model) {
		this.model = assertNotNull(model);
		this.log = model.getLog();
		
		initializeModelManagerWithModelAgent();
	}
	
	public ITaskAgent getModelAgent() {
		return modelAgent;
	}
	
	protected void initializeModelManagerWithModelAgent() {
		// Put a latch runnable to prevent model from actually starting
		// This is because typically we want model to start only after UI code is fully loaded 
		modelAgent.submit(startLatch);
		
		// Run heavyweight initialization in executor thread.
		// This is necessary so that we avoid running the initialization during plugin initialization.
		// Otherwise there could be problems because initialization is heavyweight code:
		// it requests workspace locks (which may not be available) and issues workspace deltas
		modelAgent.submit(new Runnable() {
			@Override
			public void run() {
				initializeModelManager();
			}
		});
	}
	
	@Override
	public void startManager() {
		log.println("==> Starting: " + getClass().getSimpleName());
		startLatch.releaseAll();
	}
	
	@Override
	protected void doShutdown() {
		super.doShutdown();
		
		modelAgent.shutdownNow();
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public BUNDLE_MODEL getModel() {
		return model;
	}
	
	@Override
	public AbstractBundleInfo getProjectInfo(IProject project) {
		return model.getProjectInfo(project);
	}
	
}