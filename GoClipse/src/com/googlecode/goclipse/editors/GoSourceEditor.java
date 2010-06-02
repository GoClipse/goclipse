package com.googlecode.goclipse.editors;

import org.eclipse.ui.editors.text.TextEditor;

public class GoSourceEditor extends TextEditor {

	private ColorManager colorManager;

	public GoSourceEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new GoSourceConfiguration(colorManager));
		setDocumentProvider(new GoSourceDocumentProvider());
	}
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
