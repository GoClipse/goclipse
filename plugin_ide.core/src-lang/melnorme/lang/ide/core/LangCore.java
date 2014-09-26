package melnorme.lang.ide.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

public abstract class LangCore extends Plugin {
	
	public static final String PLUGIN_ID = LangCore_Actual.PLUGIN_ID;
	public static final String NATURE_ID = LangCore_Actual.NATURE_ID;
	
	protected static LangCore pluginInstance;
	
	protected boolean initializedAfterUI = false;
	
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
	
	/** 
	 * Initialize services that should only be started after the UI plugin 
	 * (or other application plugin such as test runner) has started.
	 * This is because the UI plugin might register listeners into core services, 
	 * and this ensures that the UI plugin gets all updates, because they will only start after this. 
	 */
	public final void initializeAfterUIStart() {
		if(initializedAfterUI == true) {
			LangCore.logError("Atempted initializeAfterUIStart more than once.");
		} else {
			initializedAfterUI = true;
			doInitializeAfterUIStart();
		}
	}
	
	public void doInitializeAfterUIStart() {
	}
	
	@Override
	public final void stop(BundleContext context) throws Exception {
		doCustomStop(context);
		super.stop(context);
		pluginInstance = null;
	}
	
	protected abstract void doCustomStop(BundleContext context);
	
	
	/* ----------------- ----------------- */
	
	/** Creates an OK status with given message. */
	public static Status createOkStatus(String message) {
		return createStatus(IStatus.OK, message, null);
	}
	
	/** Creates a Status with given status code and message. */
	public static Status createStatus(int statusCode, String message, Throwable throwable) {
		return new Status(statusCode, LangCore.PLUGIN_ID, message, throwable);
	}
	
	/** Creates a status describing an error in this plugin, with given message. */
	public static IStatus createErrorStatus(String message) {
		return createErrorStatus(message, null);
	}
	
	/** Creates a status describing an error in this plugin, with given message and given throwable. */
	public static Status createErrorStatus(String message, Throwable throwable) {
		return createStatus(IStatus.ERROR, message, throwable);
	}
	
	/** Creates a CoreException describing an error in this plugin. */
	public static CoreException createCoreException(String message, Throwable throwable) {
		return new CoreException(createErrorStatus(message, throwable));
	}
	
	/** Logs given status. */
	public static void logStatus(IStatus status) {
		getInstance().getLog().log(status);
	}
	
	/** Logs an error status with given message and given throwable. */
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
	
	/** Logs a warning status with given message */
	public static void logWarning(String message) {
		getInstance().getLog().log(new Status(IStatus.WARNING, PLUGIN_ID, message, null));
	}
	
	/** Logs a warning status with given message and given throwable */
	public static void logWarning(String message, Throwable throwable) {
		getInstance().getLog().log(new Status(IStatus.WARNING, PLUGIN_ID, message, throwable));
	}
	
}