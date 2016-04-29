/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.engine;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.core.runtime.IProgressMonitor;

import melnorme.lang.ide.core.engine.SourceModelManager.StructureInfo;
import melnorme.lang.ide.core.utils.CoreExecutors;
import melnorme.utilbox.concurrency.ExecutorTaskAgent;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public abstract class EngineOperation<RET> {
	
	protected final SourceModelManager sourceModelMgr;
	
	protected final Location location;
	protected final int offset;
	protected final int timeoutMillis;
	protected final String opName;
	
	public EngineOperation(SourceModelManager sourceModelMgr, Location location, int offset, int timeoutMillis, 
			String opName) {
		this.sourceModelMgr = sourceModelMgr;
		
		this.location = location;
		this.offset = offset;
		this.timeoutMillis = timeoutMillis;
		this.opName = assertNotNull(opName);
	}
	
	public RET runEngineOperation(final IProgressMonitor pm) 
			throws CommonException, OperationCancellation {
		
		if(timeoutMillis <= 0 ) {
			// Run directly
			return doRunEngineOperation(pm);
		}
		
		// Use a one-time executor
		ExecutorTaskAgent completionExecutor = CoreExecutors.newExecutorTaskAgent(opName + " - Task Executor");
		try {
			return runEngineOperationWithExecutor(pm, completionExecutor);
		} finally {
			completionExecutor.shutdownNow();
		}
	}
	
	protected RET runEngineOperationWithExecutor(final IProgressMonitor pm, ExecutorTaskAgent completionExecutor)
			throws CommonException, OperationCancellation {
		Future<RET> future = completionExecutor.submit(new Callable<RET>() {
			@Override
			public RET call() throws CommonException, OperationCancellation {
				return doRunEngineOperation(pm);
			}
		});
		
		try {
			return future.get(timeoutMillis, TimeUnit.MILLISECONDS);
		} catch(ExecutionException e) {
			if(e.getCause() instanceof OperationCancellation) {
				throw (OperationCancellation) e.getCause(); 
			}
			if(e.getCause() instanceof CommonException) {
				throw (CommonException) e.getCause(); 
			}
			
			throw new CommonException("Error performing " + opName + ".", e.getCause());
		} catch (TimeoutException e) {
			throw new CommonException("Timeout performing " + opName + ".", null);
		} catch (InterruptedException e) {
			throw new CommonException("Interrupted.", e);
		}
	}
	
	protected RET doRunEngineOperation(final IProgressMonitor pm) 
			throws CommonException, OperationCancellation {
		StructureInfo structureInfo = sourceModelMgr.getStoredStructureInfo(new LocationKey(location));
		if(structureInfo != null) {
			structureInfo.awaitUpdatedData(pm);
		}
		
		return doRunOperationWithWorkingCopy(pm);
	}
	
	protected abstract RET doRunOperationWithWorkingCopy(IProgressMonitor pm) 
			throws CommonException, OperationCancellation;
	
}