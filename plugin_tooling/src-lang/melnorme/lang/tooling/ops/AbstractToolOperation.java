/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
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
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;


public abstract class AbstractToolOperation {
	
	protected final IProcessRunner processRunner;
	
	public AbstractToolOperation(IProcessRunner processRunner) {
		this.processRunner = assertNotNull(processRunner);
	}
	
	protected ExternalProcessResult runToolProcess(ProcessBuilder pb, String input) 
			throws CommonException, OperationCancellation {
		return processRunner.runProcess(pb, input);
	};
	
}