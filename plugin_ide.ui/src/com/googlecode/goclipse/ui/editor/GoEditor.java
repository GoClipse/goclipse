package com.googlecode.goclipse.ui.editor;

import melnorme.lang.ide.ui.EditorSettings_Actual.EditorPrefConstants;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.editor.structure.AbstractLangStructureEditor;
import melnorme.lang.ide.ui.editor.text.LangPairMatcher;
import melnorme.lang.ide.ui.text.AbstractLangSourceViewerConfiguration;

import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

import _org.eclipse.cdt.ui.text.IColorManager;

public class GoEditor extends AbstractLangStructureEditor {
	
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
	
}