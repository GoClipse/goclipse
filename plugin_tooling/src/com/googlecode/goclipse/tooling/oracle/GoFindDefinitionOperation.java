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

import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.toolchain.ops.FindDefinitionResult;
import melnorme.lang.tooling.toolchain.ops.IToolOperationService;
import melnorme.lang.tooling.toolchain.ops.OperationSoftFailure;
import melnorme.lang.tooling.toolchain.ops.SourceOpContext;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.StringUtil;

public abstract class GoFindDefinitionOperation {
	
	protected final GoEnvironment goEnv;
	protected final SourceOpContext opContext;
	protected final int invocationOffset;
	protected final String source;
	protected final IToolOperationService toolOperationService;
	
	public GoFindDefinitionOperation(GoEnvironment goEnv, SourceOpContext opContext, 
			IToolOperationService toolOperationService) {
		this.goEnv = assertNotNull(goEnv);
		this.opContext = assertNotNull(opContext);
		this.invocationOffset = opContext.getOffset();
		this.source = assertNotNull(opContext.source);
		this.toolOperationService = assertNotNull(toolOperationService);
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
	
	public FindDefinitionResult execute(IOperationMonitor cm) 
			throws CommonException, OperationCancellation, OperationSoftFailure {
		Location fileLocation = getFileLocation();
		
		Charset charset = StringUtil.UTF8; // All Go source file must be encoded in UTF8, not another format.
		int byteOffset = getByteOffsetFromEncoding(source, invocationOffset, charset);
		
		try {
			String godefPath = getGodefPath();
			return new GodefOperation(toolOperationService, godefPath, goEnv, fileLocation, byteOffset).execute(cm);
		} catch(OperationSoftFailure | CommonException e) {
			
			// Try go oracle as an alternative
			try {
				String goOraclePath = getGuruPath();
				return new GuruFindDefinitionOperation(goOraclePath).execute(fileLocation, byteOffset, goEnv, 
					toolOperationService, cm);
			} catch(OperationSoftFailure | CommonException oracleError) {
				// Ignore oracle error, display previous godef error 
			}
			
			throw e;
		}
	}
	
	protected abstract String getGodefPath() throws CommonException;
	
	protected abstract String getGuruPath() throws CommonException;
	
}