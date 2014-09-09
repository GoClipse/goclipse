/*******************************************************************************
 * Copyright (c) 2014, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package LANG_PROJECT_ID.ide.ui.editor;

import static melnorme.utilbox.core.CoreUtil.array;

import org.eclipse.cdt.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.source.ISourceViewer;

import LANG_PROJECT_ID.ide.ui.text.LANGUAGE_Scanner;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.editor.BestMatchHover;
import melnorme.lang.ide.ui.text.AbstractLangSourceViewerConfiguration;

public class LANGUAGE_SourceViewerConfiguration extends AbstractLangSourceViewerConfiguration {
	
	public LANGUAGE_SourceViewerConfiguration(IPreferenceStore preferenceStore, IColorManager colorManager) {
		super(preferenceStore, colorManager);
	}
	
	@Override
	public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
		return LangUIPlugin_Actual.LANG_PARTITIONING;
	}
	
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return LangUIPlugin_Actual.PARTITION_TYPES;
	}
	
	@Override
	protected void createScanners() {
		addScanner(new LANGUAGE_Scanner(getTokenStoreFactory()), IDocument.DEFAULT_CONTENT_TYPE);
	}
	
	// TODO:
//	@Override
//	public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType, int stateMask) {
//		if(contentType.equals(IDocument.DEFAULT_CONTENT_TYPE)) {
//			return new BestMatchHover(editor, stateMask);
//		}
//		return null;
//	}
	
	@Override
	public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
		if(IDocument.DEFAULT_CONTENT_TYPE.equals(contentType)) {
			return array(new LANGUAGE_AutoEditStrategy(LangUIPlugin.getPrefStore(), contentType, sourceViewer));
		} else {
			return super.getAutoEditStrategies(sourceViewer, contentType);
		}
	}
	
}