package com.googlecode.goclipse.preferences;

import static com.googlecode.goclipse.GoEnvironmentPrefs.getGO_OS_Default;
import static com.googlecode.goclipse.GoEnvironmentPrefs.get_GO_ARCH_Default;
import melnorme.lang.ide.core.utils.prefs.StringPreference;

/**
 * Constant definitions for plug-in preferences
 */
public interface PreferenceConstants {

	static StringPreference GO_PATH = new StringPreference("com.googlecode.goclipse.gopath", "");
	static StringPreference GO_ROOT = new StringPreference("com.googlecode.goclipse.goroot", "");
	static StringPreference GO_OS = new StringPreference("com.googlecode.goclipse.goos", getGO_OS_Default());
	static StringPreference GO_ARCH = new StringPreference("com.googlecode.goclipse.goarch", get_GO_ARCH_Default());
	
	static String GO_TOOL_PATH    = "com.googlecode.goclipse.compiler.path";
	static String FORMATTER_PATH  = "com.googlecode.goclipse.formatter.path";
	static String DOCUMENTOR_PATH = "com.googlecode.goclipse.documentor.path";
	
	static String OS_WINDOWS = "windows";
	static String OS_NACL    = "nacl";
	static String OS_FREEBSD = "freebsd";
	static String OS_LINUX   = "linux";
	static String OS_DARWIN  = "darwin";

	static String ARCH_ARM   = "arm";
	static String ARCH_386   = "386";
	static String ARCH_AMD64 = "amd64";

}
