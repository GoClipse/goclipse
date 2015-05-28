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
import melnorme.lang.ide.ui.CodeFormatterConstants.IndentMode;
import melnorme.lang.ide.ui.text.util.AutoEditUtils;

// Originally was RubyPreferencesInterpreter from DLTK
public class LangAutoEditsPreferencesAdapter {
	
	public LangAutoEditsPreferencesAdapter() {
	}
	
	public boolean isSmartMode() {
		return LangAutoEditPreferenceConstants.AE_SMART_INDENT.get();
	}
	
	public boolean isSmartDeIndent() {
		return LangAutoEditPreferenceConstants.AE_SMART_DEINDENT.get();
	}
	
	public boolean closeBlocks() {
		return closeBraces();
	}

	public boolean closeBraces() {
		return LangAutoEditPreferenceConstants.AE_CLOSE_BRACES.get();
	}
	
	public boolean isSmartPaste() {
		return LangAutoEditPreferenceConstants.AE_SMART_PASTE.get();
	}
	

	public IndentMode getTabStyle() {
		return IndentMode.fromPrefStore();
	}
	
	public String getIndentUnit() {
		if (getTabStyle() == IndentMode.SPACES) {
			return AutoEditUtils.getNSpaces(getIndentSize());
		} else {
			return "\t";
		}
	}
	
	public int getIndentSize() {
		return CodeFormatterConstants.FORMATTER_INDENTATION_SPACES_SIZE.get();
	}
	
}