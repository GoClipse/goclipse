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
package melnorme.lang.ide.ui;

import melnorme.lang.ide.core.utils.prefs.BooleanPreference;
import melnorme.lang.ide.ui.preferences.ColorPreference;

import org.eclipse.swt.graphics.RGB;


interface EditorPreferences_Default {
	
	BooleanPreference SOURCE_HOVER_BACKGROUND_COLOR_UseSystemDefault = 
			new BooleanPreference("SourceHover.bg_color.useSystemDefault", true); 
	ColorPreference SOURCE_HOVER_BACKGROUND_COLOR_rgb = 
			new ColorPreference("SourceHover.bg_color.rgb", new RGB(255, 255, 255));
	
}