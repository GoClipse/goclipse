/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.text.completion;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.option;

import org.eclipse.jface.text.IDocument;

import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.common.ISourceBuffer;
import melnorme.lang.tooling.toolchain.ops.SourceOpContext;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public class CompletionContext {
	
	protected final ISourceBuffer sourceBuffer;
	protected final IDocument document;
	protected final SourceOpContext context;
	
	public CompletionContext(ISourceBuffer sourceBuffer, int offset, SourceRange selection, IDocument document) {
		this.sourceBuffer = assertNotNull(sourceBuffer);
		this.document = assertNotNull(document);
		
		Location fileLocation = sourceBuffer.getLocation_orNull();
		boolean dirty = sourceBuffer.isDirty();
		this.context = new SourceOpContext(option(fileLocation), offset, selection, document.get(), dirty);
	}
	
	public ISourceBuffer getSourceBuffer() {
		return assertNotNull(sourceBuffer);
	}
	
	public SourceOpContext getContext() {
		return context;
	}
	public String getSource() {
		return context.getSource();
	}
	
	public int getOffset() {
		return context.getOffset();
	}
	
	public SourceRange getSelection() {
		return context.getSelection();
	}
	
	public Location getEditorInputLocation() throws CommonException {
		return context.getFileLocation();
	}
	
	public IDocument getDocument() {
		return document;
	}
	
}