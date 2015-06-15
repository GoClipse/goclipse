package com.googlecode.goclipse.editors;

import melnorme.lang.ide.ui.EditorSettings_Actual.EditorPrefConstants;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.editor.structure.AbstractLangStructureEditor;
import melnorme.lang.ide.ui.editor.text.LangPairMatcher;
import melnorme.lang.ide.ui.text.AbstractLangSourceViewerConfiguration;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

import _org.eclipse.cdt.ui.text.IColorManager;

import com.googlecode.goclipse.ui.editor.GoEditorSourceViewerConfiguration;

public class GoEditor extends AbstractLangStructureEditor {
	
	private EditorImageUpdater imageUpdater;
	private GoEditorOutlinePage outlinePage;
	
	public GoEditor() {
		super();
	}
	
	@Override
	protected LangPairMatcher init_createBracketMatcher() {
		return new LangPairMatcher("{}[]()".toCharArray());
	}
	
	@Override
	protected void configureBracketMatcher(SourceViewerDecorationSupport support) {
		support.setCharacterPairMatcher(fBracketMatcher);
		support.setMatchingCharacterPainterPreferenceKeys(
			EditorPrefConstants.MATCHING_BRACKETS, 
			EditorPrefConstants.MATCHING_BRACKETS_COLOR, 
			EditorPrefConstants.HIGHLIGHT_BRACKET_AT_CARET_LOCATION, 
			EditorPrefConstants.ENCLOSING_BRACKETS);
	}
	
	@Override
	protected AbstractLangSourceViewerConfiguration createSourceViewerConfiguration() {
		IColorManager colorManager = LangUIPlugin.getInstance().getColorManager();
		return new GoEditorSourceViewerConfiguration(getPreferenceStore(), colorManager, this);
	}
	
	/* ----------------- need to review/cleanup rest of this code ----------------- */
	
	@Override
	public Object getAdapter(Class required) {
//		if (IContentOutlinePage.class.equals(required)) {
//			if (outlinePage == null) {
//				outlinePage = new GoEditorOutlinePage(getDocumentProvider(), this);
//			}
//			
//			return outlinePage;
//		}
		
		return super.getAdapter(required);
	}
	
	@Override
	protected final void doSetInput(IEditorInput input) throws CoreException {
		super.doSetInput(input);
		
		if (input instanceof IFileEditorInput) {
			imageUpdater = new EditorImageUpdater(this);
		}
	}
	
	public String getText() {
		return getSourceViewer().getDocument().get();
	}
	
	public static void replaceText(ISourceViewer sourceViewer, String newText) {
		ISelection sel = sourceViewer.getSelectionProvider().getSelection();
		int topIndex = sourceViewer.getTopIndex();
		
		sourceViewer.getDocument().set(newText);
		
		if (sel != null) {
			sourceViewer.getSelectionProvider().setSelection(sel);
		}
		
		if (topIndex != -1) {
			sourceViewer.setTopIndex(topIndex);
		}
	}
	
	@Override
	public void dispose() {
		
		if (imageUpdater != null) {
			imageUpdater.dispose();
		}
		
		super.dispose();
	}
	
	@SuppressWarnings("unused")
	protected void handleReconcilation(IRegion partition) {
		if (outlinePage != null) {
			outlinePage.handleEditorReconcilation();
		}
	}
	
	public IProject getCurrentProject() {
		if (getEditorInput() instanceof IFileEditorInput) {
			IFileEditorInput input = (IFileEditorInput) getEditorInput();
			
			IFile file = input.getFile();
			
			if (file != null) {
				return file.getProject();
			}
		}
		
		return null;
	}
	
	@Override
	protected boolean isTabsToSpacesConversionEnabled() {
		return false;
	}
	
}