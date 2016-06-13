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
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.texteditor.ITextEditor;

import melnorme.lang.ide.core.text.ISourceBufferExt;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.ui.utils.WorkbenchUtils;
import melnorme.lang.ide.ui.utils.operations.RunnableWithProgressOperationAdapter.ProgressMonitorDialogOpRunner;
import melnorme.lang.ide.ui.utils.operations.UIOperation;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
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
	public boolean isEditable() {
		return editor.isEditable();
	}
	
	@Override
	public boolean isDirty() {
		return editor.isDirty();
	}
	
	@Override
	public boolean trySaveBuffer() {
		Shell shell = WorkbenchUtils.getActiveWorkbenchShell();
		UIOperation op = new UIOperation("Saving editor for hover information", this::saveBuffer) {
			@Override
			protected void executeBackgroundOperation() throws CommonException, OperationCancellation {
				// Execute under ProgressMonitorDialog, but not on a background thread.
				new ProgressMonitorDialogOpRunner(shell, getBackgroundOperation()) {{ 
					fork = false; 
				}}.execute();
			}
		};
		
		return op.executeAndHandle();
	}
	
	public void saveBuffer(ICancelMonitor cm) {
		if(editor instanceof AbstractLangEditor) {
			AbstractLangEditor langEditor = (AbstractLangEditor) editor;
			langEditor.saveWithoutSaveActions2(cm);
		} else {
			editor.doSave(EclipseUtils.pm(cm));
		}
	}
	
	/* -----------------  ----------------- */
	
	public static class SourceViewerSourceBuffer implements ISourceBufferExt {
		
		protected final ISourceViewer sourceViewer;
		
		public SourceViewerSourceBuffer(ISourceViewer sourceViewer) {
			this.sourceViewer = assertNotNull(sourceViewer);
		}
		
		@Override
		public Location getLocation_orNull() {
			return null;
		}
		
		@Override
		public IDocument getDocument() {
			return sourceViewer.getDocument();
		}
		
		@Override
		public boolean isEditable() {
			return sourceViewer.isEditable();
		}
		
		@Override
		public boolean isDirty() {
			return true; // a source buffer with not location in the filesystem is always considered dirty
		}
		
		@Override
		public boolean trySaveBuffer() {
			return false;
		}
		
	}
	
}