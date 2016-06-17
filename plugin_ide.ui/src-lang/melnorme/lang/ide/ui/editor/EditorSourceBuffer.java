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
package melnorme.lang.ide.ui.editor;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.texteditor.ITextEditor;

import melnorme.lang.ide.core.text.ISourceBufferExt;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.ui.utils.operations.WorkbenchOperationExecutor;
import melnorme.lang.tooling.common.ISourceBuffer;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.CommonResult;
import melnorme.utilbox.core.fntypes.OperationCallable;
import melnorme.utilbox.core.fntypes.ResultRunnable;
import melnorme.utilbox.misc.Location;

/**
 * An abstraction of the buffer of an editor, or af a source viewer.
 * Note that location is not immutable! 
 */
public class EditorSourceBuffer implements ISourceBufferExt {
	
	protected final ITextEditor editor;
	
	public EditorSourceBuffer(ITextEditor editor) {
		this.editor = assertNotNull(editor);
	}
	
	@Override
	public Location getLocation_orNull() {
		return EditorUtils.getInputLocationOrNull(editor);
	}
	
	@Override
	public IDocument getDocument() {
		return EditorUtils.getEditorDocument(editor);
	}
	
	@Override
	public boolean isDirty() {
		return editor.isDirty();
	}
	
	@Override
	public void doTrySaveBuffer() throws CommonException, OperationCancellation {
		if(Display.getCurrent() == null) {
			OperationCallable<Void> operationCallable = () -> {
				saveBuffer();
				return null;
			};
			ResultRunnable<CommonResult<Void>> resultRunnable = operationCallable.toResultRunnable();
			Display.getDefault().syncExec(resultRunnable);
			resultRunnable.getResult().get();
		} else {
			saveBuffer();
		}
	}
	
	protected void saveBuffer() throws CommonException, OperationCancellation {
		// Run under a progress dialog, but in the UI thread 
		new WorkbenchOperationExecutor(true).execute(this::doSaveBuffer);
	}
	
	public void doSaveBuffer(ICancelMonitor cm) throws OperationCancellation {
		cm.checkCancellation();
		if(editor instanceof AbstractLangEditor) {
			AbstractLangEditor langEditor = (AbstractLangEditor) editor;
			langEditor.saveWithoutSaveActions2(cm);
		} else {
			editor.doSave(EclipseUtils.pm(cm));
		}
	}
	
	public static final CommonException CANNOT_SAVE_ReadOnlyView = 
			new CommonException("Cannot save editor, read-only view.");
	
	@Override
	public ISourceBuffer getReadOnlyView() {
		return new EditorSourceBuffer(editor) {
			@Override
			public void doTrySaveBuffer() throws CommonException {
				throw CANNOT_SAVE_ReadOnlyView;
			}
		};
	}
	
	/* -----------------  ----------------- */
	
	public static class DocumentSourceBuffer implements ISourceBufferExt {
		
		protected final IDocument document;
		
		public DocumentSourceBuffer(IDocument document) {
			this.document = assertNotNull(document);
		}
		
		@Override
		public Location getLocation_orNull() {
			return null;
		}
		
		@Override
		public IDocument getDocument() {
			return document;
		}
		
		@Override
		public boolean isDirty() {
			// a source buffer with no location in the filesystem is always considered dirty
			// note: subclasses may override this
			return true; 
		}
		
		@Override
		public void doTrySaveBuffer() throws CommonException {
			throw new CommonException("Cannot save document for this source buffer");
		}
		
		@Override
		public ISourceBuffer getReadOnlyView() {
			return this; // This buffer is already readOnly
		}
		
	}
	
}