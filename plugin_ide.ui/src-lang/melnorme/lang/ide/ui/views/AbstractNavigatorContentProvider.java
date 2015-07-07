/*******************************************************************************
 * Copyright (c) 2014, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.views;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonContentProvider;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.build.BuildManager.BuildModel;
import melnorme.lang.ide.core.project_model.IProjectModelListener;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.core.project_model.UpdateEvent;
import melnorme.lang.ide.ui.navigator.BuildTargetElement;
import melnorme.lang.ide.ui.navigator.BuildTargetsContainer;
import melnorme.lang.ide.ui.navigator.NavigatorElementsSwitcher;
import melnorme.util.swt.SWTUtil;
import melnorme.util.swt.jface.AbstractTreeContentProvider;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.misc.CollectionUtil;
import melnorme.utilbox.ownership.IDisposable;

public abstract class AbstractNavigatorContentProvider extends AbstractTreeContentProvider 
	implements ICommonContentProvider {
	
	public AbstractNavigatorContentProvider() {
		super();
	}
	
	@Override
	public void saveState(IMemento aMemento) {
	}
	
	@Override
	public void restoreState(IMemento aMemento) {
	}
	
	@Override
	public void init(ICommonContentExtensionSite aConfig) {
	}
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		assertTrue(viewer instanceof StructuredViewer);
		super.inputChanged(viewer, oldInput, newInput);
	}
	
	protected StructuredViewer getViewer() {
		return (StructuredViewer) viewer;
	}
	
	protected BuildModel getBuildModel() {
		return LangCore.getBuildManager().getBuildModel();
	}
	
	@Override
	protected void viewerInitialized() {
		super.viewerInitialized();
		
		getBuildModel().addListener(listener);
	}
	
	@Override
	public void dispose() {
		getBuildModel().removeListener(listener);
		
		super.dispose();
	}
	
	
	/* -----------------  ----------------- */
	
	
	@Override
	public boolean hasChildren(Object element) {
		return hasChildren_switcher().switchElement(element);
	}
	
	protected abstract LangNavigatorSwitcher_HasChildren hasChildren_switcher();
	
	protected static interface LangNavigatorSwitcher_HasChildren extends NavigatorElementsSwitcher<Boolean> {
		@Override
		default Boolean visitProject(IProject project) {
			return project.isAccessible();
		}
		@Override
		default Boolean visitBuildTargetsElement(BuildTargetsContainer buildTargetsElement) {
			return true;
		}
		@Override
		default Boolean visitBuildTarget(BuildTargetElement buildTarget) {
			return false;
		}
		@Override
		default Boolean visitOther(Object element) {
			return false;
		}
	}
	
	@Override
	public Object[] getChildren(Object parent) {
		return getChildren_switcher().switchElement(parent);
	}
	
	protected abstract LangNavigatorSwitcher_GetChildren getChildren_switcher();
	
	public abstract class LangNavigatorSwitcher_GetChildren implements NavigatorElementsSwitcher<Object[]> {
		@Override
		public Object[] visitProject(IProject project) {
			return getProjectChildren(project);
		}
		@Override
		public Object[] visitBuildTargetsElement(BuildTargetsContainer buildTargetsElement) {
			return buildTargetsElement.getChildren_toArray();
		}
		
		@Override
		public Object[] visitBuildTarget(BuildTargetElement buildTarget) {
			return null;
		}
		@Override
		public Object[] visitOther(Object element) {
			return null;
		}
		
		public Object[] getProjectChildren(IProject project) {
			ArrayList<Object> projectChildren = new ArrayList<>();
			if(project.isAccessible()) {
				addFirstProjectChildren(project, projectChildren);
				addBuildTargetsContainer(project, projectChildren);
				addProjectResourceChildren(project, projectChildren);
			}
			return projectChildren.toArray();
		}
		
		protected void addProjectResourceChildren(IProject project, ArrayList<Object> projectChildren) {
			// Add project children ourselves: this is so that children will be sorted by our own sorter. 
			// (otherwise only Platform Navigator sorter will be used)
			// Navigator ResourceExtension will also add this, but they will not appear duplicated because they
			// are equal elements.
			try {
				projectChildren.addAll(CollectionUtil.createArrayList(project.members()));
			} catch (CoreException e) {
				// ignore, leave empty
			}
		}
		
		@SuppressWarnings("unused")
		public void addFirstProjectChildren(IProject project, ArrayList<Object> projectChildren) { 
		}
		
	}
	
	protected void addBuildTargetsContainer(IProject project, ArrayList<Object> projectChildren) {
		ProjectBuildInfo targets = LangCore.getBuildManager().getBuildInfo(project);
		if(targets != null) {
			projectChildren.add(new BuildTargetsContainer(targets));
		}
	}
	
	@Override
	public Object getParent(Object element) {
		return getParent_switcher().switchElement(element);
	}
	
	protected abstract LangNavigatorSwitcher_GetParent getParent_switcher();
	
	public static interface LangNavigatorSwitcher_GetParent extends NavigatorElementsSwitcher<Object> {
		@Override
		default Object visitProject(IProject project) {
			return project.getParent();
		}
		@Override
		default Object visitBuildTargetsElement(BuildTargetsContainer buildTargetsElement) {
			return buildTargetsElement.buildInfo.getProject();
		}
		@Override
		default Object visitBuildTarget(BuildTargetElement buildTarget) {
			return buildTarget.getParent();
		}
		@Override
		default Object visitOther(Object element) {
			return null;
		}
	}
	
	/* -----------------  ----------------- */
	
	protected final BuildModelListener listener = new BuildModelListener();
	
	protected class BuildModelListener implements IProjectModelListener<ProjectBuildInfo> {
		
		@Override
		public void notifyUpdateEvent(UpdateEvent<ProjectBuildInfo> updateEvent) {
			Display.getDefault().asyncExec(
				() -> getViewer().refresh(updateEvent.project));
		}
		
	}
	
	/* -----------------  ----------------- */
	
	// useful mostly to workaround bug: https://bugs.eclipse.org/bugs/show_bug.cgi?id=430005
	/**
	 * Helper to throttle some code, that is, to prevent some recorring code to run too soon after each other 
	 * within a given a time interval.
	 */
	public abstract class ThrottleCodeJob extends Job {
		
		protected final int throttleDelayMillis;
		protected long lastRequestMillis;
		protected boolean isScheduled = false;
		
		public ThrottleCodeJob(int throttleDelayMillis) {
			super("throttle job");
			this.throttleDelayMillis = throttleDelayMillis;
			setSystem(true);
		}
		
		/** Schedule this job to run again. Will run throttled code immediatly if past time delay, 
		 * schedule otherwise.
		 * Multiple schedule requests within the delay period will be squashed into just one request. 
		 */
		public void scheduleRefreshJob() {
			
			synchronized (this) {
				if(isScheduled) {
					return;
				}
				assertTrue(getState() == Job.NONE || getState() == Job.RUNNING);
				
				isScheduled = true;
				long runningTimeMillis = getRunningTimeMillis();
				long nextPeriod = lastRequestMillis + throttleDelayMillis;
				long deltaToNext = nextPeriod - runningTimeMillis;
				if(deltaToNext > 0) {
					//System.out.println(" schedule delta to next:" + deltaToNext);
					schedule(deltaToNext);
					return;
				} else {
					// continue and run immediately
				}
			}
			
			runThrottledCode();
		}

		protected long getRunningTimeMillis() {
			return System.nanoTime() / 1000_000;
		}
		
		@Override
		protected final IStatus run(IProgressMonitor monitor) {
			//System.out.println(getRunningTimeMillis() + " :job#run");
			runThrottledCode();
			return LangCore.createOkStatus("ok");
		}
		
		public void markRequestFinished() {
			synchronized (this) {
				isScheduled = false;
				lastRequestMillis = getRunningTimeMillis();
			}
			//System.out.println(lastRequestMillis + " lastRequestFinished");
		}
		
		protected abstract void runThrottledCode();
	}
	
	protected class NavigatorModelListener implements IDisposable {
		
		@Override
		public void dispose() {
		}
		
		// we use throttle Job as a workaround to to ensure label is updated, due to bug:
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=430005
		protected final ThrottleCodeJob viewerRefreshThrottleJob = new ThrottleCodeJob(1200) {
			@Override
			protected void runThrottledCode() {
				postRefreshEventToUI(this, getElementsToRefresh());
			}
		};
		
		protected Indexable<Object> getElementsToRefresh() {
			return new ArrayList2<>();
		};
		
		protected void postRefreshEventToUI(ThrottleCodeJob throttleCodeJob, Indexable<Object> elementsToRefresh) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					throttleCodeJob.markRequestFinished();
					
					if(SWTUtil.isOkToUse(getViewer().getControl())) {
						for (Object element : elementsToRefresh) {
							getViewer().refresh(element);
						}
					}
				}
			});
			
		}
		
	}
	
}