package com.googlecode.goclipse.editors;

import java.util.ResourceBundle;

import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.editor.AbstractLangEditor;
import melnorme.lang.ide.ui.editor.AbstractLangEditorActions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.source.DefaultCharacterPairMatcher;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.googlecode.goclipse.ui.GoUIPreferenceConstants;
import com.googlecode.goclipse.ui.editor.GoEditorSourceViewerConfiguration;
import com.googlecode.goclipse.ui.editor.actions.GoEditorActions;

public class GoEditor extends AbstractLangEditor {
	
	private static final String BUNDLE_ID = "com.googlecode.goclipse.editors.GoEditorMessages";
	
	private static ResourceBundle editorResourceBundle = ResourceBundle.getBundle(BUNDLE_ID);
	
	private DefaultCharacterPairMatcher matcher;
	private EditorImageUpdater imageUpdater;
	
	private GoEditorOutlinePage outlinePage;
	
	public GoEditor() {
		super();
	}
	
	@Override
	protected GoEditorSourceViewerConfiguration createSourceViewerConfiguration() {
		return new GoEditorSourceViewerConfiguration(getPreferenceStore(), 
			LangUIPlugin.getInstance().getColorManager(), this);
	}
	
	
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
	protected void configureSourceViewerDecorationSupport(SourceViewerDecorationSupport support) {
		super.configureSourceViewerDecorationSupport(support);
		
		char[] matchChars = {'(', ')', '[', ']', '{', '}'}; //which brackets to match
		matcher = new DefaultCharacterPairMatcher(matchChars, IDocumentExtension3.DEFAULT_PARTITIONING);
		support.setCharacterPairMatcher(matcher);
		support.setMatchingCharacterPainterPreferenceKeys(
			GoUIPreferenceConstants.EDITOR_MATCHING_BRACKETS,
			GoUIPreferenceConstants.EDITOR_MATCHING_BRACKETS_COLOR);
	}
	
	@Override
	protected AbstractLangEditorActions createActionsManager() {
		return new GoEditorActions(this);
	}
	
	@Override
	protected void createActions() {
		super.createActions();
		
		IAction action;
		
		action = new ToggleCommentAction(editorResourceBundle, "ToggleComment.", this);
		action.setActionDefinitionId("com.googlecode.goclipse.actions.ToggleComment");
		setAction("ToggleComment", action);
		markAsStateDependentAction("ToggleComment", true);
		configureToggleCommentAction();
	}
	
	public DefaultCharacterPairMatcher getPairMatcher() {
		return matcher;
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