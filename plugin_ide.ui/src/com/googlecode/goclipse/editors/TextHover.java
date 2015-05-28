package com.googlecode.goclipse.editors;


import melnorme.lang.ide.ui.editor.AbstractLangEditorTextHover;
import melnorme.lang.ide.ui.editor.ILangEditorTextHover;

import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;

// TODO: this does nothing at the moment...
public class TextHover extends AbstractLangEditorTextHover implements ILangEditorTextHover<String> {
	
	@Override
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		return null;
	}
	
	@Override
	public String getHoverInfo2(ITextViewer textViewer, IRegion hoverRegion) {
		return null;
	}
	
	@Override
	public IInformationControlCreator getHoverControlCreator() {
		return null;
	}
	
}