/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui.actions;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.texteditor.ITextEditor;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.core.GoToolPreferences;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.tooling.oracle.GoOracleFindDefinitionOperation;
import com.googlecode.goclipse.tooling.oracle.GodefOperation;

import melnorme.lang.ide.core.utils.operation.EclipseCancelMonitor;
import melnorme.lang.ide.ui.editor.EditorUtils.OpenNewEditorMode;
import melnorme.lang.ide.ui.editor.actions.AbstractOpenElementOperation;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.ops.FindDefinitionResult;
import melnorme.lang.tooling.ops.OperationSoftFailure;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;

public class GoOpenDefinitionOperation extends AbstractOpenElementOperation {
	
	public static final String OPEN_DEFINITION_OpName = "Open definition";
	
	public GoOpenDefinitionOperation(ITextEditor editor, SourceRange range, OpenNewEditorMode openEditorMode) {
		super(OPEN_DEFINITION_OpName, editor, range, openEditorMode);
	}
	
	@Override
	protected void prepareOperation() throws CoreException, CommonException {
		super.prepareOperation();
		assertNotNull(inputLoc);
		editor.doSave(new NullProgressMonitor());
		
		byteOffset = getByteOffsetForInvocationEncoding();
	}
	
	protected int byteOffset;
	
	protected int getByteOffsetForInvocationEncoding() throws CommonException {
		Charset charset = StringUtil.UTF8; // All Go source file must be encoded in UTF8, not another format.
		return getByteOffsetFromEncoding(source, getInvocationOffset(), charset);
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
	protected FindDefinitionResult performLongRunningComputation_doAndGetResult(IProgressMonitor monitor) 
			throws CoreException, CommonException, OperationCancellation {
		String goOraclePath = GoToolPreferences.GO_ORACLE_Path.getDerivedValue().toString();
		
		GoEnvironment goEnv = GoProjectEnvironment.getGoEnvironment(project);
		
		EclipseCancelMonitor cm = cm(monitor);
		
		try {
			String godefPath = GoToolPreferences.GODEF_Path.getDerivedValue().toString();
			return new GodefOperation(this, godefPath, goEnv, inputLoc, byteOffset).execute(cm);
		} catch(OperationSoftFailure | CommonException e) {

			// Try go oracle as an alternative
			try {
				return new GoOracleFindDefinitionOperation(goOraclePath).execute(inputLoc, byteOffset, goEnv, this, cm);
			} catch(OperationSoftFailure | CommonException oracleError) {
				// Ignore oracle error, display previous godef error 
			}
			
			try {
				throw e;
			} catch(OperationSoftFailure ir) {
				statusErrorMessage = e.getMessage();
				return null;
			}
		}
		
	}
	
}