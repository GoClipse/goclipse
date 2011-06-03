package com.googlecode.goclipse.editors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.texteditor.ITextEditor;

import sun.text.normalizer.UTF16;

/**
 * 
 * 
 */
public class GoHyperlinkDetector extends AbstractHyperlinkDetector {

	/**
	 * 
	 */
	public GoHyperlinkDetector(GoEditor editor) {
		setContext(editor);
	}

	@Override
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean showMultiple) {
		ITextEditor textEditor = (ITextEditor)getAdapter(ITextEditor.class);
		
		int offset = region.getOffset();
		
		IDocument document = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
		IRegion wordRegion = findWord(document, offset);
		if (wordRegion == null || wordRegion.getLength() == 0) {
			return null;
		}
		
		try {
			String word = textViewer.getDocument().get(wordRegion.getOffset(), wordRegion.getLength());
			
			//System.out.println("[" + word + "]");
		} catch (BadLocationException e) {
			// ignore
		}
		
		return null;
	}

	private static IRegion findWord(IDocument document, int offset) {

		int start= -2;
		int end= -1;

		try {
			int pos= offset;
			char c;

			while (pos >= 0) {
				c= document.getChar(pos);
				if (!Character.isJavaIdentifierPart(c)) {
					// Check for surrogates
					if (UTF16.isSurrogate(c)) {
						
					} else {
						break;
					}
				}
				--pos;
			}
			start= pos;

			pos= offset;
			int length= document.getLength();

			while (pos < length) {
				c= document.getChar(pos);
				if (!Character.isJavaIdentifierPart(c))
					break;
				++pos;
			}
			end= pos;

		} catch (BadLocationException x) {
		}

		if (start >= -1 && end > -1) {
			if (start == offset && end == offset)
				return new Region(offset, 0);
			else if (start == offset)
				return new Region(start, end - start);
			else
				return new Region(start + 1, end - start - 1);
		}

		return null;
	}

}
