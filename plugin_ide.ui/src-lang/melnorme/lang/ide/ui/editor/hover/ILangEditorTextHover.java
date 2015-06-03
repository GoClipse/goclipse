package melnorme.lang.ide.ui.editor.hover;

import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextHoverExtension2;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.IEditorPart;

public interface ILangEditorTextHover<INFO_TYPE> extends ITextHover, ITextHoverExtension, ITextHoverExtension2 {
	
	/**
	 * Sets the editor on which the hover is shown.
	 */
	void setEditor(IEditorPart editor);
	
	@Override
	public INFO_TYPE getHoverInfo2(ITextViewer textViewer, IRegion hoverRegion);
	
	@Override
	public IInformationControlCreator getHoverControlCreator();
	
}