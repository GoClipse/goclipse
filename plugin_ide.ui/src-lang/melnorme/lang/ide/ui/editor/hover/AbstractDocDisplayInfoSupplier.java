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

import java.util.function.Supplier;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.utils.operations.CalculateValueUIOperation;
import melnorme.lang.tooling.common.ISourceBuffer;
import melnorme.lang.tooling.common.ops.CommonResultOperation;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.toolchain.ops.ToolOpResult;
import melnorme.lang.tooling.utils.HTMLEscapeUtil;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;

public abstract class AbstractDocDisplayInfoSupplier implements Supplier<String> {
	
	protected final ISourceBuffer sourceBuffer;
	protected final int offset;
	
	public AbstractDocDisplayInfoSupplier(ISourceBuffer sourceBuffer, int offset) {
		this.sourceBuffer = assertNotNull(sourceBuffer);
		this.offset = offset;
	}
	
	@Override
	public String get() {
		try {
			String rawDocumentation = getRawDocumentation(sourceBuffer, offset);
			
			if(rawDocumentation == null) {
				return null;
			}
			
			return escapeToHTML(rawDocumentation);
		} catch(CommonException ce) {
			LangCore.logStatusException(ce.toStatusException());
			// TODO: we could add a nicer HTML formatting:
			return "<b>Error:</b> " + ce.getMessage() + StringUtil.asString(" ", ce.getCause());
		}
	}
	
	protected String escapeToHTML(String rawDocumentation) {
		return HTMLEscapeUtil.escapeToToHTML(rawDocumentation);
	}
	
	protected String getRawDocumentation(ISourceBuffer sourceBuffer, int offset) throws CommonException {
		CalculateValueUIOperation<String> op = getOpenDocumentationOperation(sourceBuffer, offset);
		return op.executeAndGetValidatedResult();
	}
	
	protected abstract OpenDocumentationOperation getOpenDocumentationOperation(ISourceBuffer sourceBuffer, 
			int offset);
	
	public static class OpenDocumentationOperation extends CalculateValueUIOperation<String> {
		
		protected final CommonResultOperation<ToolOpResult<String>> findDocOperation;
		
		public OpenDocumentationOperation(String operationName,
				CommonResultOperation<ToolOpResult<String>> findDocOperation) {
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