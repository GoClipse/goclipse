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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.RGB;

import melnorme.lang.ide.core.utils.PreferencesLookupHelper;

public class UIPreferencesLookupHelper extends PreferencesLookupHelper {
	
	public UIPreferencesLookupHelper(String qualifier, IProject project) {
		super(qualifier, project);
	}
	
	public UIPreferencesLookupHelper(String qualifier) {
		super(qualifier);
	}
	
	public RGB getRGB(String key, RGB defaultValue) {
		String prefValue = getString(key, null);
		if(prefValue == null) {
			return defaultValue;
		}
		return StringConverter.asRGB(prefValue);
	}
	
	public void setRGB(String key, RGB value) {
		assertNotNull(value);
		String stringValue = StringConverter.asString(value);
		InstanceScope.INSTANCE.getNode(qualifier).put(key, stringValue);
	}
	
}