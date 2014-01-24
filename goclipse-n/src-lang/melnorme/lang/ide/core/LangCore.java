package melnorme.lang.ide.core;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;

public abstract class LangCore extends Plugin {
	
	public static class ILangConstants {
		
		public static int INTERNAL_ERROR = 1;
		
	}
	
	public static String PLUGIN_ID = LangCore_Actual.PLUGIN_ID;
	
	public static Plugin getInstance() {
		return LangCore_Actual.getInstance();
	}
	
	/** Convenience method to get the WorkspaceRoot. */
	public static IWorkspaceRoot getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}
	
	/** Convenience method to get the Workspace. */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}
	
	/** Creates a status describing an error in this plugin. */
	public static IStatus createErrorStatus(String msg) {
		return createErrorStatus(msg, null);
	}
	
	/** Creates a status describing an error in this plugin. */
	public static Status createErrorStatus(String msg, Exception e) {
		return new Status(IStatus.ERROR, PLUGIN_ID, ILangConstants.INTERNAL_ERROR, msg, e); 
	}
	
	/** Creates a CoreException describing an error in this plugin. */
	public static CoreException createCoreException(String msg, Exception e) {
		return new CoreException(createErrorStatus(msg, e));
	}
	
	public static void log(Exception e) {
		logError(e);
	}
	
	/** Logs an error status with given exception and given message. */
	public static void logError(Exception e, String message) {
		getInstance().getLog().log(createErrorStatus(message, e));
	}
	
	/** Logs an error status with given message. */
	public static void logError(String message) {
		getInstance().getLog().log(createErrorStatus(message, null));
	}
	
	/** Logs an error status with given exception. */
	public static void logError(Exception e) {
		getInstance().getLog().log(createErrorStatus(LangCoreMessages.LangCore_internal_error, e));
	}
	
	/** Logs the given message, creating a new warning status for this plugin. */
	public static void logWarning(String message) {
		getInstance().getLog().log(
				new Status(IStatus.WARNING, PLUGIN_ID, ILangConstants.INTERNAL_ERROR, message, null));
	}
	
}