/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.editor.text;

import melnorme.lang.ide.ui.EditorSettings_Actual.EditorPrefConstants;

/** 
 * This interface should not be accessed by name directly, 
 * but rather {@link EditorPrefConstants} should be used instead, 
 * so that the value of certain keys can be overrideden by Lang-specific code if so desired 
 */
public interface EditorPrefConstants_Common {
	
	String MATCHING_BRACKETS=  "matchingBrackets";
	String MATCHING_BRACKETS_COLOR=  "matchingBracketsColor";
	String HIGHLIGHT_BRACKET_AT_CARET_LOCATION= "highlightBracketAtCaretLocation";
	
	/**
	 * Preference key for enclosing brackets.
	 * 
	 * @since 3.8
	 */
	String ENCLOSING_BRACKETS= "enclosingBrackets";
	
}