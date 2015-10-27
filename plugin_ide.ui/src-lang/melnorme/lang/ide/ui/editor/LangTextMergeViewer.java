/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.editor;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.contentmergeviewer.TextMergeViewer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.TextSettings_Actual;
import melnorme.lang.ide.ui.EditorSettings_Actual;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.text.LangSourceViewerConfiguration;
import melnorme.util.swt.jface.text.ColorManager2;

public class LangTextMergeViewer extends TextMergeViewer {
	
	public LangTextMergeViewer(Composite parent, CompareConfiguration configuration) {
		super(parent, configuration);
	}
	
	@Override
	protected SourceViewer createSourceViewer(Composite parent, int textOrientation) {
		int styles = textOrientation | SWT.H_SCROLL | SWT.V_SCROLL;
		return new LangSourceViewer(parent, new CompositeRuler(), styles);
	}
	
	@Override
	protected IDocumentPartitioner getDocumentPartitioner() {
		return TextSettings_Actual.createDocumentSetupHelper().createDocumentPartitioner();
	}
	
	@Override
	protected String getDocumentPartitioning() {
		return TextSettings_Actual.PARTITIONING_ID;
	}
	
	@Override
	protected void configureTextViewer(TextViewer textViewer) {
		if(textViewer instanceof SourceViewer) {
			SourceViewer sourceViewer = (SourceViewer) textViewer;
			sourceViewer.configure(getSourceViewerConfiguration());
		}
	}
	
	@Override
	public String getTitle() {
		return LangCore_Actual.LANGUAGE_NAME + LangEditorMessages.MSG__SOURCE_COMPARE;
	}
	
	public IPreferenceStore getPreferenceStore() {
		return LangUIPlugin.getDefault().getCombinedPreferenceStore();
	}
	
	protected SourceViewerConfiguration getSourceViewerConfiguration() {
		ColorManager2 colorManager = LangUIPlugin.getInstance().getColorManager();
		return new LangSourceViewerConfiguration(getPreferenceStore(), colorManager, null,
			EditorSettings_Actual.getStylingPreferences());
	}
	
}