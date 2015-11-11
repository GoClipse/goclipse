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
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public abstract class AbstractToolOutputParser<RESULT> extends ToolOutputParseHelper {
	
	public AbstractToolOutputParser() {
		super();
	}
	
	public RESULT parse(ExternalProcessResult result) throws CommonException {
		int exitValue = result.exitValue;
		if(exitValue != 0) {
			throw new CommonException(
				ToolingMessages.PROCESS_CompletedWithNonZeroVAlue(getToolProcessName(), exitValue));
		}
		
		return doParse(result);
	}
	
	protected abstract String getToolProcessName();
	
	protected RESULT doParse(ExternalProcessResult result) throws CommonException {
		return parse(result.getStdOutBytes().toString(StringUtil.UTF8));
	}
	
	public RESULT parse(String outputSource) throws CommonException {
		return parse(new StringParseSource(outputSource));
	}
	
	protected abstract RESULT parse(StringParseSource source) throws CommonException;
	
	protected abstract void handleMessageParseError(CommonException ce) throws CommonException;
	
}