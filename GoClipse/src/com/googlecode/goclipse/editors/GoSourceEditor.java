package com.googlecode.goclipse.editors;

import org.eclipse.ui.editors.text.TextEditor;

public class GoSourceEditor extends TextEditor {

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

}
