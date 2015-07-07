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
package melnorme.lang.ide.ui.tools;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.operation.EclipseCancelMonitor;
import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.ops.IOperationHelper;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.runtime.IProgressMonitor;

/** 
 * Helper to start processes in the tool manager. 
 */
public class ToolManagerOperationHelper implements IOperationHelper {
	
	protected final EclipseCancelMonitor cm;
	
	public ToolManagerOperationHelper(IProgressMonitor monitor) {
		this.cm = new EclipseCancelMonitor(monitor);
	}
	
	@Override
	public ExternalProcessResult runProcess(ProcessBuilder pb, String input) throws CommonException,
			OperationCancellation {
		return LangCore.getToolManager().new RunEngineClientOperation(pb, cm).doRunProcess(input, false);
	}
	
	@Override
	public void logStatus(StatusException statusException) {
		LangCore.logStatusException(statusException);
	}
	
}