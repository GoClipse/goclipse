package melnorme.lang.ide.ui.editor.hover;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.information.IInformationProviderExtension2;

import melnorme.lang.ide.ui.editor.AbstractLangEditor;

public interface ILangEditorTextHover<INFO> 
	extends ITextHoverExtension, IInformationProviderExtension2
{
	
	@SuppressWarnings("unused")
	/**
	 * @param editor the editor for this hover, non-null
	 */
	default INFO getHoverInfo(AbstractLangEditor editor, IRegion hoverRegion, boolean canSaveEditor) {
		assertNotNull(editor);
		return getHoverInfo(editor, hoverRegion);
	}
	
	INFO getHoverInfo(AbstractLangEditor editor, IRegion hoverRegion);
	
	@Override
	IInformationControlCreator getHoverControlCreator();
	
	@Override
	IInformationControlCreator getInformationPresenterControlCreator();
	
}