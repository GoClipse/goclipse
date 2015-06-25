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
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangNature;
import melnorme.lang.ide.core.utils.CoreTaskAgent;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.tooling.IBundleInfo;
import melnorme.utilbox.concurrency.ITaskAgent;
import melnorme.utilbox.misc.SimpleLogger;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;

public abstract class BundleModelManager {
	
	public static SimpleLogger log = new SimpleLogger(Platform.inDebugMode());
	
	/* ----------------------------------- */
	
	protected final BundleManifestModelWatcher listener = new BundleManifestModelWatcher();
	protected final ITaskAgent modelAgent = new CoreTaskAgent(getClass().getSimpleName());
	
	protected boolean started = false;
	
	public BundleModelManager() {
	}
	
	public ITaskAgent getModelAgent() {
		return modelAgent;
	}
	
	public void startManager() {
		log.print("==> Starting: " + getClass().getSimpleName());
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
	
	protected void initializeModelManager() {
		try {
			EclipseUtils.getWorkspace().run(new IWorkspaceRunnable() {
				@Override
				public void run(IProgressMonitor monitor) {
					EclipseUtils.getWorkspace().addResourceChangeListener(
						listener, IResourceChangeEvent.POST_CHANGE);
					initializeProjectsInfo(monitor);
				}
			}, null);
		} catch (CoreException ce) {
			LangCore.logStatus(ce);
			// This really should not happen, but still try to recover by registering listener.
			EclipseUtils.getWorkspace().addResourceChangeListener(
				listener, IResourceChangeEvent.POST_CHANGE);
		}
	}
	
	public void shutdownManager() {
		doShutdown();
		
		try {
			modelAgent.awaitTermination();
		} catch (InterruptedException e) {
			LangCore.logInternalError(e);
		}
	}
	
	protected void doShutdown() {
		// It is possible to shutdown the manager without having it started.
		
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(listener);
		// shutdown model manager agent first, since model agent uses dub process agent
		modelAgent.shutdownNow();
	}
	
	/* -----------------  ----------------- */
	
	protected void initializeProjectsInfo(@SuppressWarnings("unused") IProgressMonitor monitor) {
		
		IProject[] projects = EclipseUtils.getWorkspaceRoot().getProjects();
		for (IProject project : projects) {
			if(isEligibleForBundleModelWatch(project) && projectHasBundleManifest(project)) {
				bundleProjectAdded(project);
			}
		}
		
	}
	
	public abstract IBundleInfo getBundleInfo(IProject project);
	
	public boolean isEligibleForBundleModelWatch(IProject project) {
		return LangNature.isAccessible(project, true);
	}
	
	public abstract boolean projectHasBundleManifest(IProject project);
	
	public abstract boolean resourceDeltaIsBundleManifestChange(IResourceDelta resourceDelta);
	
	protected class BundleManifestModelWatcher extends BundleManifestResourceListener {
		
		@Override
		public IBundleInfo getBundleInfo(IProject project) {
			return BundleModelManager.this.getBundleInfo(project);
		}
		
		@Override
		public boolean isEligibleForBundleModelWatch(IProject project) {
			return BundleModelManager.this.isEligibleForBundleModelWatch(project);
		}
		
		@Override
		public boolean projectHasBundleManifest(IProject project) {
			return BundleModelManager.this.projectHasBundleManifest(project);
		}
		
		@Override
		public boolean resourceDeltaIsBundleManifestChange(IResourceDelta resourceDelta) {
			return BundleModelManager.this.resourceDeltaIsBundleManifestChange(resourceDelta);
		}
		
		@Override
		public void bundleProjectAdded(IProject project) {
			BundleModelManager.this.bundleProjectAdded(project);
		}
		
		@Override
		public void bundleProjectRemoved(IProject project) {
			BundleModelManager.this.bundleProjectRemoved(project);
		}
		
		@Override
		public void bundleManifestChanged(IProject project) {
			BundleModelManager.this.bundleManifestFileChanged(project);
		}
		
	}
	
	protected abstract void bundleProjectAdded(IProject project);
	
	protected abstract void bundleProjectRemoved(IProject project);
	
	protected abstract void bundleManifestFileChanged(IProject project);
	
}