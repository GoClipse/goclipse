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
import melnorme.lang.ide.ui.preferences.ColorPreference;

/**
 * Only {@link ContentAssistConstants} should refer to this class, no one else, 
 * not even the initializer class!
 */
public interface ContentAssistConstants_Default {
	
	ColorPreference PROPOSALS_FOREGROUND_2 = new ColorPreference("content_assist_proposals_foreground", 
		new RGB(0, 0, 0));
	ColorPreference PROPOSALS_BACKGROUND_2 = new ColorPreference("content_assist_proposals_background",
		new RGB(255, 255, 255));
	ColorPreference PARAMETERS_FOREGROUND_2 = new ColorPreference("content_assist_parameters_foreground",
		new RGB(0, 0, 0));
	ColorPreference PARAMETERS_BACKGROUND_2 = new ColorPreference("content_assist_parameters_background",
		new RGB(255, 255, 255));
	
}