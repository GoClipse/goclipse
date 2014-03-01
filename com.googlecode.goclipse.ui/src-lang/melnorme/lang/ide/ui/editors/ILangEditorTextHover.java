package melnorme.lang.ide.ui.editors;

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
	 * 
	 * @param editor the editor on which the hover popup should be shown
	 */
	void setEditor(IEditorPart editor);
	
	@Override
	public Object getHoverInfo2(ITextViewer textViewer, IRegion hoverRegion);
	
	public INFO_TYPE getHoverInfo2_do(ITextViewer textViewer, IRegion hoverRegion);
	
	@Override
	public IInformationControlCreator getHoverControlCreator();
	
}