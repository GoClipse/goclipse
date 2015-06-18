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
import melnorme.lang.ide.core.LangCore;
import melnorme.util.swt.jface.AbstractTreeContentProvider;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonContentProvider;

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
	
}