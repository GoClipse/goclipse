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

import melnorme.utilbox.misc.MiscUtil;

import com.googlecode.goclipse.tooling.GoArch;
import com.googlecode.goclipse.tooling.GoOs;

public class GoEnvironmentPrefUtils {

	public static String get_GO_ARCH_Default() {
		if (isAMD64()){
			return GoArch.ARCH_AMD64;
		} else if (is386()){
			return GoArch.ARCH_386;
		} else {
			return "";
		}
	}
	
	public static boolean isAMD64() {
		String osProp = System.getProperty("os.arch");
		return "x86_64".equals(osProp) || "amd64".equals(osProp);
	}

	public static boolean is386() {
		String osProp = System.getProperty("os.arch");
		return "i386".equals(osProp) || "x86".equals(osProp) || "i686".equals(osProp);
	}
	
	public static String getGO_OS_Default() {
		if (MiscUtil.OS_IS_WINDOWS){
			return GoOs.OS_WINDOWS;
		} else if (MiscUtil.OS_IS_LINUX) {
			return GoOs.OS_LINUX;
		} else if (MiscUtil.OS_IS_MAC) {
			return GoOs.OS_DARWIN;
		} else {
			return "";
		}
	}
	
}