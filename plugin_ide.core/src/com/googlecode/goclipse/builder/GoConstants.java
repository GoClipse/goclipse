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
	
}