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

import java.util.ArrayList;
import java.util.List;

import melnorme.utilbox.misc.MiscUtil;

import com.googlecode.goclipse.tooling.env.GoArch;
import com.googlecode.goclipse.tooling.env.GoOs;

// XXX: Some of the methods of this class may not be necessary, could use some refactoring.
public class GoEnvironmentUtils {

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
	
	public static String getDefaultPlatformChar() {
		if (GoEnvironmentUtils.isAMD64()){
			return "6";
		} else if (GoEnvironmentUtils.is386()) {
			return "8";
		}
		return null;
	}
	
	public static String getDefaultCompilerName() {
		String platformChar = getDefaultPlatformChar();
		if (platformChar == null){
			return null;
		}
		return "go"+(MiscUtil.OS_IS_WINDOWS?".exe":"");
	}
	
	// TODO refactor this out
	protected static String get32bitCompilerName() {
		return "go"+(MiscUtil.OS_IS_WINDOWS?".exe":"");
	}
	
	public static List<String> getSupportedCompilerNames() {
	  List<String> names = new ArrayList<String>();
	  
	  String defaultCompiler = getDefaultCompilerName();
	  
	  if (defaultCompiler != null) {
	    names.add(defaultCompiler);
	  }
	  
	  if (GoEnvironmentUtils.isAMD64()) {
	    String altCompiler = get32bitCompilerName();
	    
	    if (altCompiler != null) {
	      names.add(altCompiler);
	    }
	  }
	  
	  return names;
	}
	
	public static String getDefaultGodocName() {
		return "godoc" +(MiscUtil.OS_IS_WINDOWS?".exe":"");
	}

	public static String getDefaultGofmtName() {
		return "gofmt" +(MiscUtil.OS_IS_WINDOWS?".exe":"");
	}
	
}