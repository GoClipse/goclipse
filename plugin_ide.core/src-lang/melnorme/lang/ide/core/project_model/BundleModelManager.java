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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.core.runtime.Platform;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.CoreTaskAgent;
import melnorme.utilbox.concurrency.ITaskAgent;
import melnorme.utilbox.misc.SimpleLogger;

public abstract class BundleModelManager extends ProjectBasedModelManager {
	
	public static SimpleLogger log = new SimpleLogger(Platform.inDebugMode());
	
	/* ----------------------------------- */
	
	protected final LangBundleModel<?> model;
	
	protected final ITaskAgent modelAgent = new CoreTaskAgent(getClass().getSimpleName());
	
	protected boolean started = false;
	
	public BundleModelManager(LangBundleModel<?> model) {
		this.model = model;
	}
	
	public LangBundleModel<?> getModel() {
		return model;
	}
	
	public ITaskAgent getModelAgent() {
		return modelAgent;
	}
	
	@Override
	public void startManager() {
		log.println("==> Starting: " + getClass().getSimpleName());
		assertTrue(started == false); // start only once
		started = true;
		
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
	public void shutdownManager() {
		doShutdown();
		
		try {
			modelAgent.awaitTermination();
		} catch (InterruptedException e) {
			LangCore.logInternalError(e);
		}
	}
	
	@Override
	protected void doShutdown() {
		super.doShutdown();
		// shutdown model manager agent first, since model agent uses dub process agent
		modelAgent.shutdownNow();
	}
	
}