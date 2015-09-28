/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.editor.text;

import melnorme.lang.ide.ui.CodeFormatterConstants.IndentMode;
import melnorme.lang.ide.ui.text.util.AutoEditUtils;


public interface ILangAutoEditsPreferencesAccess {
	
	boolean isSmartIndent();
	
	boolean isSmartDeIndent();
	
	boolean closeBlocks();
	
	boolean closeBraces();
	
	boolean isSmartPaste();
	
	IndentMode getTabStyle();
	
	int getIndentSize();
	
	default String getIndentUnit() {
		if (getTabStyle() == IndentMode.SPACES) {
			return AutoEditUtils.getNSpaces(getIndentSize());
		} else {
			return "\t";
		}
	}
	
}