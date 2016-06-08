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

import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.texteditor.ITextEditor;

import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.ui.utils.WorkbenchUtils;
import melnorme.lang.ide.ui.utils.operations.RunnableWithProgressOperationAdapter.ProgressMonitorDialogOpRunner;
import melnorme.lang.ide.ui.utils.operations.UIOperation;
import melnorme.lang.tooling.common.ISourceBuffer;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

/**
 * An abstraction of the buffer of an editor, or af a source viewer. 
 */
public class EditorSourceModule implements ISourceBuffer {
	
	protected final ITextEditor editor;
	
	public EditorSourceModule(ITextEditor editor) {
		this.editor = assertNotNull(editor);
	}
	
	@Override
	public Location getLocation_orNull() {
		return EditorUtils.getInputLocationOrNull(editor);
	}
	
	@Override
	public String getSource() {
		return EditorUtils.getEditorDocument(editor).get();
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
		UIOperation op = new UIOperation("Saving editor for hover information", this::saveContents) {
			@Override
			protected void executeBackgroundOperation() throws CommonException, OperationCancellation {
				// Execute under ProgressMonitorDialog, but not on a background thread.
				new ProgressMonitorDialogOpRunner(shell, this::runBackgroundComputation) {{ 
					fork = false; 
				}}.execute();
			}
		};
		
		return op.executeAndHandle();
	}
	
	public void saveContents(ICancelMonitor cm) {
		if(editor instanceof AbstractLangEditor) {
			AbstractLangEditor langEditor = (AbstractLangEditor) editor;
			langEditor.saveWithoutSaveActions2(cm);
		} else {
			editor.doSave(EclipseUtils.pm(cm));
		}
	}
	
	/* -----------------  ----------------- */
	
	public static class SourceViewerSourceModule implements ISourceBuffer {
		
		protected final ISourceViewer sourceViewer;
		
		public SourceViewerSourceModule(ISourceViewer sourceViewer) {
			this.sourceViewer = assertNotNull(sourceViewer);
		}
		
		@Override
		public Location getLocation_orNull() {
			return null;
		}
		
		@Override
		public String getSource() {
			return sourceViewer.getDocument().get();
		}
		
		@Override
		public boolean isEditable() {
			return sourceViewer.isEditable();
		}
		
		@Override
		public boolean isDirty() {
			return true; /* FIXME: */
		}
		
		@Override
		public boolean trySaveBuffer() {
			return false;
		}
		
	}
	
}