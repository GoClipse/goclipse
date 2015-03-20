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

import com.googlecode.goclipse.editors.GoEditor;
import com.googlecode.goclipse.ui.editor.GoEditorSourceViewerConfiguration;
import com.googlecode.goclipse.ui.editor.GoSimpleSourceViewerConfiguration;
import com.googlecode.goclipse.ui.editor.actions.GoEditorContextMenuContributor;

public class EditorSettings_Actual {
	
	public static final String EDITOR_ID = "com.googlecode.goclipse.editors.Editor";
	public static final String EDITOR_CONTEXT_ID = "com.googlecode.goclipse.editor";
	
	public static final String EDITOR_CODE_TARGET = "com.googlecode.goclipse.ui.Editor.CodeTarget";
	
	public static GoEditorSourceViewerConfiguration createSourceViewerConfiguration(
			IPreferenceStore preferenceStore, AbstractLangEditor editor) {
		IColorManager colorManager = LangUIPlugin.getInstance().getColorManager();
		return new GoEditorSourceViewerConfiguration(preferenceStore, colorManager, (GoEditor) editor);
	}
	
	public static GoSimpleSourceViewerConfiguration createSimpleSourceViewerConfiguration(
			IPreferenceStore preferenceStore, IColorManager colorManager) {
		return new GoSimpleSourceViewerConfiguration(preferenceStore, colorManager, null);
	}
	
	public static Class<GoEditor> editorKlass() {
		return GoEditor.class;
	}
	
	/* ----------------- actions ----------------- */
	
	public static interface EditorCommandIds {
		
		public static final String OpenDef_ID = "com.googlecode.goclipse.ui.commands.openDefinition";
		
	}
	
	public static LangEditorContextMenuContributor createCommandsContribHelper(IServiceLocator svcLocator) {
		return new GoEditorContextMenuContributor(svcLocator);
	}
	
}