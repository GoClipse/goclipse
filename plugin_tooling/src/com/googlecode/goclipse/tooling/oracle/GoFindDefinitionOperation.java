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
package com.googlecode.goclipse.tooling.oracle;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import com.googlecode.goclipse.tooling.env.GoEnvironment;

import melnorme.lang.tooling.common.ISourceBuffer;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.common.ops.ResultOperation;
import melnorme.lang.tooling.toolchain.ops.FindDefinitionResult;
import melnorme.lang.tooling.toolchain.ops.IToolOperationService;
import melnorme.lang.tooling.toolchain.ops.SourceOpContext;
import melnorme.lang.tooling.toolchain.ops.ToolResponse;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.StringUtil;

public abstract class GoFindDefinitionOperation implements ResultOperation<ToolResponse<FindDefinitionResult>> {
	
	protected final GoOperationContext goOperationContext;
	protected final GoEnvironment goEnv;
	protected final SourceOpContext opContext;
	protected final int invocationOffset;
	protected final String source;
	protected final IToolOperationService toolOpService;
	
	public GoFindDefinitionOperation(GoOperationContext goOperationContext) {
		this.goOperationContext = assertNotNull(goOperationContext);
		this.goEnv = assertNotNull(goOperationContext.goEnv);
		this.opContext = assertNotNull(goOperationContext.opContext);
		this.invocationOffset = opContext.getOffset();
		this.source = assertNotNull(opContext.getSource());
		this.toolOpService = assertNotNull(goOperationContext.toolOpService);
	}
	
	public ISourceBuffer getSourceBuffer() {
		return goOperationContext.getSourceBuffer();
	}
	
	public int getInvocationOffset() {
		return invocationOffset;
	}
	
	public String getSource() {
		return source;
	}
	
	public Location getFileLocation() throws CommonException {
		return opContext.getFileLocation();
	}
	
	protected int getByteOffsetFromEncoding(String source, int charOffset, Charset charset) throws CommonException {
		CharsetEncoder encoder = charset.newEncoder();
		
		CharBuffer src = CharBuffer.wrap(source, 0, charOffset);
		try {
			return encoder.encode(src).limit();
		} catch(CharacterCodingException e) {
			throw new CommonException("Could not determine byte offset for Unicode string.", e);
		}
	}
	
	@Override
	public ToolResponse<FindDefinitionResult> executeOp(IOperationMonitor cm) 
			throws CommonException, OperationCancellation {
		Location fileLocation = getFileLocation(); // Validate early
		
		Charset charset = StringUtil.UTF8; // All Go source file must be encoded in UTF8, not another format.
		int byteOffset = getByteOffsetFromEncoding(source, invocationOffset, charset);
		
		ToolResponse<FindDefinitionResult> godefResult = null;
		try {
			String godefPath = getGodefPath();
			godefResult = new GodefOperation(toolOpService, godefPath, goEnv, opContext, byteOffset).execute(cm);
			
			godefResult.getValidResult(); // ensure valid, throw otherwise
			return godefResult;
		} catch(CommonException e) {
			
			// Try go oracle as an alternative
			try {
				String guruPath = getGuruPath();
				ToolResponse<FindDefinitionResult> result = new GuruFindDefinitionOperation(guruPath)
						.execute(fileLocation, byteOffset, goEnv, toolOpService, cm);
				result.getValidResult(); // check that it is valid
				return result;
			} catch(CommonException e2) {
				// Ignore oracle error, display previous godef error 
			}
			if(godefResult != null) {
				return godefResult;
			}
			throw e;
		}
	}
	
	protected abstract String getGodefPath() throws CommonException;
	
	protected abstract String getGuruPath() throws CommonException;
	
}