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

import melnorme.lang.tooling.common.ToolSourceMessage;
import melnorme.lang.utils.parse.StringCharSource;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.IByteSequence;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;


public abstract class BuildOutputParser2 extends AbstractToolResultParser<ArrayList2<ToolSourceMessage>> {
	
	protected ArrayList2<ToolSourceMessage> buildMessages;
	
	public BuildOutputParser2() {
	}
	
	public ArrayList2<ToolSourceMessage> getBuildMessages() {
		return buildMessages;
	}
	
	@Override
	protected String getToolName() {
		return "Build tool";
	}
	
	@Override
	public ArrayList2<ToolSourceMessage> doParseResult(ExternalProcessResult result)
			throws CommonException {
		try {
			validateExitCode(result);
			
			return parseOutput(getOutputFromProcessResult(result).toString(StringUtil.UTF8));
		} catch(OperationSoftFailure e) {
			// There shouldn't even be a StatusValidation error for build, 
			// because the source should always be able to be analysed. (might need  to refactor this)
			throw new CommonException(e.getMessage());
		}
	}
	
	protected IByteSequence getOutputFromProcessResult(ExternalProcessResult result) {
		return result.getStdOutBytes();
	}
	
	@Override
	public final ArrayList2<ToolSourceMessage> parseOutput(String output) throws CommonException {
		return parseOutput(new StringCharSource(output));
	}
	
	@Override
	public ArrayList2<ToolSourceMessage> parseOutput(StringCharSource output) throws CommonException {
		buildMessages = new ArrayList2<>();
		
		while(output.hasCharAhead()) {
			doParseToolMessage(output);
		}
		
		return buildMessages;
	}
	
	protected void doParseToolMessage(StringCharSource output) {
		try {
			ToolMessageData toolMessage = parseMessageData(output);
			
			if(toolMessage != null) {
				addBuildMessage(toolMessage);
			}
		} catch (CommonException ce) {
			handleMessageParseError(ce);
		}
	}
	
	protected void addBuildMessage(ToolMessageData toolMessage) throws CommonException {
		addBuildMessage(toolMessageParser.createMessage(toolMessage));
	}
	
	public void addBuildMessage(ToolSourceMessage sourceMessage) {
		if(sourceMessage.message.startsWith("aborting due to ")) {
			return; // Ignore
		}
		buildMessages.add(sourceMessage);
	}
	
	protected abstract ToolMessageData parseMessageData(StringCharSource output) throws CommonException;
	
	protected final void handleMessageParseError(CommonException ce) {
		handleParseError(ce);
	}
	
	protected abstract void handleParseError(CommonException ce);
	
	protected CommonException createUnknownLineSyntaxError(String line) {
		return new CommonException("Unknown error line syntax: " + line);
	}
	
	/* -----------------  ----------------- */
	
	protected final ToolMessageParser toolMessageParser = init_ToolMessageParser();
	
	protected ToolMessageParser init_ToolMessageParser() {
		return new ToolMessageParser();
	}
	
}