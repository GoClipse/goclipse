/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.utils.operation;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.function.Function;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.tooling.common.ops.BiDelegatingOperationMonitor;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.common.ops.IOperationMonitor.IOperationSubMonitor;
import melnorme.lang.utils.concurrency.JobFuture;
import melnorme.utilbox.concurrency.MonitorRunnableFuture;
import melnorme.utilbox.concurrency.OperationCancellation;

public class EclipseJobFuture<RET> extends MonitorRunnableFuture<RET> implements JobFuture<RET> {
	
	protected final String operationName;
	protected final Function<IOperationMonitor, RET> resultFunction;
	protected final Job job;
	
	protected boolean scheduled = false;
	
	public EclipseJobFuture(String operationName, Function<IOperationMonitor, RET> resultFunction, boolean schedule) {
		super();
		this.operationName = assertNotNull(operationName);
		this.resultFunction = assertNotNull(resultFunction);
		
		this.job = init_createJob();
		
		if(schedule) {
			this.start();
		}
	}
	
	@Override
	public void start() {
		if(!scheduled) {
			// Can only schedule once
			scheduled = true;
			job.schedule();
		}
	}
	
	@Override
	public boolean isStarted() {
		return scheduled;
	}
	
	/* -----------------  ----------------- */
	
	protected volatile BiDelegatingOperationMonitor biMonitor;
	
	protected Job init_createJob() {
		return new Job(operationName) {
			
			@Override
			protected IStatus run(IProgressMonitor pm) {
				IOperationMonitor parentOM = EclipseUtils.om(pm);
				
				try(IOperationSubMonitor subMonitor = parentOM.enterSubTask(operationName)) {
					
					try {
						doFutureRun(subMonitor);
					} catch(OperationCancellation e) {
						return Status.CANCEL_STATUS;
					}
					return Status.OK_STATUS;
				}
			}
			
			protected void doFutureRun(IOperationMonitor om) throws OperationCancellation {
				assertTrue(biMonitor == null);
				biMonitor = new BiDelegatingOperationMonitor(om, getCancelMonitor());
				
				EclipseJobFuture.this.runFuture();
				assertTrue(completableResult.isTerminated());
			}
		};
	}
	
	@Override
	public void run() {
		throw assertFail();
	}
	
	@Override
	protected void runFuture() {
		super.runFuture();
	}
	
	@Override
	protected RET internalInvoke() {
		return resultFunction.apply(biMonitor);
	}
	
	@Override
	protected void handleCancellation() {
		super.handleCancellation();
		job.cancel();
	}
	
}