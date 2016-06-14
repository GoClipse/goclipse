package melnorme.lang.ide.ui.editor.hover;

import java.util.Optional;

import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.information.IInformationProviderExtension2;
import org.eclipse.ui.texteditor.ITextEditor;

import melnorme.lang.tooling.common.ISourceBuffer;

public interface ILangEditorTextHover<INFO> 
	extends ITextHoverExtension, IInformationProviderExtension2
{
	
	@SuppressWarnings("unused")
	default INFO getHoverInfo(ISourceBuffer sourceBuffer, IRegion hoverRegion, 
			Optional<ITextEditor> editor, ITextViewer textViewer) {
		return getHoverInfo(sourceBuffer, hoverRegion, textViewer);
	}
	
	INFO getHoverInfo(ISourceBuffer sourceBuffer, IRegion hoverRegion, ITextViewer textViewer);
	
	@Override
	IInformationControlCreator getHoverControlCreator();
	
	@Override
	IInformationControlCreator getInformationPresenterControlCreator();
	
}