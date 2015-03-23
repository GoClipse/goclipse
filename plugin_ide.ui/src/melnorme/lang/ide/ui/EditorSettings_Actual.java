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

import melnorme.lang.ide.ui.editor.AbstractLangEditor;
import melnorme.lang.ide.ui.editor.LangEditorContextMenuContributor;

import org.eclipse.cdt.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.services.IServiceLocator;

import LANG_PROJECT_ID.ide.ui.editor.LANGUAGE_Editor;
import LANG_PROJECT_ID.ide.ui.editor.LANGUAGE_SimpleSourceViewerConfiguration;
import LANG_PROJECT_ID.ide.ui.editor.LANGUAGE_SourceViewerConfiguration;

public class EditorSettings_Actual {
	
	public static final String EDITOR_ID = LangUIPlugin.PLUGIN_ID + ".Editors.SourceEditor";
	public static final String EDITOR_CONTEXT_ID = LangUIPlugin.PLUGIN_ID + ".Contexts.Editor";
	
	public static final String EDITOR_CODE_TARGET = LangUIPlugin.PLUGIN_ID + ".Editor.HyperlinkCodeTarget";
	
	public static LANGUAGE_SourceViewerConfiguration createSourceViewerConfiguration(
			IPreferenceStore preferenceStore, AbstractLangEditor editor) {
		IColorManager colorManager = LangUIPlugin.getInstance().getColorManager();
		return new LANGUAGE_SourceViewerConfiguration(preferenceStore, colorManager, editor);
	}
	
	public static LANGUAGE_SimpleSourceViewerConfiguration createSimpleSourceViewerConfiguration(
			IPreferenceStore preferenceStore, IColorManager colorManager) {
		return new LANGUAGE_SimpleSourceViewerConfiguration(preferenceStore, colorManager);
	}
	
	public static Class<LANGUAGE_Editor> editorKlass() {
		return LANGUAGE_Editor.class;
	}
	
	/* ----------------- actions ----------------- */
	
	public static interface EditorCommandIds {
		
		public static final String OpenDef_ID = LangUIPlugin.PLUGIN_ID + ".commands.openDefinition";
		
		public static final String GoToMatchingBracket = LangUIPlugin.PLUGIN_ID + ".commands.GoToMatchingBracket";
		public static final String ToggleComment = LangUIPlugin.PLUGIN_ID + ".commands.ToggleComment";
		
	}
	
	public static LangEditorContextMenuContributor createCommandsContribHelper(IServiceLocator svcLocator) {
		return new LangEditorContextMenuContributor(svcLocator) { };
	}
	
}