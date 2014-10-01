package com.googlecode.goclipse.builder;

import com.googlecode.goclipse.tooling.GoCommandConstants;
import com.googlecode.goclipse.tooling.GoFileNaming;
import com.googlecode.goclipse.tooling.env.GoEnvironmentConstants;

import melnorme.lang.ide.launching.LaunchConstants;

/**
 * 
 */
@Deprecated
public class GoConstants implements GoEnvironmentConstants, GoCommandConstants, GoFileNaming {
	
	
	public static final String OBJ_FILE_DIRECTORY   = "_obj";
	public static final String TEST_FILE_DIRECTORY  = "_test";
	public static final String GO_TEST_MAIN 		= "_testmain" + GO_SOURCE_FILE_EXTENSION;
	public static final String EXE_FILE_DIRECTORY   = "out";

	/**
	 * defined in plugin.xml
	 */
	public static final String LAUNCH_CONFIGURATION_TYPE = "com.googlecode.goclipse.debug.LaunchConfigurationDelegate";

	/**
	 * used in launch configurations
	 */
	public static final String GO_CONF_ATTRIBUTE_PROJECT           = LaunchConstants.ATTR_PROJECT_NAME;
	public static final String GO_CONF_ATTRIBUTE_MAIN              = LaunchConstants.ATTR_PROGRAM_PATH;
	public static final String GO_CONF_ATTRIBUTE_ARGS              = LaunchConstants.ATTR_PROGRAM_ARGUMENTS;
	public static final String GO_CONF_ATTRIBUTE_WORKING_DIRECTORY = LaunchConstants.ATTR_WORKING_DIRECTORY;
	public static final String GO_CONF_ATTRIBUTE_BUILD_CONFIG      = LaunchConstants.ATTR_BUILD_CONFIG;
	
}