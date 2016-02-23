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
package melnorme.lang.ide.ui.preferences.common;

import melnorme.util.swt.components.IWidgetComponent;

/**
 * Basically a widget component that knows how to save it's settings, and load the default settings. 
 */
public interface IPreferencesWidget extends IWidgetComponent, IPreferencesEditor {
	
}