/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.utils.operations;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.text.MessageFormat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.utils.UIOperationsStatusHandler;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public abstract class BasicUIOperation implements Runnable {
	
	protected static final String MSG_EXECUTING_OPERATION 
		= "{0}";
	protected static final String MSG_ERROR_EXECUTING_OPERATION 
		= "Error executing `{0}`.";
	protected static final String MSG_INTERNAL_ERROR_EXECUTING_OPERATION 
		= "Internal error executing `{0}`.";
	
	
	protected final String operationName;
	
	public BasicUIOperation() {
		this.operationName = assertNotNull("");
	}
	
	public BasicUIOperation(String operationName) {
		this.operationName = assertNotNull(operationName);
	}
	
	public String getOperationName() {
		return operationName;
	}
	
	@Override
	public final void run() {
		executeAndHandle();
	}
	
	public void executeAndHandle() {
		assertTrue(Display.getCurrent() != null);
		
		try {
			execute0();
		} catch(CoreException ce) {
			handleError(ce);
		} catch(CommonException ce) {
			handleError(ce);
		} catch(RuntimeException re) {
			handleRuntimeException(re);
		}
	}
	
	protected void handleError(CoreException ce) {
		handleError(LangCore.createCommonException(ce));
	}
	
	protected void handleError(CommonException ce) {
		String title = operationName.isEmpty() ? 
				LangUIMessages.Error : 
				MessageFormat.format(MSG_ERROR_EXECUTING_OPERATION, operationName);
		UIOperationsStatusHandler.handleStatus(title, ce);
	}
	
	protected void handleRuntimeException(RuntimeException re) {
		String title = operationName.isEmpty() ?
				LangUIMessages.InternalError :
				MessageFormat.format(MSG_INTERNAL_ERROR_EXECUTING_OPERATION, operationName);
		UIOperationsStatusHandler.handleStatus(true, null, title, LangUIMessages.InternalErrorOccured, re);
	}
	
	/* -----------------  ----------------- */
	
	protected final void execute0() throws CoreException, CommonException {
		prepareOperation();
		try {
			doOperation();
		} catch (OperationCancellation e) {
			handleOperationCancellation();
		}
	}
	
	protected void prepareOperation() throws CoreException, CommonException {
	}
	
	protected abstract void doOperation() throws CoreException, CommonException, OperationCancellation;
	
	protected void handleOperationCancellation() throws CoreException, CommonException {
	}
	
}