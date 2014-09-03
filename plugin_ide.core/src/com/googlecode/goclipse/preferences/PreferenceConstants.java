package com.googlecode.goclipse.preferences;

import com.googlecode.goclipse.GoEnvironmentPrefs;

/**
 * Constant definitions for plug-in preferences
 */
public interface PreferenceConstants extends GoEnvironmentPrefs {

	static String OS_WINDOWS = "windows";
	static String OS_NACL    = "nacl";
	static String OS_FREEBSD = "freebsd";
	static String OS_LINUX   = "linux";
	static String OS_DARWIN  = "darwin";

	static String ARCH_ARM   = "arm";
	static String ARCH_386   = "386";
	static String ARCH_AMD64 = "amd64";

}