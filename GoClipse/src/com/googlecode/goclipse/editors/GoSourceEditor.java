package com.googlecode.goclipse.editors;

import org.eclipse.ui.editors.text.TextEditor;

public class GoSourceEditor extends TextEditor {

	private ColorManager colorManager;

	public GoSourceEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new XMLConfiguration(colorManager));
		setDocumentProvider(new XMLDocumentProvider());
	}
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
