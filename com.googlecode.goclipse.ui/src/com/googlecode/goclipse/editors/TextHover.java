package com.googlecode.goclipse.editors;


import melnorme.lang.ide.ui.editor.AbstractLangEditorTextHover;

import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;

public class TextHover extends AbstractLangEditorTextHover<String> {
	
	@Override
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		return null;
	}
	
	@Override
	public String getHoverInfo2_do(ITextViewer textViewer, IRegion hoverRegion) {
		return null;
	}
	
	@Override
	public IInformationControlCreator getHoverControlCreator() {
		return null;
	}
	
}