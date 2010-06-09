package com.googlecode.goclipse.editors;

import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

public class GoSourceEditor extends TextEditor {

	private GoSourceOutlinePage fOutlinePage;

	public GoSourceEditor() {
		super();
		setDocumentProvider(new GoSourceDocumentProvider());
	}
	
	protected void initializeEditor() {
		super.initializeEditor();
		setSourceViewerConfiguration(new GoSourceConfiguration());
	}
	
	public void dispose() {
		super.dispose();
	}
	
	public Object getAdapter(Class required) {
		if (IContentOutlinePage.class.equals(required)) {
			if (fOutlinePage == null) {
				fOutlinePage= new GoSourceOutlinePage(getDocumentProvider(), this);
				if (getEditorInput() != null)
					fOutlinePage.setInput(getEditorInput());
			}
			return fOutlinePage;
		}
		return super.getAdapter(required);
	}

}
