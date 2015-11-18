/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.utils.operation;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;

import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.concurrency.SafeFuture;

public class OperationUtils {

	public static void checkMonitorCancelation(IProgressMonitor progressMonitor) throws OperationCancellation {
		if(progressMonitor.isCanceled()) {
			throw new OperationCancellation();
		}
	}
	
	public static void checkMonitorCancelation_OCE(IProgressMonitor progressMonitor) throws OperationCanceledException {
		if(progressMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}
	}
	
	public static <R> R awaitData(SafeFuture<R> future, IProgressMonitor pm) 
			throws OperationCancellation {
		
		while(true) {
			if(pm.isCanceled()) {
				throw new OperationCancellation();
			}
			
			try {
				return future.get(100, TimeUnit.MILLISECONDS);
			} catch(InterruptedException e) {
				throw new OperationCancellation();
			} catch(TimeoutException e) {
				continue;
			}
		}
	}
	
	public static <R> R awaitData(Future<R> future, IProgressMonitor pm) 
			throws OperationCancellation, ExecutionException {
		
		while(true) {
			if(pm.isCanceled()) {
				throw new OperationCancellation();
			}
			
			try {
				return future.get(100, TimeUnit.MILLISECONDS);
			} catch(InterruptedException e) {
				throw new OperationCancellation();
			} catch(TimeoutException e) {
				continue;
			}
		}
	}
	
}