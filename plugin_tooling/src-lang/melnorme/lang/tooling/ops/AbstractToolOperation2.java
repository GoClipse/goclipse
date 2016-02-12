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
package melnorme.lang.tooling.ops;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;


public abstract class AbstractToolOperation2<RESULT> extends AbstractToolOutputParser2<RESULT> {
	
	protected final IOperationService operationHelper;
	
	public AbstractToolOperation2(IOperationService opHelper, String toolPath) {
		super(toolPath);
		this.operationHelper = assertNotNull(opHelper);
	}
	
	protected ExternalProcessResult runToolProcess(ProcessBuilder pb, String input, ICancelMonitor cm) 
			throws CommonException, OperationCancellation {
		return operationHelper.runProcess(pb, input, cm);
	}
	
	public IOperationService getOperationHelper() {
		return operationHelper;
	}
	
}