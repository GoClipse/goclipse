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
package MELNORME_ID.ide.ui.editor;

import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.editor.AbstractLangEditor;

import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

public class LANGUAGE_Editor extends AbstractLangEditor {
	
	@Override
	protected TextSourceViewerConfiguration createSourceViewerConfiguration() {
		return new LANGUAGE_SourceViewerConfiguration(getPreferenceStore(),
			LangUIPlugin.getInstance().getColorManager());
	}
	
}