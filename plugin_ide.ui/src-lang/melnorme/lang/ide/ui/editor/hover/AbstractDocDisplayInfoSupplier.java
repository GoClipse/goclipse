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
import melnorme.lang.ide.ui.utils.operations.ComputeValueUIOperation;
import melnorme.lang.tooling.common.ISourceBuffer;
import melnorme.lang.tooling.toolchain.ops.ToolResponse;
import melnorme.lang.tooling.utils.HTMLEscapeUtil;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.status.IStatusMessage;

public abstract class AbstractDocDisplayInfoSupplier implements Supplier<String> {
	
	protected final ISourceBuffer sourceBuffer;
	protected final int offset;
	
	public AbstractDocDisplayInfoSupplier(ISourceBuffer sourceBuffer, int offset) {
		this.sourceBuffer = assertNotNull(sourceBuffer);
		this.offset = offset;
	}
	
	@Override
	public String get() {
		ToolResponse<String> rawDocumentationResult;
		try {
			rawDocumentationResult = getRawDocumentation(sourceBuffer, offset);
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
		
		IStatusMessage statusMessage = rawDocumentationResult.getStatusMessage();
		if(statusMessage != null) {
			documentationInfo = documentationInfo == null ? "" : "</br> </hr>";
			documentationInfo += 
				"<b>" + statusMessage.getSeverity().getLabel() + ":</b> " + escapeToHTML(statusMessage.getMessage());
		}
		return documentationInfo;
	}
	
	protected String escapeToHTML(String rawDocumentation) {
		return HTMLEscapeUtil.escapeToToHTML(rawDocumentation);
	}
	
	protected ToolResponse<String> getRawDocumentation(ISourceBuffer sourceBuffer, int offset) 
			throws CommonException, OperationCancellation {
		return getOpenDocumentationOperation2(sourceBuffer, offset).call();
	}
	
	protected abstract ComputeValueUIOperation<ToolResponse<String>> getOpenDocumentationOperation2(
			ISourceBuffer sourceBuffer, int offset);
	
}