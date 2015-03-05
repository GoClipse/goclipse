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

import melnorme.lang.ide.ui.EditorSettings_Actual;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.TextSettings_Actual;
import melnorme.lang.ide.ui.text.AbstractLangSourceViewerConfiguration;
import melnorme.utilbox.misc.ArrayUtil;

import org.eclipse.cdt.internal.ui.editor.EclipsePreferencesAdapter;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;

public abstract class AbstractLangEditor extends TextEditor {
	
	public AbstractLangEditor() {
		super();
	}
	
	@Override
	protected void initializeKeyBindingScopes() {
//		super.initializeKeyBindingScopes();
		setKeyBindingScopes(array(EditorSettings_Actual.EDITOR_CONTEXT_ID));
	}
	
	@Override
	protected void initializeEditor() {
		super.initializeEditor();
		initialize_setContextMenuIds();
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
	
	protected AbstractLangSourceViewerConfiguration createSourceViewerConfiguration() {
		return TextSettings_Actual.createSourceViewerConfiguration(getPreferenceStore(), this);
	}
	
	protected AbstractLangSourceViewerConfiguration getSourceViewerConfiguration2() {
		return (AbstractLangSourceViewerConfiguration) getSourceViewerConfiguration(); 
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
		getSourceViewerConfiguration2().handlePropertyChangeEvent(event);
		
		super.handlePreferenceStoreChanged(event);
	}
	
	@Override
	protected boolean affectsTextPresentation(PropertyChangeEvent event) {
		return getSourceViewerConfiguration2().affectsTextPresentation(event)
				|| super.affectsTextPresentation(event);
	}
	
	
	/* ----------------- create controls ----------------- */
	
	@Override
	protected SourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
		ISourceViewer sourceViewer = super.createSourceViewer(parent, ruler, styles);
		return assertInstance(sourceViewer, SourceViewer.class);
	}
	
	public SourceViewer getSourceViewer_() {
		return (SourceViewer) getSourceViewer();
	}
	
	/* ----------------- actions ----------------- */
	
	protected void initialize_setContextMenuIds() {
		setEditorContextMenuId(LangUIPlugin_Actual.EDITOR_CONTEXT);
		setRulerContextMenuId(LangUIPlugin_Actual.RULER_CONTEXT);
	}
	
	protected AbstractLangEditorActions editorActionsManager;
	
	@Override
	protected void createActions() {
		super.createActions();
		
		editorActionsManager = createActionsManager();
	}
	
	protected abstract AbstractLangEditorActions createActionsManager();
	
	@Override
	protected void editorContextMenuAboutToShow(IMenuManager menu) {
		super.editorContextMenuAboutToShow(menu);
		editorContextMenuAboutToShow_extend(menu);
	}
	
	protected void editorContextMenuAboutToShow_extend(IMenuManager menu) {
		editorActionsManager.editorContextMenuAboutToShow(menu);
	}
	
}