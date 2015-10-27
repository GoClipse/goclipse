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

import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.texteditor.ITextEditor;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.core.GoToolPreferences;
import com.googlecode.goclipse.core.operations.GoToolManager;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.tooling.oracle.GoOracleFindDefinitionOperation;

import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.ui.editor.EditorUtils.OpenNewEditorMode;
import melnorme.lang.ide.ui.editor.actions.AbstractOpenElementOperation;
import melnorme.lang.ide.ui.tools.console.DaemonToolMessageConsole;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.ops.FindDefinitionResult;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public class GoOracleOpenDefinitionOperation extends AbstractOpenElementOperation {
	
	public static final String OPEN_DEFINITION_OpName = "Open definition (go oracle)";
	
	public GoOracleOpenDefinitionOperation(ITextEditor editor, SourceRange range, OpenNewEditorMode openEditorMode) {
		super(OPEN_DEFINITION_OpName, editor, range, openEditorMode, null);
	}
	
	@Override
	protected void prepareOperation() throws CoreException, CommonException {
		super.prepareOperation();
		
		editor.doSave(new NullProgressMonitor());
		
		byteOffset = getByteOffsetForInvocationEncoding();
	}
	
	protected int byteOffset;
	
	protected int getByteOffsetForInvocationEncoding() throws CommonException {
		ITextFileBuffer textFileBuffer = ResourceUtils.getTextFileBuffer(inputLoc);
		Charset charset = Charset.forName(textFileBuffer.getEncoding());
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
		String goOraclePath = GoToolPreferences.GO_ORACLE_Path.get();
		
		GoEnvironment goEnv = GoProjectEnvironment.getGoEnvironment(project);
		
		GoOracleFindDefinitionOperation op = new GoOracleFindDefinitionOperation(goOraclePath);
		ProcessBuilder pb = op.createProcessBuilder(goEnv, inputLoc, byteOffset);
		
		ExternalProcessResult result = GoToolManager.getDefault().runEngineTool(pb, null, monitor);
		if(result.exitValue != 0) {
			statusErrorMessage = "Go oracle did not complete successfully.";
			return null;
		}
		
		return op.parseToolResult(result);
	}
	
	@Override
	protected void handleStatusErrorMessage() {
		DaemonToolMessageConsole engineToolsConsole = DaemonToolMessageConsole.getConsole();
		engineToolsConsole.activate();
		
		super.handleStatusErrorMessage();
	}
	
}