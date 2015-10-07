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
package melnorme.lang.ide.ui.text.completion;

import org.eclipse.swt.graphics.RGB;

import melnorme.lang.ide.ui.ContentAssistConstants;
import melnorme.lang.ide.ui.text.coloring.ThemedColorPreference;

/**
 * Only {@link ContentAssistConstants} should refer to this class, no one else, 
 * not even the initializer class!
 */
public interface ContentAssistConstants_Default {
	
	ThemedColorPreference PROPOSALS_FOREGROUND_2 = new ThemedColorPreference("content_assist_proposals_foreground", 
		new RGB(  0,   0,   0), new RGB(230,230,230));
	ThemedColorPreference PROPOSALS_BACKGROUND_2 = new ThemedColorPreference("content_assist_proposals_background",
		new RGB(255, 255, 255), new RGB(52,57,61));
	ThemedColorPreference PARAMETERS_FOREGROUND_2 = new ThemedColorPreference("content_assist_parameters_foreground",
		new RGB(  0,   0,   0), new RGB(230,230,230));
	ThemedColorPreference PARAMETERS_BACKGROUND_2 = new ThemedColorPreference("content_assist_parameters_background",
		new RGB(255, 255, 255), new RGB(52,57,61));
	
}