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

import melnorme.lang.ide.core.text.format.FormatterIndentMode;
import melnorme.lang.ide.core.text.format.ILangAutoEditsPreferencesAccess;
import melnorme.lang.ide.ui.CodeFormatterConstants;
import melnorme.lang.ide.ui.LangAutoEditPreferenceConstants;

public class LangAutoEditsPreferencesAccess implements ILangAutoEditsPreferencesAccess {
	
	public LangAutoEditsPreferencesAccess() {
	}
	
	@Override
	public boolean isSmartIndent() {
		return LangAutoEditPreferenceConstants.AE_SMART_INDENT.get();
	}
	
	@Override
	public boolean isSmartDeIndent() {
		return LangAutoEditPreferenceConstants.AE_SMART_DEINDENT.get();
	}
	
	@Override
	public boolean closeBlocks() {
		return closeBraces();
	}

	@Override
	public boolean closeBraces() {
		return LangAutoEditPreferenceConstants.AE_CLOSE_BRACES.get();
	}
	
	@Override
	public boolean isSmartPaste() {
		return LangAutoEditPreferenceConstants.AE_SMART_PASTE.get();
	}
	
	@Override
	public FormatterIndentMode getTabStyle() {
		return CodeFormatterConstants.fromPrefStore();
	}
	
	@Override
	public int getIndentSize() {
		return CodeFormatterConstants.FORMATTER_INDENTATION_SPACES_SIZE.get();
	}
	
}