package melnorme.lang.ide.launching;

import java.text.MessageFormat;

public class LaunchMessages {
	
	public static final String LCD_buildPrerequesite = 
			"Building prerequisite project list...";
	public static final String LCD_startingLaunchConfiguration =
			"Starting launch configuration {0}...";
	public static final String LCD_startingProcess = 
			"Starting process...";
	
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
	
	public static final String LangArgumentsTab_Program_Arguments = 
			"Program Arguments";
	public static final String LangArgumentsTab_Variables = 
			"Variables";
	public static final String Launch_common_Exception_occurred_reading_configuration_EXCEPTION = 
			"Exception occurred reading launch configuration";
	public static final String LangArgumentsTab_Arguments = 
			"Arguments";
	
	public static String getFormattedString(String message, Object... args) {
		return MessageFormat.format(message, args);
	}
	
}