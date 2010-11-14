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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class PairMatcher extends Action implements IWorkbenchWindowActionDelegate{
	private GoEditor editor;
	private ISelection selection;

	@Override
	public void run() {
		super.run();
	}

	@Override
	public void run(IAction action) {
		if (editor != null && selection != null && selection instanceof TextSelection) {
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

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;		
	}

	@Override
	public void dispose() {
		selection = null;
		editor = null;
		
	}

	@Override
	public void init(IWorkbenchWindow window) {
		System.out.println(window);
		IEditorPart editorPart = window.getActivePage().getActiveEditor();
		if (editorPart instanceof GoEditor) {
			editor = (GoEditor)editorPart;
		} else {
			editor = null;
		}
	}
	
	
}
