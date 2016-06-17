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
import melnorme.lang.ide.ui.utils.operations.WorkbenchOperationExecutor;
import melnorme.lang.tooling.common.ISourceBuffer;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.toolchain.ops.AbstractToolOperation;
import melnorme.lang.tooling.toolchain.ops.ToolResponse;
import melnorme.lang.tooling.utils.HTMLHelper;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.status.Severity;

public abstract class AbstractDocDisplayInfoSupplier implements Supplier<String> {
	
	protected final ISourceBuffer sourceBuffer;
	protected final int offset;
	
	public AbstractDocDisplayInfoSupplier(ISourceBuffer sourceBuffer, int offset) {
		this.sourceBuffer = assertNotNull(sourceBuffer);
		this.offset = offset;
	}
	
	@Override
	public String get() {
		return new WorkbenchOperationExecutor().callInBackground(this::doGetDocumentation);
	}
	
	public String doGetDocumentation(IOperationMonitor om) {
		ToolResponse<String> rawDocumentationResult;
		try {
			rawDocumentationResult = getRawDocumentation(om, sourceBuffer, offset);
		} catch(CommonException ce) {
			LangCore.logStatusException(ce.toStatusException());
			// TODO: we could add a nicer HTML formatting:
			return "<b>Operation Error:</b> " + ce.getMessage() + StringUtil.asString(" ", ce.getCause());
		} catch(OperationCancellation e) {
			return null;
		}
		
		String documentationInfo;
		
		String rawDocumentation = rawDocumentationResult.getResultData();
		if(rawDocumentation == null) {
			documentationInfo = null;
		} else {
			documentationInfo = escapeToHTML(rawDocumentation);
		}
		
		String errorMessage = rawDocumentationResult.getErrorMessage();
		if(errorMessage != null) {
			documentationInfo = documentationInfo == null ? "" : "</br> </hr>";
			Severity severity = Severity.ERROR;
			documentationInfo += 
				"<b>" + severity.getLabel() + ":</b> " + escapeToHTML(errorMessage);

		}
		if(documentationInfo == null) {
			return null;
		}
		return documentationInfo;
	}
	
	protected String escapeToHTML(String rawDocumentation) {
		return HTMLHelper.escapeToToHTML(rawDocumentation);
	}
	
	protected ToolResponse<String> getRawDocumentation(IOperationMonitor om, ISourceBuffer sourceBuffer, int offset) 
			throws CommonException, OperationCancellation {
		AbstractToolOperation<String> findDefinitionOp = getFindDocOperation(sourceBuffer, offset);
		return findDefinitionOp.toResultOperation().executeOp(om);
	}
	
	protected abstract AbstractToolOperation<String> getFindDocOperation(ISourceBuffer sourceBuffer, int offset);
	
}