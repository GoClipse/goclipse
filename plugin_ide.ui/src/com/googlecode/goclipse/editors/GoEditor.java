package com.googlecode.goclipse.editors;

import melnorme.lang.ide.ui.EditorSettings_Actual.EditorPrefConstants;
import melnorme.lang.ide.ui.editor.AbstractLangEditor;
import melnorme.lang.ide.ui.editor.text.LangPairMatcher;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

public class GoEditor extends AbstractLangEditor {
	
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
	
	/* ----------------- need to review/cleanup rest of this code ----------------- */
	
	// make public accesss
	@Override
	protected void setTitleImage(Image image) {
		super.setTitleImage(image);
	}
	
	@Override
	public Object getAdapter(Class required) {
		if (IContentOutlinePage.class.equals(required)) {
			if (outlinePage == null) {
				outlinePage = new GoEditorOutlinePage(getDocumentProvider(), this);
			}
			
			return outlinePage;
		}
		
		return super.getAdapter(required);
	}
	
	@Override
	protected final void doSetInput(IEditorInput input) throws CoreException {
		super.doSetInput(input);
		
		if (input instanceof IFileEditorInput) {
			imageUpdater = new EditorImageUpdater(this);
		}
	}
	
	
	@Override
	protected void createActions() {
		super.createActions();
		
		IAction action;
		
		action = new ToggleCommentAction("ToggleComment.", this);
		action.setActionDefinitionId("com.googlecode.goclipse.actions.ToggleComment");
		setAction("ToggleComment", action);
		markAsStateDependentAction("ToggleComment", true);
		configureToggleCommentAction();
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
	
	private void configureToggleCommentAction() {
		IAction action = getAction("ToggleComment");
		
		if (action instanceof ToggleCommentAction) {
			ISourceViewer sourceViewer = getSourceViewer();
			SourceViewerConfiguration configuration = getSourceViewerConfiguration();
			((ToggleCommentAction) action).configure(sourceViewer, configuration);
		}
	}
	
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