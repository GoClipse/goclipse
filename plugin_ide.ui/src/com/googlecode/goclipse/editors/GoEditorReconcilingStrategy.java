package com.googlecode.goclipse.editors;

import melnorme.lang.ide.ui.editor.text.LangReconcilingStrategy;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;

import com.googlecode.goclipse.ui.editor.GoEditorSourceViewerConfiguration;

/**
 * This IReconcilingStrategy notifies the editor that a reconcile is occuring.
 * 
 * @see GoEditorSourceViewerConfiguration
 */
public class GoEditorReconcilingStrategy extends LangReconcilingStrategy {
	
	private GoEditor editor;

	/**
	 * Create a new GoEditorReconcilingStrategy.
	 * 
	 * @param editor
	 */
	public GoEditorReconcilingStrategy(GoEditor editor) {
		super(editor);
		this.editor = editor;
	}

//	@Override
//	public void setDocument(IDocument document) {
//	}

	@Override
	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		// This really won't get called, as we indicate that we don't support incremental
		// reconciliation.
		editor.handleReconcilation(null);
	}
	
	@Override
	public void reconcile(IRegion partition) {
		super.reconcile(partition);
		
		editor.handleReconcilation(partition);
	}

}
