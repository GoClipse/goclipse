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
package com.googlecode.goclipse.core;


/* FIXME: the deprecations from this class */
public class GoEnvironmentPrefs implements GoEnvironmentPrefConstants {
	
	/** @return true if the preferences have been set for all values */
	@Deprecated
	public static boolean isValid() {
		String goroot = GoEnvironmentPrefConstants.GO_ROOT.get();
		String goarch = GoEnvironmentPrefConstants.GO_ARCH.get();
		String goos = GoEnvironmentPrefConstants.GO_OS.get();
		
		if (isNullOrEmpty(goroot) || isNullOrEmpty(goos) || isNullOrEmpty(goarch)) {
			return false;
		}
		
		return true;
	}
	
	private static boolean isNullOrEmpty(String string) {
		return string == null || string.isEmpty();
	}
	
}