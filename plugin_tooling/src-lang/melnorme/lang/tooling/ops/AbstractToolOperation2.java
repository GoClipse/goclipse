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
package melnorme.lang.tooling.ops;

import melnorme.lang.tooling.data.InfoResult;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public abstract class AbstractToolOperation2<RESULT> extends ToolOutputParseHelper {
	
	public AbstractToolOperation2() {
		super();
	}
	
	public RESULT handleProcessResult(ExternalProcessResult result) 
			throws CommonException, OperationCancellation, InfoResult {
		if(result.exitValue != 0) {
			handleNonZeroExitCode(result);
		}
		
		return doHandleProcessResult(result);
	}
	
	protected abstract void handleNonZeroExitCode(ExternalProcessResult result) 
			throws CommonException, OperationCancellation, InfoResult;
	
	protected RESULT doHandleProcessResult(ExternalProcessResult result) 
			throws CommonException, OperationCancellation, InfoResult {
		return handleProcessOutput(result.getStdOutBytes().toString(StringUtil.UTF8));
	}
	
	protected abstract RESULT handleProcessOutput(String output) 
			throws CommonException, OperationCancellation, InfoResult;
	
}