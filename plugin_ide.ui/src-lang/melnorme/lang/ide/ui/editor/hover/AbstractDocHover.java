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
package melnorme.lang.ide.ui.editor.hover;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.Optional;

import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.texteditor.ITextEditor;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.text.DocumentationHoverCreator;
import melnorme.lang.ide.ui.utils.operations.CalculateValueUIOperation;
import melnorme.lang.tooling.common.ISourceBuffer;
import melnorme.lang.tooling.common.ops.CommonOperation;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.toolchain.ops.ToolOpResult;
import melnorme.lang.tooling.utils.HTMLEscapeUtil;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;

public abstract class AbstractDocHover implements ILangEditorTextHover<String> {
	
	protected final DocumentationHoverCreator hoverCreator = new DocumentationHoverCreator(); 
	
	@Override
	public IInformationControlCreator getHoverControlCreator() {
		return hoverCreator.getHoverControlCreator();
	}
	
	@Override
	public IInformationControlCreator getInformationPresenterControlCreator() {
		return hoverCreator.getInformationPresenterControlCreator();
	}
	
	protected boolean requiresSavedBuffer() {
		return true;
	}
	
	@Override
	public String getHoverInfo(ISourceBuffer sourceBuffer, IRegion hoverRegion, Optional<ITextEditor> editor,
			ITextViewer textViewer, boolean allowedToSaveBuffer) {
		assertNotNull(sourceBuffer);
		
		if(requiresSavedBuffer() && sourceBuffer.isDirty()) {
			
			if(!allowedToSaveBuffer) {
				return null; // Nothing we can do, quite
			}
			
			boolean success = sourceBuffer.trySaveBuffer();
			if(!success) {
				return null;
			}
			
		}
		
		return getHoverInfo(sourceBuffer, hoverRegion, textViewer);
	}
	
	@Override
	public String getHoverInfo(ISourceBuffer sourceBuffer, IRegion hoverRegion, ITextViewer textViewer) {
		
		try {
			int offset = hoverRegion.getOffset();
			String rawDocumentation = getRawDocumentation(sourceBuffer, offset);
			
			if(rawDocumentation == null) {
				return null;
			}
			
			return HTMLEscapeUtil.escapeToToHTML(rawDocumentation);
		} catch(CommonException ce) {
			LangCore.logStatusException(ce.toStatusException());
			// TODO: we could add a nicer HTML formatting:
			return "<b>Error:</b> " + ce.getMessage() + StringUtil.asString(" ", ce.getCause());
		}
	}
	
	protected String getRawDocumentation(ISourceBuffer sourceBuffer, int offset) throws CommonException {
		CalculateValueUIOperation<String> op = getOpenDocumentationOperation(sourceBuffer, offset);
		return op.executeAndGetValidatedResult();
	}
	
	protected abstract OpenDocumentationOperation getOpenDocumentationOperation(ISourceBuffer sourceBuffer, 
			int offset);
	
	public static class OpenDocumentationOperation extends CalculateValueUIOperation<String> {
		
		protected final CommonOperation<ToolOpResult<String>> findDocOperation;
		
		public OpenDocumentationOperation(String operationName,
				CommonOperation<ToolOpResult<String>> findDocOperation) {
			super(operationName, true);
			this.findDocOperation = assertNotNull(findDocOperation);
		}
		
		@Override
		protected String doBackgroundValueComputation(IOperationMonitor om)
				throws CommonException, OperationCancellation {
			ToolOpResult<String> opResult = findDocOperation.executeOp(om);
			try {
				return opResult.get();
			} catch(CommonException e) {
				return e.getMessage();
			}
		}
		
	}
	
}