package melnorme.lang.ide.debug.ui;

import java.util.Optional;

import org.eclipse.cdt.ui.text.c.hover.ICEditorTextHover;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextHoverExtension2;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.texteditor.ITextEditor;

import melnorme.lang.ide.ui.editor.hover.ILangEditorTextHover;
import melnorme.lang.tooling.common.ISourceBuffer;

/**
 * Common debug text hover delegating to debugger specific implementations
 * based on active debug context.
 *
 * @since 7.0
 */
//BM: adapted to ILangEditorTextHover
// TODO: try to find a way to restrict the hover to relevant launches only?
public class DelegatingDebugTextHover
implements ILangEditorTextHover<Object>{

    private ITextEditor fEditor;
	private ICEditorTextHover fDelegate;

//	@Override
//	public IRegion getHoverRegion(ITextViewer viewer, int offset) {
//		fDelegate = getDelegate();
//		if (fDelegate != null) {
//	        return fDelegate.getHoverRegion(viewer, offset);
//		}
//		return null;
//	}
	
//    @Override
//	@SuppressWarnings("deprecation")
//    public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
//        fDelegate = getDelegate();
//        if (fDelegate != null) {
//            return fDelegate.getHoverInfo(textViewer, hoverRegion);
//        }
//        return null;
//    }
	
	@Override
	public Object getHoverInfo(ISourceBuffer sourceBuffer, IRegion hoverRegion, Optional<ITextEditor> editor,
			ITextViewer textViewer) {
		fEditor = editor.orElse(null);		
		return getHoverInfo(sourceBuffer, hoverRegion, textViewer);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public Object getHoverInfo(ISourceBuffer sourceBuffer, IRegion hoverRegion, ITextViewer textViewer) {
        
		fDelegate = getDelegate();
        if (fDelegate instanceof ITextHoverExtension2) {
			return ((ITextHoverExtension2) fDelegate).getHoverInfo2(textViewer, hoverRegion);
        }
        // fall back to legacy method
        if (fDelegate != null) {
            return fDelegate.getHoverInfo(textViewer, hoverRegion);
        }
        return null;
    }
    
    @Override
	public IInformationControlCreator getHoverControlCreator() {
        if (fDelegate instanceof ITextHoverExtension) {
            return ((ITextHoverExtension) fDelegate).getHoverControlCreator();
        }
        return null;
    }
    
    @Override
    public IInformationControlCreator getInformationPresenterControlCreator() {
    	// The debug hover cannot be opened with getInformationPresenterControlCreator
        return null;
    }
    
    private ICEditorTextHover getDelegate() {
        IAdaptable context = DebugUITools.getDebugContext();
        if (context != null) {
            ICEditorTextHover hover = context.getAdapter(ICEditorTextHover.class);
            if (hover != null) {
                hover.setEditor(fEditor);
            }
            return hover;
        }
        return null;
    }

}