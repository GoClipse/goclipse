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
package melnorme.lang.ide.ui;

import static melnorme.utilbox.core.CoreUtil.array;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.services.IServiceLocator;

import LANG_PROJECT_ID.ide.ui.editor.LANGUAGE_Editor;
import LANG_PROJECT_ID.ide.ui.text.LANGUAGE_ColorPreferences;
import melnorme.lang.ide.core.TextSettings_Actual.LangPartitionTypes;
import melnorme.lang.ide.ui.editor.LangEditorContextMenuContributor;
import melnorme.lang.ide.ui.editor.text.EditorPrefConstants_Common;
import melnorme.lang.ide.ui.text.SimpleSourceViewerConfiguration;
import melnorme.lang.ide.ui.text.coloring.StylingPreferences;
import melnorme.lang.ide.ui.text.coloring.ThemedTextStylingPreference;

public class EditorSettings_Actual {
	
	public static final String EDITOR_ID = LangUIPlugin.PLUGIN_ID + ".Editors.SourceEditor";
	public static final String EDITOR_CONTEXT_ID = LangUIPlugin.PLUGIN_ID + ".Contexts.Editor";
	
	public static final String EDITOR_CODE_TARGET = LangUIPlugin.PLUGIN_ID + ".Editor.HyperlinkCodeTarget";
	
	public static Class<LANGUAGE_Editor> editorKlass() {
		return LANGUAGE_Editor.class;
	}
	
	public static interface EditorPrefConstants extends EditorPrefConstants_Common {
		
	}
	
	public static StylingPreferences getStylingPreferences() {
		return new StylingPreferences(
			LANGUAGE_ColorPreferences.COMMENTS,
			LANGUAGE_ColorPreferences.DOC_COMMENTS,
			LANGUAGE_ColorPreferences.STRINGS,
			LANGUAGE_ColorPreferences.CHARACTER,
			
			LANGUAGE_ColorPreferences.DEFAULT,
			LANGUAGE_ColorPreferences.KEYWORDS,
			LANGUAGE_ColorPreferences.KEYWORDS_VALUES
		);
	}
	
	public static final String TEMPLATE_CONTEXT_TYPE_ID = LangUIPlugin.PLUGIN_ID + ".TemplateContextType";
	
	public static final ThemedTextStylingPreference CODE_DEFAULT_COLOR = LANGUAGE_ColorPreferences.DEFAULT;
	
	public static SourceViewerConfiguration createTemplateEditorSourceViewerConfiguration(
			IPreferenceStore store, final IContentAssistProcessor templateCAP) {
		return new SimpleSourceViewerConfiguration(store) {
			@Override
			public ContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
				return setupSimpleContentAssistant(templateCAP, array(
					LangPartitionTypes.CODE.getId(), 
					LangPartitionTypes.LINE_COMMENT.getId(), 
					LangPartitionTypes.BLOCK_COMMENT.getId(), 
					LangPartitionTypes.DOC_LINE_COMMENT.getId(),
					LangPartitionTypes.DOC_BLOCK_COMMENT.getId() 
				));
			}
		};
	}
	
	/* ----------------- actions ----------------- */
	
	public static interface EditorCommandIds {
		
		public static final String OpenDef_ID = LangUIPlugin.PLUGIN_ID + ".commands.openDefinition";
		
		public static final String GoToMatchingBracket = LangUIPlugin.PLUGIN_ID + ".commands.GoToMatchingBracket";
		public static final String ToggleComment = LangUIPlugin.PLUGIN_ID + ".commands.ToggleComment";
		
		public static final String QuickOutline = LangUIPlugin.PLUGIN_ID + ".commands.QuickOutline";
		public static final String Format = LangUIPlugin.PLUGIN_ID + ".commands.Format";
		
	}
	
	public static LangEditorContextMenuContributor createCommandsContribHelper(IServiceLocator svcLocator) {
		return new LangEditorContextMenuContributor(svcLocator) { };
	}
	
}
