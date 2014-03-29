/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     DLTK ?? - initial API and implementation? (Originally was RubyPreferencesInterpreter)
 *     Bruno Medeiros - lang modifications
 *******************************************************************************/
package melnorme.lang.ide.ui.editor.text;

import melnorme.lang.ide.ui.CodeFormatterConstants;
import melnorme.lang.ide.ui.text.util.AutoEditUtils;
import melnorme.lang.ide.ui.text.util.TabStyle;

import org.eclipse.jface.preference.IPreferenceStore;

// Originally was RubyPreferencesInterpreter
public class LangAutoEditsPreferencesAdapter {
	
	protected final IPreferenceStore store;
	
	public LangAutoEditsPreferencesAdapter(IPreferenceStore store) {
		this.store = store;
	}
	
	public boolean isSmartMode() {
		return store.getBoolean(LangAutoEditPreferenceConstants.AE_SMART_INDENT);
	}
	
	public boolean isSmartDeIndent() {
		return store.getBoolean(LangAutoEditPreferenceConstants.AE_SMART_DEINDENT);
	}
	
	public boolean closeBlocks() {
		return closeBraces();
	}

	public boolean closeBraces() {
		return store.getBoolean(LangAutoEditPreferenceConstants.AE_CLOSE_BRACES);
	}
	
	public boolean isSmartPaste() {
		return store.getBoolean(LangAutoEditPreferenceConstants.AE_SMART_PASTE);
	}
	

	public TabStyle getTabStyle() {
		return TabStyle.forName(store.getString(CodeFormatterConstants.FORMATTER_TAB_CHAR), TabStyle.TAB);
	}

	public String getIndent() {
		if (getTabStyle() == TabStyle.SPACES) {
			return AutoEditUtils.getNSpaces(getIndentSize());
		} else {
			return "\t"; //$NON-NLS-1$
		}
	}
	
	public int getIndentSize() {
		return store.getInt(CodeFormatterConstants.FORMATTER_INDENTATION_SIZE);
	}
	
}