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
import melnorme.lang.tooling.ops.IProcessRunner;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.runtime.IProgressMonitor;

/** Helper to start processes in the tool manager. */
public class ToolProcessRunner implements IProcessRunner {
	
	protected final IProgressMonitor pm;
	
	public ToolProcessRunner(IProgressMonitor pm) {
		this.pm = pm;
	}
	
	@Override
	public ExternalProcessResult runProcess(ProcessBuilder pb, String input) throws CommonException,
			OperationCancellation {
		return LangCore.getToolManager().new RunEngineClientOperation(pb, pm).doRunProcess(input, false);
	}
	
}