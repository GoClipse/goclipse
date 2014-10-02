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
package com.googlecode.goclipse.ui.editor.actions;

public interface GoCommandConstants {
	
	static String COMMAND_ToggleLineComment = "com.googlecode.goclipse.editors.ToggleComment.run";
	static String COMMAND_GoToMatchingBracket = "com.googlecode.goclipse.editors.PairMatcherShortcut.run";
	
	static String COMMAND_RunGoGet = "com.googlecode.goclipse.editors.GoGetShortcut.run";
	static String COMMAND_RunGoFix = "com.googlecode.goclipse.editors.GofixShortcut.run";
	static String COMMAND_RunGoFmt = "com.googlecode.goclipse.editors.GofmtShortcut.run";
	
}