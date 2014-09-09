/*******************************************************************************
 * Copyright (c) 2008, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package LANG_PROJECT_ID.ide.ui.text;



/**
 * Color preference constants used in the Dee preference store. 
 */
public interface LANGUAGE_ColorConstants {
	
	/** Prefix for preference keys. */
	String PREFIX = "dee.coloring."; 
	
	String CODE__DEFAULT = PREFIX + "default";
	String CODE__KEYWORDS = PREFIX + "keyword";
	String CODE__KEYWORD_VALUES = PREFIX + "keywor_values";
	String CODE__OPERATORS = PREFIX + "operators";
	
}