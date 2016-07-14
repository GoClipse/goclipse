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

import java.util.concurrent.FutureTask;

public abstract class AbstractRunnableFuture2<RET> extends AbstractFuture2<RET> 
	implements IRunnableFuture2<RET>	
{
	
	public AbstractRunnableFuture2() {
		super();
	}
	
	/**
	 * FutureTask is only used to wrap the internalTaskRun method, so that
	 * - internalTaskRun not run if task already cancelled.
	 * - if a thread is currently running the future, interrupt the thread if future is cancelled
	 */
	private final FutureTask<RET> futureTask = new FutureTask<RET>(this::internalTaskRun, null);
	
	@Override
	public final void run() {
		futureTask.run();
	}
	
	protected void internalTaskRun() {
		completableResult.setResult(invokeToResult());
	}
	
	protected abstract RET invokeToResult();
	
	/* -----------------  ----------------- */
	
	@Override
	protected void handleCancellation() {
		futureTask.cancel(true);
	}
	
}