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
package melnorme.lang.ide.ui.preferences;

import melnorme.util.swt.components.IWidgetComponent;

import org.eclipse.jface.preference.IPreferenceStore;

public interface IPreferencesComponent extends IWidgetComponent {
	
	public void loadFromStore(IPreferenceStore store);
	
	public void saveToStore(IPreferenceStore store);
	
	public void loadStoreDefaults(IPreferenceStore store);
	
}