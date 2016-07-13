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
package melnorme.lang.ide.core.engine;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import java.nio.file.Path;

import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.operations.ToolManager;
import melnorme.lang.ide.core.operations.ToolchainPreferences;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.common.ops.JobExecutor;
import melnorme.lang.utils.validators.LocationOrSinglePathValidator;
import melnorme.lang.utils.validators.PathValidator;
import melnorme.utilbox.concurrency.AbstractFuture2.CompletedFuture;
import melnorme.utilbox.concurrency.AutoUnlockable;
import melnorme.utilbox.concurrency.Future2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.concurrency.ReentrantLockExt;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.OperationResult;

public abstract class LanguageServerHandler<LS_INSTANCE extends LanguageServerInstance> 
	implements ILanguageServerHandler 
{
	
	protected final ToolManager toolMgr;
	protected final JobExecutor jobExecutor;
	
	protected final PathValidator languageToolPathValidator = 
			init_ServerToolPathValidator();
	
	private ReentrantLockExt serverInstanceFutureLock = new ReentrantLockExt();
	private volatile Future2<OperationResult<LS_INSTANCE>> serverInstanceFuture; // Non-null
	
	public LanguageServerHandler(JobExecutor jobExecutor, ToolManager toolMgr) {
		super();
		this.jobExecutor = assertNotNull(jobExecutor);
		this.toolMgr = assertNotNull(toolMgr);
		
		this.serverInstanceFuture = new CompletedFuture<>(
				OperationResult.fromException(new CommonException("Not initialized."))); 
	}
	
	protected PathValidator init_ServerToolPathValidator() {
		return new LocationOrSinglePathValidator(LangCore_Actual.LANGUAGE_SERVER_Name + ":");
	}
	
	@Override
	public void dispose() {
		stopServerInstance();
	}
	
	protected String getLanguageServerName() {
		return LangCore_Actual.LANGUAGE_SERVER_Name;
	}
	
	public Path getServerPath() throws CommonException {
		return languageToolPathValidator.getValidatedPath(ToolchainPreferences.LANGUAGE_SERVER_PATH.get());
	}
	
	public PathValidator getLanguageToolPathValidator() {
		return languageToolPathValidator;
	}
	
	/* -----------------  ----------------- */
	
	public void stopServerInstance() {
		serverInstanceFutureLock.runUnderLock(this::locked_stopServerInstance);
	}
	
	private void locked_stopServerInstance() {
		try {
			OperationResult<LS_INSTANCE> opResult = serverInstanceFuture.cancelOrGetResult();
			
			if(opResult.isSuccessful()) {
				opResult.getSuccessful().stop();
			}
		} catch(OperationCancellation e) {
			return;
		}
	}
	
	public void warmup() {
		restartServerInstance();
	}
	
	public void restartServerInstance() {
		serverInstanceFutureLock.runUnderLock(this::locked_restartServerInstance);
	}
	
	private void locked_restartServerInstance() {
		stopServerInstance();
		
		this.serverInstanceFuture = jobExecutor.startResultOp(
			"Starting " + getLanguageServerName(), true, this::createServerInstance);
	}
	
	protected final LS_INSTANCE createServerInstance(IOperationMonitor om) throws CommonException, OperationCancellation {
		return assertNotNull(doCreateServerInstance(om));
	}
	
	protected abstract LS_INSTANCE doCreateServerInstance(IOperationMonitor om)
			throws CommonException, OperationCancellation;
	
	
	public LS_INSTANCE getReadyServerInstance() throws CommonException, OperationCancellation {
		Path currentServerPath = getServerPath();
		
		Future2<OperationResult<LS_INSTANCE>> updatedServerInstanceFuture;
		
		try(AutoUnlockable lock_ = serverInstanceFutureLock.lock_()) {
			
			OperationResult<LS_INSTANCE> serverInstanceResult = serverInstanceFuture.awaitResult2();
			
			if(
				serverInstanceResult.isException() || 
				!areEqual(serverInstanceResult.get().getServerPath(), currentServerPath)
			) {
				// server instance is out-of-date, recreate
				restartServerInstance();
			}
			
			updatedServerInstanceFuture = serverInstanceFuture;
		}
		
		return assertNotNull(updatedServerInstanceFuture.awaitResult2().get());
	}
	
}