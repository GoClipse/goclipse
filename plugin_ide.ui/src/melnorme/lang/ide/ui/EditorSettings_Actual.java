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

import melnorme.lang.ide.ui.editor.LangEditorContextMenuContributor;
import melnorme.lang.ide.ui.editor.text.EditorPrefConstants_Common;

import org.eclipse.cdt.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.services.IServiceLocator;

import com.googlecode.goclipse.editors.GoEditor;
import com.googlecode.goclipse.ui.editor.GoSimpleSourceViewerConfiguration;
import com.googlecode.goclipse.ui.editor.actions.GoEditorContextMenuContributor;
import com.googlecode.goclipse.ui.text.GoColorPreferences;

public class EditorSettings_Actual {
	
	public static final String EDITOR_ID = "com.googlecode.goclipse.editors.Editor";
	public static final String EDITOR_CONTEXT_ID = LangUIPlugin.PLUGIN_ID + ".Contexts.Editor";
	
	public static final String EDITOR_CODE_TARGET = LangUIPlugin.PLUGIN_ID + ".Editor.CodeTarget";
	
	public static GoSimpleSourceViewerConfiguration createSimpleSourceViewerConfiguration(
			IPreferenceStore preferenceStore, IColorManager colorManager) {
		return new GoSimpleSourceViewerConfiguration(preferenceStore, colorManager, null);
	}
	
	public static Class<GoEditor> editorKlass() {
		return GoEditor.class;
	}
	
	public static interface EditorPrefConstants extends EditorPrefConstants_Common {
		
	}
	
	public static final String TEMPLATE_CONTEXT_TYPE_ID = LangUIPlugin.PLUGIN_ID + ".TemplateContextType";
	
	public static final String CODE_DEFAULT_COLOR = GoColorPreferences.SYNTAX_COLORING__TEXT.key;
	
	/* ----------------- actions ----------------- */
	
	public static interface EditorCommandIds {
		
		public static final String OpenDef_ID = LangUIPlugin.PLUGIN_ID + ".commands.openDefinition";
		
		public static final String GoToMatchingBracket = LangUIPlugin.PLUGIN_ID + ".commands.GoToMatchingBracket";
		public static final String ToggleComment = LangUIPlugin.PLUGIN_ID + ".commands.ToggleComment";
		
	}
	
	public static LangEditorContextMenuContributor createCommandsContribHelper(IServiceLocator svcLocator) {
		return new GoEditorContextMenuContributor(svcLocator);
	}
	
}
