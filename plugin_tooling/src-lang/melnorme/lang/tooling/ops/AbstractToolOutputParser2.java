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


import melnorme.lang.tooling.data.InfoResult;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public abstract class AbstractToolOutputParser2<RESULT> extends AbstractSingleToolOperation<RESULT> {
	
	protected final boolean nonZeroExitIsFatal;
	
	public AbstractToolOutputParser2(IOperationService opService, String toolPath, boolean nonZeroResultIsFatal) {
		super(opService, toolPath);
		this.nonZeroExitIsFatal = nonZeroResultIsFatal;
	}
	
	@Override
	protected void handleNonZeroExitCode(ExternalProcessResult result) throws CommonException, InfoResult {
		String nonZeroExitMsg = getToolName() + " did not complete successfully.";
		if(nonZeroExitIsFatal) {
			throw new CommonException(nonZeroExitMsg);
		} else {
			throw new InfoResult(nonZeroExitMsg);
		}
	}
	
	protected abstract String getToolName();
	
}