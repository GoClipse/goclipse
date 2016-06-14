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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import org.eclipse.ui.texteditor.ITextEditor;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.tooling.oracle.GoOperationContext;

import melnorme.lang.ide.ui.editor.EditorSourceBuffer;
import melnorme.lang.ide.ui.editor.EditorUtils.OpenNewEditorMode;
import melnorme.lang.ide.ui.editor.actions.AbstractOpenElementOperation;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.toolchain.ops.SourceLocation;
import melnorme.lang.tooling.toolchain.ops.ToolResponse;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class GoOpenDefinitionOperation extends AbstractOpenElementOperation {
	
	public static final String OPEN_DEFINITION_OpName = "Open definition";
	
	public GoOpenDefinitionOperation(ITextEditor editor, SourceRange range, OpenNewEditorMode openEditorMode) {
		super(OPEN_DEFINITION_OpName, editor, range, openEditorMode);
	}
	
	@Override
	protected void executeBackgroundOperation() throws CommonException, OperationCancellation {
		GoOperationContext goOpContext = GoProjectEnvironment.getGoOperationContext(
			new EditorSourceBuffer(editor), getOperationOffset());
		result = new GoFindDefinitionOperation(goOpContext).call();
	}
	
	@Override
	protected ToolResponse<SourceLocation> doBackgroundValueComputation(IOperationMonitor om)
			throws CommonException, OperationCancellation {
		throw assertFail();
	}
	
}