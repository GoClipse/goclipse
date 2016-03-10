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
package melnorme.lang.ide.ui.text;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;

import com.googlecode.goclipse.ui.GoUIPreferenceConstants;
import com.googlecode.goclipse.ui.editor.GocodeCompletionProposalComputer;
import com.googlecode.goclipse.ui.text.GoScanner;

import melnorme.lang.ide.core.TextSettings_Actual.LangPartitionTypes;
import melnorme.lang.ide.ui.editor.structure.AbstractLangStructureEditor;
import melnorme.lang.ide.ui.text.coloring.SingleTokenScanner;
import melnorme.lang.ide.ui.text.coloring.StylingPreferences;
import melnorme.lang.ide.ui.text.coloring.TokenRegistry;
import melnorme.lang.ide.ui.text.completion.ILangCompletionProposalComputer;
import melnorme.lang.ide.ui.text.completion.LangContentAssistProcessor.ContentAssistCategoriesBuilder;
import melnorme.lang.tooling.LANG_SPECIFIC;
import melnorme.util.swt.jface.text.ColorManager2;

@LANG_SPECIFIC
public class LangSourceViewerConfiguration extends AbstractLangSourceViewerConfiguration {
	
	public LangSourceViewerConfiguration(IPreferenceStore preferenceStore, ColorManager2 colorManager, 
			AbstractLangStructureEditor editor, StylingPreferences stylingPrefs) {
		super(preferenceStore, colorManager, stylingPrefs, editor);
	}
	
	@Override
	protected AbstractLangScanner createScannerFor(Display current, LangPartitionTypes partitionType,
			TokenRegistry tokenStore) {
		switch (partitionType) {
		case CODE:
			return new GoScanner(tokenStore);
					
		case LINE_COMMENT:
		case BLOCK_COMMENT:
			return new SingleTokenScanner(tokenStore, GoUIPreferenceConstants.COMMENT);
			
		case CHARACTER:
			return new SingleTokenScanner(tokenStore, GoUIPreferenceConstants.CHARACTER);
		case STRING:
			return new SingleTokenScanner(tokenStore, GoUIPreferenceConstants.STRING);
		case MULTILINE_STRING:
			return new SingleTokenScanner(tokenStore, GoUIPreferenceConstants.MULTILINE_STRING);
		}
		
		throw assertFail();
	}
	
	@Override
	protected String getToggleCommentPrefix() {
		return "//";
	}
	
	
	@Override
	protected ContentAssistCategoriesBuilder getContentAssistCategoriesProvider() {
		return new ContentAssistCategoriesBuilder() {
			
			@Override
			protected ILangCompletionProposalComputer createDefaultSymbolsProposalComputer() {
				return new GocodeCompletionProposalComputer();
			}
			
		};
	}
	
}