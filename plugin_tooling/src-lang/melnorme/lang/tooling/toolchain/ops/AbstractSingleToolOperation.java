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
package melnorme.lang.tooling.toolchain.ops;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public abstract class AbstractSingleToolOperation<RESULT> extends AbstractToolOperation2<RESULT> {
	
	protected final IToolOperationService opHelper;
	protected final String toolPath;
	protected final boolean nonZeroExitIsFatal;

	protected String toolInput = "";
	
	public AbstractSingleToolOperation(IToolOperationService opHelper, String toolPath, boolean nonZeroResultIsFatal) {
		this.opHelper = assertNotNull(opHelper);
		this.toolPath = assertNotNull(toolPath);
		this.nonZeroExitIsFatal = nonZeroResultIsFatal;
	}
	
	public RESULT execute(ICancelMonitor cm) throws CommonException, OperationCancellation, OperationSoftFailure {
		ProcessBuilder pb = createProcessBuilder();
		ExternalProcessResult result = opHelper.runProcess(pb, toolInput, cm);
		return handleProcessResult(result);
	}
	
	protected abstract ProcessBuilder createProcessBuilder() throws CommonException;
	
	@Override
	protected void handleNonZeroExitCode(ExternalProcessResult result) throws CommonException, OperationSoftFailure {
		String nonZeroExitMsg = getToolName() + " did not complete successfully.";
		if(nonZeroExitIsFatal) {
			throw new CommonException(nonZeroExitMsg);
		} else {
			throw new OperationSoftFailure(nonZeroExitMsg);
		}
	}
	
	protected abstract String getToolName();

	
}