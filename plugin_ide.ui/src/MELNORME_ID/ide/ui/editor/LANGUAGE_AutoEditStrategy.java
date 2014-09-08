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
package MELNORME_ID.ide.ui.editor;

import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.editor.text.LangAutoEditStrategyExt;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.ITextViewer;

public class LANGUAGE_AutoEditStrategy extends LangAutoEditStrategyExt {
	
	public LANGUAGE_AutoEditStrategy(IPreferenceStore store, String contentType, ITextViewer viewer) {
		super(store, LangUIPlugin_Actual.LANG_PARTITIONING, contentType, viewer);
	}
	
}