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


import melnorme.lang.tooling.ToolingMessages;
import melnorme.lang.utils.parse.StringParseSource;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public abstract class AbstractToolOutputParser<RESULT> extends AbstractToolOperation2<RESULT> {
	
	public AbstractToolOutputParser() {
		super();
	}
	
	public RESULT parse(ExternalProcessResult result) throws CommonException {
		return handleProcessResult(result);
	}
	
	@Override
	protected void handleNonZeroExitCode(int exitValue) throws CommonException {
		throw new CommonException(
			ToolingMessages.PROCESS_CompletedWithNonZeroValue(getToolProcessName(), exitValue));
	}
	
	protected abstract String getToolProcessName();
	
	@Override
	protected RESULT handleProcessResult(String source) throws CommonException {
		return parse(source);
	}
	
	public RESULT parse(String outputSource) throws CommonException {
		return parse(new StringParseSource(outputSource));
	}
	
	protected abstract RESULT parse(StringParseSource outputParseSource) throws CommonException;
	
	protected abstract void handleParseError(CommonException ce) throws CommonException;
	
}