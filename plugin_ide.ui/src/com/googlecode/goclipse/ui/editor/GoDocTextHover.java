/*******************************************************************************
 * Copyright (c) 2008, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui.editor;

import java.util.Optional;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.texteditor.ITextEditor;

import com.googlecode.goclipse.tooling.oracle.GoFindDefinitionOperation;
import com.googlecode.goclipse.tooling.oracle.GoFindDocOperation;
import com.googlecode.goclipse.ui.actions.GoOpenDefinitionOperation;

import melnorme.lang.ide.ui.editor.hover.AbstractDocHover;
import melnorme.lang.ide.ui.utils.operations.CalculateValueUIOperation;
import melnorme.lang.tooling.common.ISourceBuffer;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.toolchain.ops.ToolOpResult;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

/**
 * Standard documentation hover.
 * (used in editor hovers extensions, and editor information provider (F2))
 */
public class GoDocTextHover extends AbstractDocHover {
	
	protected ITextEditor editor;
	
	public GoDocTextHover() {
	}
	
	@Override
	public String getHoverInfo(ISourceBuffer sourceBuffer, IRegion hoverRegion, Optional<ITextEditor> _editor,
			ITextViewer textViewer, boolean allowedToSaveBuffer) {
		/* FIXME: use sourceBuffer*/
		if(!_editor.isPresent()) {
			return null;
		}
		this.editor = _editor.get();
		return super.getHoverInfo(sourceBuffer, hoverRegion, _editor, textViewer, allowedToSaveBuffer);
	}
	
	@Override
	protected CalculateValueUIOperation<String> getOpenDocumentationOperation(ISourceBuffer sourceBuffer, int offset) {
		/* FIXME: use sourceBuffer*/
		return new GoOpenDocumentationOperation("Get Documentation", editor, offset);
	}
	
	public static class GoOpenDocumentationOperation extends CalculateValueUIOperation<String> {
		
		protected final GoFindDefinitionOperation findDefOperation;
		
		public GoOpenDocumentationOperation(String operationName, ITextEditor editor, int offset) {
			super(operationName, true);
			findDefOperation = GoOpenDefinitionOperation.getFindDefinitionOperation(editor, offset);
		}
		
		@Override
		protected String doBackgroundValueComputation(IOperationMonitor om)
				throws CommonException, OperationCancellation {
			ToolOpResult<String> opResult = new GoFindDocOperation(findDefOperation).execute(om);
			try {
				return opResult.get();
			} catch(CommonException e) {
				return e.getMessage();
			}
		}
		
	}
	
}