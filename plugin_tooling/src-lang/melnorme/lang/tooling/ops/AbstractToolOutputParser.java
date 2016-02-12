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

public abstract class AbstractToolOutputParser<RESULT> extends AbstractToolOutputParser2<RESULT> {
	
	public AbstractToolOutputParser(String toolPath) {
		super(toolPath, true);
	}
	
	public RESULT parse(ExternalProcessResult result) throws CommonException {
		int exitValue = result.exitValue;
		if(exitValue != 0) {
			handleNonZeroExitValue(result);
		}
		
		return doParse(result);
	}
	
	@Override
	protected void handleNonZeroExitValue(ExternalProcessResult result) throws CommonException {
		throw new CommonException(
			ToolingMessages.PROCESS_CompletedWithNonZeroVAlue(getToolName(), result.exitValue));
	}
	
	protected final RESULT doParse(ExternalProcessResult result) throws CommonException {
		return parseToolResult(result);
	}
	
	public final RESULT parse(String outputSource) throws CommonException {
		return parseToolResult(outputSource);
	}
	
	@Override
	public RESULT parseToolResult(String output) throws CommonException {
		return parse(new StringParseSource(output));
	}
	
	protected abstract RESULT parse(StringParseSource outputParseSource) throws CommonException;
	
	protected abstract void handleParseError(CommonException ce) throws CommonException;
	
}