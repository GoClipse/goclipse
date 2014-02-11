package melnorme.lang.ide.debug.ui;

import melnorme.lang.ide.ui.editors.ILangEditorTextHover;

import org.eclipse.cdt.ui.text.c.hover.ICEditorTextHover;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextHoverExtension2;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.IEditorPart;

/**
 * Common debug text hover delegating to debugger specific implementations
 * based on active debug context.
 *
 * @since 7.0
 */
//BM: adapted to ILangEditorTextHover
// TODO: try to find a way to restrict the hover to relevant launches only?
public class DelegatingDebugTextHover implements ILangEditorTextHover<Object>, ITextHoverExtension, ITextHoverExtension2 {

    private IEditorPart fEditor;
	private ICEditorTextHover fDelegate;

	/*
	 * @see org.eclipse.jface.text.ITextHover#getHoverRegion(org.eclipse.jface.text.ITextViewer, int)
	 */
	@Override
	public IRegion getHoverRegion(ITextViewer viewer, int offset) {
		fDelegate = getDelegate();
		if (fDelegate != null) {
	        return fDelegate.getHoverRegion(viewer, offset);
		}
		return null;
	}
	
    /*
     * @see org.eclipse.jface.text.ITextHover#getHoverInfo(org.eclipse.jface.text.ITextViewer, org.eclipse.jface.text.IRegion)
     */
    @Override
	@SuppressWarnings("deprecation")
    public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
        fDelegate = getDelegate();
        if (fDelegate != null) {
            return fDelegate.getHoverInfo(textViewer, hoverRegion);
        }
        return null;
    }

    /*
     * @see org.eclipse.jface.text.ITextHoverExtension2#getHoverInfo2(org.eclipse.jface.text.ITextViewer, org.eclipse.jface.text.IRegion)
     */
    @Override
	@SuppressWarnings("deprecation")
    public Object getHoverInfo2(ITextViewer textViewer, IRegion hoverRegion) {
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
	public Object getHoverInfo2_do(ITextViewer textViewer, IRegion hoverRegion) {
		return getHoverInfo2(textViewer, hoverRegion);
	}

    /*
     * @see org.eclipse.jface.text.ITextHoverExtension#getHoverControlCreator()
     */
    @Override
	public IInformationControlCreator getHoverControlCreator() {
        if (fDelegate instanceof ITextHoverExtension) {
            return ((ITextHoverExtension) fDelegate).getHoverControlCreator();
        }
        return null;
    }

    /*
     * @see org.eclipse.cdt.ui.text.c.hover.ICEditorTextHover#setEditor(org.eclipse.ui.IEditorPart)
     */
    @Override
	public final void setEditor(IEditorPart editor) {
        fEditor = editor;
    }

    private ICEditorTextHover getDelegate() {
        IAdaptable context = DebugUITools.getDebugContext();
        if (context != null) {
            ICEditorTextHover hover = (ICEditorTextHover) context.getAdapter(ICEditorTextHover.class);
            if (hover != null) {
                hover.setEditor(fEditor);
            }
            return hover;
        }
        return null;
    }

}