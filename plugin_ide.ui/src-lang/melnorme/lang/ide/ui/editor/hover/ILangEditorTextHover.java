package melnorme.lang.ide.ui.editor.hover;

import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.texteditor.ITextEditor;

public interface ILangEditorTextHover<INFO_TYPE> 
	extends ITextHoverExtension
{
	
	public INFO_TYPE getHoverInfo(ITextEditor editor, ITextViewer textViewer, IRegion hoverRegion);
	
	@Override
	public IInformationControlCreator getHoverControlCreator();
	
}