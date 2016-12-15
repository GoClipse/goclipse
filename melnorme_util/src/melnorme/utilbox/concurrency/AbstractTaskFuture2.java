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
package melnorme.utilbox.concurrency;

import melnorme.utilbox.core.fntypes.CallableX;

public abstract class AbstractTaskFuture2<RET> extends AbstractFuture2<RET> 
	implements ICancellable
{
	
	public AbstractTaskFuture2() {
		super();
	}
	
	/**
	 * CancellableTask is use for:
	 * - not run task if task already cancelled.
	 * - if a thread is currently running the internalTaskRun, interrupt the thread if future is cancelled.
	 */
	private final CancellableTask cancellableTask = new CancellableTask() {
		@Override
		protected void doRun() {
			AbstractTaskFuture2.this.internalTaskRun();
		}
	};
	
	@Override
	public boolean canExecute() {
		return cancellableTask.canExecute();
	}
	
	protected void runFuture() {
		cancellableTask.run();
	}
	
	protected void internalTaskRun() {
		// need to use anon-class instead of lambda, javac errors on lambda expresion 
		completableResult.setResultFromCallable(new CallableX<RET, RuntimeException>() {
			@Override
			public RET call() throws RuntimeException {
				return internalInvoke();
			}
		});
	}
	
	protected abstract RET internalInvoke();
	
	
	public void completeWithResult(RET result) {
		cancellableTask.markExecuted();
		completableResult.setResult(result);
	}
	
	/* -----------------  ----------------- */
	
	@Override
	protected void handleCancellation() {
		cancellableTask.tryCancel();
	}
	
}