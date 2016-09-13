/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.editor;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.array;
import static melnorme.utilbox.core.CoreUtil.assertInstance;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.ToolchainPreferences;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.ui.EditorSettings_Actual;
import melnorme.lang.ide.ui.EditorSettings_Actual.EditorPrefConstants;
import melnorme.lang.ide.ui.LangImages;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.editor.actions.AbstractEditorToolOperation;
import melnorme.lang.ide.ui.editor.actions.GotoMatchingBracketManager;
import melnorme.lang.ide.ui.editor.text.LangPairMatcher;
import melnorme.lang.ide.ui.text.AbstractLangSourceViewerConfiguration;
import melnorme.lang.ide.ui.utils.PluginImagesHelper.ImageHandle;
import melnorme.lang.ide.ui.utils.prefs.EclipsePreferencesAdapter;
import melnorme.lang.utils.EnablementCounter;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.misc.ArrayUtil;
import melnorme.utilbox.ownership.Disposable;

public abstract class AbstractLangEditor extends TextEditorExt {
	
	public AbstractLangEditor() {
		super();
		
		// Force start debug plugin to ensure action contributions are propagated. 
		// XXX: Definitely there should be a better way to achieve the above, but it's tricky.
		LangUIPlugin.startDebugPlugin();
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
		
		if(input == null) {
			// Do nothing, editor will be closed.
			LangCore.logError("doSetInput = null");
			return;
		}
		
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
		LangUIPlugin_Actual.createDocumentSetupHelper().setupPartitioningIfNotSet(doc);
		
		internalDoSetInput(input);
	}
	
	protected void changePreferenceStore(IPreferenceStore store) {
		super.setPreferenceStore(store);
		setSourceViewerConfiguration(createSourceViewerConfiguration());
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
			LangSourceViewer sourceViewer = getSourceViewer_();
			langSVC.handlePropertyChange(event, getPreferenceStore(), sourceViewer);
		}
		
		super.handlePreferenceStoreChanged(event);
	}
	
	@Override
	protected boolean affectsTextPresentation(PropertyChangeEvent event) {
		return super.affectsTextPresentation(event);
	}
	
	protected void internalDoSetInput(IEditorInput input) {
		assertNotNull(input);
		editorTitleImageUpdater.updateEditorImage(EditorUtils.getAssociatedFile(input));
	}
	
	/* ----------------- create controls ----------------- */
	
	@Override
	public int getOrientation() {
		return SWT.LEFT_TO_RIGHT; // Always left to right, 
		// also, this fixes bug super.getOrientation(), which can return SWT.NONE (but shouldn't)
	}
	
	@Override
	protected final ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
		LangSourceViewer viewer = doCreateSourceViewer(parent, ruler, styles);
		assertInstance(viewer, SourceViewer.class);
		assertInstance(viewer, ISourceViewerExt.class);
		return viewer;
	}
	
	public LangSourceViewer getSourceViewer_() {
		return (LangSourceViewer) getSourceViewer();
	}
	public ISourceViewerExt getSourceViewer_asExt() {
		return (ISourceViewerExt) getSourceViewer();
	}
	
	protected LangSourceViewer doCreateSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
		LangSourceViewer viewer = 
				new LangSourceViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);
		// ensure decoration support has been created and configured.
		getSourceViewerDecorationSupport(viewer);
		return viewer;
	}
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		
		editorSelChangedListener.install(getSelectionProvider());
	}
	
	/* ----------------- other ----------------- */
	
	protected final EditorTitleImageUpdater editorTitleImageUpdater = addOwned(new EditorTitleImageUpdater(this));
	
	public ImageHandle getBaseEditorImage() {
		return LangImages.SOURCE_EDITOR_ICON;
	}
	
	@Override
	protected boolean isTabsToSpacesConversionEnabled() {
		return false;
	}
	
	protected final AbstractSelectionChangedListener editorSelChangedListener = new AbstractSelectionChangedListener() {
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			handleEditorSelectionChanged(event);
		}
		
	};
	
	@SuppressWarnings("unused")
	protected void handleEditorSelectionChanged(SelectionChangedEvent event) {
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
			EditorPrefConstants.MATCHING_BRACKETS_.key, 
			EditorPrefConstants.MATCHING_BRACKETS_COLOR2.getActiveKey(), 
			EditorPrefConstants.HIGHLIGHT_BRACKET_AT_CARET_LOCATION, 
			EditorPrefConstants.ENCLOSING_BRACKETS);
	}
	
	/* ----------------- save ----------------- */
	
	/** Whether save actions are enabled or not. */
	protected final EnablementCounter saveActionsEnablement = new EnablementCounter();
	
	public EnablementCounter saveActionsEnablement() {
		return saveActionsEnablement;
	}
	
	public void saveWithoutSaveActions2(ICancelMonitor cm) {
		saveWithoutSaveActions(EclipseUtils.pm(cm));
	}
	
	public void saveWithoutSaveActions(IProgressMonitor pm) {
		try(Disposable disposable = saveActionsEnablement().enterDisable()) {
			doSave(pm);
		}
	}
	
	@Override
	protected void performSave(boolean overwrite, IProgressMonitor pm) {
		
		if(saveActionsEnablement.isEnabled()) {
			IProject associatedProject = EditorUtils.getAssociatedProject(getEditorInput());
			if(ToolchainPreferences.FORMAT_ON_SAVE.getEffectiveValue(associatedProject)) {
				AbstractEditorToolOperation<?> formatOperation = LangUIPlugin_Actual.getFormatOperation(this);
				formatOperation.handleSoftFailureWithDialog = false;
				formatOperation.executeAndHandle();
			}
		}
		
		super.performSave(overwrite, pm);
	}
	
}