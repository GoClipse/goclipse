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

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import org.eclipse.swt.widgets.Display;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.utils.operations.RunnableWithProgressOperationAdapter.WorkbenchProgressServiceOpRunner;
import melnorme.lang.tooling.common.ISourceBuffer;
import melnorme.lang.tooling.common.ops.CommonOperation;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.common.ops.IOperationMonitor.NullOperationMonitor;
import melnorme.lang.tooling.common.ops.ResultOperation;
import melnorme.lang.tooling.toolchain.ops.ToolResponse;
import melnorme.lang.tooling.utils.HTMLEscapeUtil;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.status.StatusException;

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
		
		try {
			String rawDocumentation = rawDocumentationResult.getValidResult();
			if(rawDocumentation == null) {
				return null;
			}
			return escapeToHTML(rawDocumentation);
		} catch(StatusException e) {
			return "<b>Error:</b> " + e.getMessage();
		}
	}
	
	protected String escapeToHTML(String rawDocumentation) {
		return HTMLEscapeUtil.escapeToToHTML(rawDocumentation);
	}
	
	protected ToolResponse<String> getRawDocumentation(ISourceBuffer sourceBuffer, int offset) 
			throws CommonException, OperationCancellation {
		ResultOperation<ToolResponse<String>> openDocOp = getOpenDocumentationOperation2(sourceBuffer, offset);
		return invokeInBackground(openDocOp.namedOperation("Get Documentation"));
	}
	
	protected abstract ResultOperation<ToolResponse<String>> getOpenDocumentationOperation2(
			ISourceBuffer sourceBuffer, int offset);
	
	public <R> R invokeInBackground(ResultOperation<R> op) throws CommonException, OperationCancellation {
		
		AtomicReference<R> resultHolder = new AtomicReference<>();
		
		runInBackground(new CommonOperation() {
			@Override
			public void execute(IOperationMonitor om) throws CommonException, OperationCancellation {
				R result = op.executeOp(om);
				resultHolder.set(result);
			}
		});
		
		return resultHolder.get();
	}
	
	public void runInBackground(CommonOperation op) throws CommonException, OperationCancellation {
		if(Display.getCurrent() == null) {
			// Perform computation directly in this thread, but cancellation won't be possible.
			op.execute(new NullOperationMonitor());
		} else {
			new WorkbenchProgressServiceOpRunner(op).execute();
		}
	}
	
}