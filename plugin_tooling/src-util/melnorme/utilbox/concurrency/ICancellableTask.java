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

/**
 * A concurrent task that can be cancelled.
 */
public interface ICancellableTask extends Runnable {
	
	@Override
	abstract void run();
	
	/**
	 * Try to cancel this task.
	 * If cancelled before the task is run, invoking {@link #run()} will have no effect.
	 * If cancelled while it is running, the running thread is interrupted.
	 * Has no effect if it is called after the task has run.
	 * @return whether the task was cancelled.
	 * 
	 */
	boolean tryCancel();
	
}