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

import java.nio.file.Path;

import org.eclipse.core.runtime.CoreException;

import com.googlecode.goclipse.preferences.PreferenceConstants;


public class GoEnvironmentPrefs implements GoEnvironmentPrefConstants {
	
	public static GoRoot getGoRoot() {
		return new GoRoot(GO_ROOT.get(), GO_OS.get(), GO_ARCH.get());
	}
	
	public static class GoRoot extends CommonGoLocation {
		
		public GoRoot(String location, String go_os, String go_arch) {
			super(location, go_os, go_arch);
		}
		
		public Path getToolsLocation() throws CoreException {
			return createPath(location + "/pkg/tool/").resolve(getGo_OS_Arch_segment());
		}
		
	}
	
	/** @return true if the preferences have been set for all values */
	public static boolean isValid() {
		String goroot = PreferenceConstants.GO_ROOT.get();
		String goarch = PreferenceConstants.GO_ARCH.get();
		String goos = PreferenceConstants.GO_OS.get();
		
		if (isNullOrEmpty(goroot) || isNullOrEmpty(goos) || isNullOrEmpty(goarch)) {
			return false;
		}
		
		return true;
	}
	
	private static boolean isNullOrEmpty(String string) {
		return string == null || string.isEmpty();
	}
	
}