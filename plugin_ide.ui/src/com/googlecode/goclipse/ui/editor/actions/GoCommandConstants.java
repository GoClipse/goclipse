/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui.editor.actions;

import melnorme.lang.ide.ui.EditorSettings_Actual.EditorCommandIds;

public interface GoCommandConstants {
	
	static String COMMAND_GoToMatchingBracket = EditorCommandIds.GoToMatchingBracket;
	static String COMMAND_ToggleLineComment = EditorCommandIds.ToggleComment;
	
	static String COMMAND_RunGoFmt = "com.googlecode.goclipse.editors.GofmtShortcut.run";
	
}