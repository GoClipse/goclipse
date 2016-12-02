/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *     Pieter Penninckx - added copy constructor for ToolMessageData
 *******************************************************************************/
package melnorme.lang.tooling.toolchain.ops;

import java.io.IOException;

import melnorme.lang.tooling.common.ToolSourceMessage;
import melnorme.lang.utils.parse.StringCharSource;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

/**
 * Improved API
 */
public abstract class BuildOutputParser3 extends BuildOutputParser2 {
	
	public BuildOutputParser3() {
		buildMessages = new ArrayList2<>();
	}
	
	@Override
	public ArrayList2<ToolSourceMessage> doParseResult(ExternalProcessResult result)
			throws CommonException {
		try {
			validateExitCode(result);
			
			return parseOutputStreams(result);
		} catch(OperationSoftFailure e) {
			// There shouldn't even be a StatusValidation error for build, 
			// because the source should always be able to be analysed. (might need  to refactor this)
			throw new CommonException(e.getMessage());
		}
	}
	
	public ArrayList2<ToolSourceMessage> parseOutputStreams(ExternalProcessResult result) throws CommonException {
		
		try {
			parseStdOut(new StringCharSource(result.getStdOutBytes().toString(StringUtil.UTF8)));
		} catch(IOException e) {
			throw new CommonException("Error reading stdout: ", e);
		}
		try {
			parseStdErr(new StringCharSource(result.getStdErrBytes().toString(StringUtil.UTF8)));
		} catch(IOException e) {
			throw new CommonException("Error reading stderr: ", e);
		}
		
		return buildMessages;
	}
	
	public void parseStdOut(StringCharSource stdout) throws CommonException, IOException {
		parseOutput(stdout);
	}
	
	public void parseStdErr(StringCharSource stderr) throws CommonException, IOException {
		parseOutput(stderr);
	}
	
}