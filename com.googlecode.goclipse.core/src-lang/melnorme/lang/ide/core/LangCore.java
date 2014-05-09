package melnorme.lang.ide.core;

import melnorme.lang.ide.core.utils.EclipseUtils;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

public abstract class LangCore extends Plugin {
	
	public static final String PLUGIN_ID = LangCore_Actual.PLUGIN_ID;
	public static final String NATURE_ID = LangCore_Actual.NATURE_ID;
	
	protected static LangCore pluginInstance;
	
	/** Returns the singleton for this plugin instance. */
	public static LangCore getInstance() {
		return pluginInstance;
	}
	
	@Override
	public final void start(BundleContext context) throws Exception {
		pluginInstance = this;
		super.start(context);
		doCustomStart(context);
	}
	
	protected abstract void doCustomStart(BundleContext context);

	@Override
	public final void stop(BundleContext context) throws Exception {
		doCustomStop(context);
		super.stop(context);
		pluginInstance = null;
	}
	
	protected abstract void doCustomStop(BundleContext context);
	
	
	/* ----------------- ----------------- */
	
	/** Convenience method to get the WorkspaceRoot. */
	public static IWorkspaceRoot getWorkspaceRoot() {
		return EclipseUtils.getWorkspaceRoot();
	}
	
	/** Convenience method to get the Workspace. */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}
	
	public static Status createStatus(String message) {
		return createOkStatus(message);
	}
	
	/** Creates an OK status with given message. */
	public static Status createOkStatus(String message) {
		return new Status(IStatus.OK, LangCore.PLUGIN_ID, message);
	}
	
	/** Creates a status describing an error in this plugin, with given message. */
	public static IStatus createErrorStatus(String message) {
		return createErrorStatus(message, null);
	}
	
	/** Creates a status describing an error in this plugin, with given message and given throwable. */
	public static Status createErrorStatus(String message, Throwable throwable) {
		return new Status(IStatus.ERROR, PLUGIN_ID, message, throwable);
	}
	
	/** Creates a CoreException describing an error in this plugin. */
	public static CoreException createCoreException(String message, Throwable throwable) {
		return new CoreException(createErrorStatus(message, throwable));
	}
	
	/** Logs given status. */
	public static void logStatus(IStatus status) {
		getInstance().getLog().log(status);
	}
	
	/** Logs an error status with given exception and given message. */
	public static void logError(String message, Throwable throwable) {
		getInstance().getLog().log(createErrorStatus(message, throwable));
	}
	
	/** Logs an error status with given message. */
	public static void logError(String message) {
		getInstance().getLog().log(createErrorStatus(message, null));
	}
	
	/** Logs an error status with given exception. */
	public static void logError(Throwable throwable) {
		getInstance().getLog().log(createErrorStatus(LangCoreMessages.LangCore_error, throwable));
	}
	
	/** Logs the given message, creating a new warning status for this plugin. */
	public static void logWarning(String message) {
		getInstance().getLog().log(new Status(IStatus.WARNING, PLUGIN_ID, message, null));
	}
	
}