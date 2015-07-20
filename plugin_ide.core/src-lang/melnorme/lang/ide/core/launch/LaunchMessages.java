package melnorme.lang.ide.core.launch;

import java.text.MessageFormat;

public class LaunchMessages {
	
	public static final String PROCESS_LAUNCH_NoBuildTargetSpecified = 
			"Build target not specified";
	public static final String PROCESS_LAUNCH_NoSuchBuildTarget = 
			"Build target does not exist";
	public static final String PROCESS_LAUNCH_CouldNotDetermineExeLocation = 
			"No build target or executable path specified.";
	
	
	public static final String LCD_PreparingLaunch = 
			"Preparing launch...";
	public static final String LCD_buildPrerequesite = 
			"Building prerequisite project list...";
	private static final String LCD_startingLaunchConfiguration =
			"Starting launch configuration {0}...";
	public static String LCD_StartingLaunchConfiguration(String name) {
		return MessageFormat.format(LCD_startingLaunchConfiguration, name);
	}
	
	public static final String LCD_errProcessNotSpecified = 
			"Process path not specified.";
	public static final String LCD_errProcessPathEmtpy = 
			"Process path is empty.";
	public static final String LCD_errINTERNAL_UnsupportedMode = 
			"Unsupported run mode: {0}";
	
	public static final String errWorkingDirectoryDoesntExist =
			"Working directory ({0}) doesn't exist.";
	public static final String errExecutableFileDoesntExist =
			"Executable file ({0}) doesn't exist.";
	public static final String errFailedToSetupProcessEnvironment =
			"Failed to setup process environment.";
	public static final String errNewJavaProcessFailed = 
			"Failed to create a child process.";
	
	public static String getFormattedString(String message, Object... args) {
		return MessageFormat.format(message, args);
	}
	
}