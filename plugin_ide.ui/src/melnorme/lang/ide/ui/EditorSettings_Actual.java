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
package melnorme.lang.ide.ui;

import static melnorme.utilbox.core.CoreUtil.array;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.services.IServiceLocator;

import com.googlecode.goclipse.ui.editor.GoEditor;
import com.googlecode.goclipse.ui.editor.actions.GoEditorContextMenuContributor;
import com.googlecode.goclipse.ui.text.GoColorPreferences;

import melnorme.lang.ide.core.TextSettings_Actual.LangPartitionTypes;
import melnorme.lang.ide.ui.editor.LangEditorContextMenuContributor;
import melnorme.lang.ide.ui.editor.text.EditorPrefConstants_Common;
import melnorme.lang.ide.ui.text.SimpleSourceViewerConfiguration;
import melnorme.lang.ide.ui.text.coloring.StylingPreferences;
import melnorme.lang.ide.ui.text.coloring.ThemedTextStylingPreference;
import melnorme.util.swt.jface.text.ColorManager2;

public class EditorSettings_Actual {
	
	public static final String EDITOR_ID = "com.googlecode.goclipse.editors.Editor";
	public static final String EDITOR_CONTEXT_ID = LangUIPlugin.PLUGIN_ID + ".Contexts.Editor";
	
	public static final String EDITOR_CODE_TARGET = LangUIPlugin.PLUGIN_ID + ".Editor.CodeTarget";
	
	public static Class<GoEditor> editorKlass() {
		return GoEditor.class;
	}
	
	public static interface EditorPrefConstants extends EditorPrefConstants_Common {
		
	}
	
	public static StylingPreferences getStylingPreferences() {
		return new StylingPreferences(
			GoColorPreferences.DEFAULT,
			
			GoColorPreferences.COMMENT,
			
			GoColorPreferences.KEYWORD,
			GoColorPreferences.KW_NATIVE_TYPES,
			GoColorPreferences.KW_LITERAL,
			GoColorPreferences.BUILTIN_FUNCTION,
			GoColorPreferences.OPERATOR,
			GoColorPreferences.STRUCTURAL_SYMBOLS,
			
			GoColorPreferences.NUMBER,
			GoColorPreferences.CHARACTER,
			GoColorPreferences.STRING,
			GoColorPreferences.MULTILINE_STRING
		);
	}
	
	public static final String TEMPLATE_CONTEXT_TYPE_ID = LangUIPlugin.PLUGIN_ID + ".TemplateContextType";
	
	public static final ThemedTextStylingPreference CODE_DEFAULT_COLOR = GoColorPreferences.DEFAULT;
	
	public static SourceViewerConfiguration createTemplateEditorSourceViewerConfiguration(
			IPreferenceStore store, final IContentAssistProcessor templateCAP) {
		ColorManager2 colorManager = LangUIPlugin.getInstance().getColorManager();
		return new SimpleSourceViewerConfiguration(store, colorManager) {
			@Override
			public ContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
				return setupSimpleContentAssistant(templateCAP, array(
					LangPartitionTypes.CODE.getId(), 
					LangPartitionTypes.LINE_COMMENT.getId(), 
					LangPartitionTypes.STRING.getId(),
					LangPartitionTypes.BLOCK_COMMENT.getId() 
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
		return new GoEditorContextMenuContributor(svcLocator);
	}
	
}
