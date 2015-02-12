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
package melnorme.lang.ide.core.bundlemodel;

import melnorme.lang.ide.core.utils.prefs.StringPreference;

public interface SDKPreferences {
	
	public static final StringPreference SDK_PATH = new StringPreference("sdk_path", "");
	
}