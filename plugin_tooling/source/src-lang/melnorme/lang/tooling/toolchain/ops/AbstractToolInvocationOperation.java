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

import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public abstract class AbstractToolInvocationOperation<RESULTDATA>
	extends AbstractToolResultParser<RESULTDATA>
	implements AbstractToolOperation<RESULTDATA> {
	
	protected final IToolOperationService opHelper;
	protected final String toolPath;

	protected String toolInput = "";
	
	public AbstractToolInvocationOperation(IToolOperationService opHelper, String toolPath) {
		this.opHelper = assertNotNull(opHelper);
		this.toolPath = assertNotNull(toolPath);
	}
	
	@Override
	public RESULTDATA executeToolOperation(IOperationMonitor om)
			throws CommonException, OperationCancellation, OperationSoftFailure {
		ProcessBuilder pb = createProcessBuilder();
		ExternalProcessResult processResult = opHelper.runProcess(pb, toolInput, om);
		return doParseResult(processResult);
	}
	
	protected abstract ProcessBuilder createProcessBuilder() throws CommonException;
	
}