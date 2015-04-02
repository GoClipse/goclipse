/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.editor;


import static melnorme.utilbox.core.CoreUtil.array;
import static melnorme.utilbox.core.CoreUtil.assertInstance;

import java.util.ArrayList;
import java.util.List;

import melnorme.lang.ide.core.TextSettings_Actual;
import melnorme.lang.ide.ui.EditorSettings_Actual;
import melnorme.lang.ide.ui.EditorSettings_Actual.EditorPrefConstants;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.editor.actions.GotoMatchingBracketManager;
import melnorme.lang.ide.ui.editor.text.LangPairMatcher;
import melnorme.lang.ide.ui.text.AbstractLangSourceViewerConfiguration;
import melnorme.utilbox.misc.ArrayUtil;

import org.eclipse.cdt.internal.ui.editor.EclipsePreferencesAdapter;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

public abstract class AbstractLangEditor extends TextEditorExt {
	
	public AbstractLangEditor() {
		super();
	}
	
	@Override
	protected void initializeEditor() {
		super.initializeEditor();
		initialize_setContextMenuIds();
	}
	
	/* ----------------- actions init ----------------- */
	
	@Override
	protected void initializeKeyBindingScopes() {
		setKeyBindingScopes(array(EditorSettings_Actual.EDITOR_CONTEXT_ID));
	}
	
	protected void initialize_setContextMenuIds() {
		setEditorContextMenuId(LangUIPlugin_Actual.EDITOR_CONTEXT);
		setRulerContextMenuId(LangUIPlugin_Actual.RULER_CONTEXT);
	}
	
	/* ----------------- input ----------------- */
	
	@Override
	protected void doSetInput(IEditorInput input) throws CoreException {
		super.doSetInput(input);
		
		SourceViewer sourceViewer = getSourceViewer_();
		
		if(sourceViewer == null) {
			changePreferenceStore(createCombinedPreferenceStore(input));
		} else {
			getSourceViewerDecorationSupport(sourceViewer).uninstall();
			sourceViewer.unconfigure();
			
			changePreferenceStore(createCombinedPreferenceStore(input));
			
			sourceViewer.configure(getSourceViewerConfiguration());
			getSourceViewerDecorationSupport(sourceViewer).install(getPreferenceStore());
		}
		
		IDocument doc = getDocumentProvider().getDocument(input);
		// Setup up partitioning if not set. It can happen if opening non-language files in the language editor.
		TextSettings_Actual.createDocumentSetupHelper().setupPartitioningIfNotSet(doc);
		
		internalDoSetInput(input);
	}
	
	protected void changePreferenceStore(IPreferenceStore store) {
		super.setPreferenceStore(store);
		setSourceViewerConfiguration(createSourceViewerConfiguration());
	}
	
	@SuppressWarnings("unused")
	protected void internalDoSetInput(IEditorInput input) {
	}
	
	protected abstract AbstractLangSourceViewerConfiguration createSourceViewerConfiguration();
	
	public AbstractLangSourceViewerConfiguration getSourceViewerConfiguration_asLang() {
		SourceViewerConfiguration svc = getSourceViewerConfiguration(); 
		if(svc instanceof AbstractLangSourceViewerConfiguration) {
			return (AbstractLangSourceViewerConfiguration) svc;
		}
		return null;
	}
	
	protected IPreferenceStore createCombinedPreferenceStore(IEditorInput input) {
		List<IPreferenceStore> stores = new ArrayList<IPreferenceStore>(4);
		
		IProject project = EditorUtils.getAssociatedProject(input);
		if (project != null) {
			stores.add(new EclipsePreferencesAdapter(new ProjectScope(project), LangUIPlugin.PLUGIN_ID));
		}
		
		stores.add(LangUIPlugin.getInstance().getPreferenceStore());
		stores.add(LangUIPlugin.getInstance().getCorePreferenceStore());
		
		alterCombinedPreferenceStores_beforeEditorsUI(stores);
		stores.add(EditorsUI.getPreferenceStore());
		
		return new ChainedPreferenceStore(ArrayUtil.createFrom(stores, IPreferenceStore.class));
	}
	
	@SuppressWarnings("unused")
	protected void alterCombinedPreferenceStores_beforeEditorsUI(List<IPreferenceStore> stores) {
	}
	
	@Override
	protected void handlePreferenceStoreChanged(PropertyChangeEvent event) {
		AbstractLangSourceViewerConfiguration langSVC = getSourceViewerConfiguration_asLang();
		if(langSVC != null) {
			langSVC.handlePropertyChangeEvent(event);
		}
		
		getSourceViewer_asExt().handlePropertyChangeEvent_2(event, getPreferenceStore());
		
		super.handlePreferenceStoreChanged(event);
	}
	
	@Override
	protected boolean affectsTextPresentation(PropertyChangeEvent event) {
		AbstractLangSourceViewerConfiguration langSVC = getSourceViewerConfiguration_asLang();
		if(langSVC != null) {
			return langSVC.affectsTextPresentation(event) || super.affectsTextPresentation(event);
		}
		
		return super.affectsTextPresentation(event);
	}
	
	
	/* ----------------- create controls ----------------- */
	
	@Override
	protected final ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
		SourceViewer viewer = doCreateSourceViewer(parent, ruler, styles);
		assertInstance(viewer, SourceViewer.class);
		assertInstance(viewer, ISourceViewerExt.class);
		return viewer;
	}
	
	public SourceViewer getSourceViewer_() {
		return (SourceViewer) getSourceViewer();
	}
	public ISourceViewerExt getSourceViewer_asExt() {
		return (ISourceViewerExt) getSourceViewer();
	}
	
	protected SourceViewer doCreateSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
		SourceViewer viewer= new SourceViewerExt(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);
		// ensure decoration support has been created and configured.
		getSourceViewerDecorationSupport(viewer);
		return viewer;
	}
	
	/* ----------------- Bracket matcher ----------------- */
	
	protected final LangPairMatcher fBracketMatcher = addOwned(init_createBracketMatcher());
	protected final GotoMatchingBracketManager gotoMatchingBracketManager = init_createGoToMatchingBracketManager();
	
	protected abstract LangPairMatcher init_createBracketMatcher();
	
	protected GotoMatchingBracketManager init_createGoToMatchingBracketManager() {
		return new GotoMatchingBracketManager(this);
	}
	
	public final LangPairMatcher getBracketMatcher() {
		return fBracketMatcher;
	}
	
	public final GotoMatchingBracketManager getGotoMatchingBracketManager() {
		return gotoMatchingBracketManager;
	}
	
	@Override
	protected void configureSourceViewerDecorationSupport(SourceViewerDecorationSupport support) {
		configureBracketMatcher(support);
		super.configureSourceViewerDecorationSupport(support);
	}
	
	protected void configureBracketMatcher(SourceViewerDecorationSupport support) {
		support.setCharacterPairMatcher(fBracketMatcher);
		support.setMatchingCharacterPainterPreferenceKeys(
			EditorPrefConstants.MATCHING_BRACKETS, 
			EditorPrefConstants.MATCHING_BRACKETS_COLOR, 
			EditorPrefConstants.HIGHLIGHT_BRACKET_AT_CARET_LOCATION, 
			EditorPrefConstants.ENCLOSING_BRACKETS);
	}
	
}