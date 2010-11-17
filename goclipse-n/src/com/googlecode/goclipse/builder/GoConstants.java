package com.googlecode.goclipse.builder;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Status;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.preferences.PreferenceConstants;

public class GoConstants {
	public static final String GO_SOURCE_FILE_EXTENSION = ".go";
	public static final String GO_LIBRARY_FILE_EXTENSION = ".a";

	public static final String OBJ_FILE_DIRECTORY       = "_obj";
	public static final String TEST_FILE_DIRECTORY      = "_test";
	public static final String GO_TEST_MAIN = "_testmain"+GO_SOURCE_FILE_EXTENSION;
	public static final String EXE_FILE_DIRECTORY       = "out";
	public static final String COMPILER_OPTION_I = "-I";
	public static final String COMPILER_OPTION_L = "-L";
	public static final String COMPILER_OPTION_O = "-o";
	public static final String COMPILER_OPTION_E = "-e";
	public static final String PACKER_OPTIONS_GRC = "grc";

	public static final String GOROOT = "GOROOT";
	public static final String GOARCH = "GOARCH";
	public static final String GOOS = "GOOS";

	/**
	 * defined in plugin.xml
	 */
	public static final String LAUNCH_CONFIGURATION_TYPE = "com.googlecode.goclipse.debug.LaunchConfigurationDelegate";

	public static final String INVALID_PREFERENCES_MESSAGE = "Invalid Go language settings.  Please adjust on the Go preferences page.";
	public static final String CYCLE_DETECTED_MESSAGE = "Dependency cycle detected.";
	public static final String DECLARED_PACKAGE_INCORRECT_MESSAGE = "Declared package name does not match directory.";
	public static final String ONLY_PACKAGE_MAIN_MESSAGE = "Only package main is allowed here.";

	/**
	 * used in launch configurations
	 */
	public static final String GO_CONF_ATTRIBUTE_PROJECT = "PROJECT_NAME";
	public static final String GO_CONF_ATTRIBUTE_MAIN = "MAIN_FILE";
	public static final String GO_CONF_ATTRIBUTE_ARGS = "PROGRAM_ARGS";
	public static final String GO_CONF_ATTRIBUTE_BUILD_CONFIG = "BUILD_CONFIG";
	
	public static Map<String, String> environment() {
		Map<String, String> goEnv = new HashMap<String, String>();
		String goroot = Activator.getDefault().getPreferenceStore().getString(
				PreferenceConstants.GOROOT);
		String goos = Activator.getDefault().getPreferenceStore().getString(
				PreferenceConstants.GOOS);
		String goarch = Activator.getDefault().getPreferenceStore().getString(
				PreferenceConstants.GOARCH);

		goEnv.put(GoConstants.GOROOT, goroot);
		goEnv.put(GoConstants.GOOS, goos);
		goEnv.put(GoConstants.GOARCH, goarch);
		return goEnv;

	}
	
	public static MessageConsole findConsole(String name) {
	      ConsolePlugin plugin = ConsolePlugin.getDefault();
	      IConsoleManager conMan = plugin.getConsoleManager();
	      IConsole[] existing = conMan.getConsoles();
	      for (int i = 0; i < existing.length; i++)
	         if (name.equals(existing[i].getName()))
	            return (MessageConsole) existing[i];
	      //no console found, so create a new one
	      MessageConsole myConsole = new MessageConsole(name, null);
	      conMan.addConsoles(new IConsole[]{myConsole});
	      return myConsole;
	   }

}
