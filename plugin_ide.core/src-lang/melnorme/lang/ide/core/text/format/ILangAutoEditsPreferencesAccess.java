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
package melnorme.lang.ide.core.text.format;

import melnorme.lang.ide.core.text.TextSourceUtils;

public interface ILangAutoEditsPreferencesAccess {
	
	boolean isSmartIndent();
	
	boolean isSmartDeIndent();
	
	boolean closeBraces();
	
	boolean isSmartPaste();
	
	public boolean parenthesesAsBlocks();
	
	FormatterIndentMode getTabStyle();
	
	int getIndentSize();
	
	default String getIndentUnit() {
		if (getTabStyle() == FormatterIndentMode.SPACES) {
			return TextSourceUtils.getNSpaces(getIndentSize());
		} else {
			return "\t";
		}
	}
	
}