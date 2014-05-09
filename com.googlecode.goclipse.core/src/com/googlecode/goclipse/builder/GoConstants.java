package com.googlecode.goclipse.builder;

import melnorme.lang.ide.launching.LaunchConstants;

/**
 * 
 */
public class GoConstants {
	
	public static final String GO_SOURCE_FILE_EXTENSION  = ".go";
	public static final String GO_TEST_FILE_EXTENSION    = "_test.go";
	public static final String GO_LIBRARY_FILE_EXTENSION = ".a";

	public static final String OBJ_FILE_DIRECTORY   = "_obj";
	public static final String TEST_FILE_DIRECTORY  = "_test";
	public static final String GO_TEST_MAIN 		= "_testmain" + GO_SOURCE_FILE_EXTENSION;
	public static final String EXE_FILE_DIRECTORY   = "out";
	public static final String COMPILER_OPTION_I 	= "-I";
	public static final String COMPILER_OPTION_L 	= "-L";
	public static final String COMPILER_OPTION_O 	= "-o";
	public static final String COMPILER_OPTION_E 	= "-e";
	public static final String PACKER_OPTIONS_GRC 	= "grc";
	
	public static final String GO_VERSION_COMMAND  = "version";
	public static final String GO_BUILD_COMMAND    = "build";
	public static final String GO_CLEAN_COMMAND    = "clean";
	public static final String GO_DOC_COMMAND      = "doc";
	public static final String GO_FIX_COMMAND      = "fix";
	public static final String GO_FMT_COMMAND      = "fmt";
	public static final String GO_GET_COMMAND      = "get";
	public static final String GO_INSTALL_COMMAND  = "install";
	public static final String GO_LIST_COMMAND     = "list";
	public static final String GO_RUN_COMMAND      = "run";
	public static final String GO_TEST_COMMAND     = "test";
	public static final String GO_VET_COMMAND      = "vet";
	public static final String GO_TOOL_COMMAND     = "tool";

	public static final String GOROOT = "GOROOT";
	public static final String GOARCH = "GOARCH";
	public static final String GOOS   = "GOOS";
	public static final String GOPATH = "GOPATH";

	/**
	 * defined in plugin.xml
	 */
	public static final String LAUNCH_CONFIGURATION_TYPE = "com.googlecode.goclipse.debug.LaunchConfigurationDelegate";

	public static final String INVALID_PREFERENCES_MESSAGE        = "Invalid Go language settings. Please adjust on the Go preferences page.";
	public static final String CYCLE_DETECTED_MESSAGE      		  = "Dependency cycle detected.";
	public static final String DECLARED_PACKAGE_INCORRECT_MESSAGE = "Declared package name does not match directory.";
	public static final String ONLY_PACKAGE_MAIN_MESSAGE          = "Only package main is allowed here.";

	/**
	 * used in launch configurations
	 */
	public static final String GO_CONF_ATTRIBUTE_PROJECT           = LaunchConstants.ATTR_PROJECT_NAME;
	public static final String GO_CONF_ATTRIBUTE_MAIN              = LaunchConstants.ATTR_PROGRAM_PATH;
	public static final String GO_CONF_ATTRIBUTE_ARGS              = LaunchConstants.ATTR_PROGRAM_ARGUMENTS;
	public static final String GO_CONF_ATTRIBUTE_WORKING_DIRECTORY = LaunchConstants.ATTR_WORKING_DIRECTORY;
	public static final String GO_CONF_ATTRIBUTE_BUILD_CONFIG      = LaunchConstants.ATTR_BUILD_CONFIG;
	
}