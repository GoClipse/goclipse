package com.googlecode.goclipse.editors;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.source.DefaultCharacterPairMatcher;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

public class PairMatcher extends Action implements IEditorActionDelegate {
	
	private IEditorPart targetEditor;
	
	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.targetEditor = targetEditor;
	}
	
	@Override
	public final void selectionChanged(IAction action, ISelection selection) {
	}
	
	@Override
	public void run(IAction action) {
		run();
	}
	
	@Override
	public void run() {
		if (!(targetEditor instanceof GoEditor)) {
			return;
		}
		
		GoEditor editor = (GoEditor) targetEditor;
		
		ISelection selection = editor.getSelectionProvider().getSelection();
		
		if (selection != null && selection instanceof TextSelection) {
			DefaultCharacterPairMatcher matcher = editor.getPairMatcher();
			IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
			Object obj = editor.getAdapter(Control.class);
			if (obj != null && obj instanceof StyledText) {
				StyledText st = (StyledText)obj;
				int startOffset = st.getCaretOffset();
				IRegion r = matcher.match(doc, startOffset);
				if (r != null) {
					int ro = r.getOffset();
					int low = ro + 1;
					int high = ro + r.getLength();
					int pairOffset = low == startOffset?high:low;
					
					editor.setHighlightRange(pairOffset, 1, true);
				}
			}
			
		}
		selection = null;
	}
	
}