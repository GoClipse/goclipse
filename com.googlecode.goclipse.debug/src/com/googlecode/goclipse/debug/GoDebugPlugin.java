package com.googlecode.goclipse.debug;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author devoncarew
 */
public class GoDebugPlugin extends AbstractUIPlugin {
	// The plug-in ID
	public static final String PLUGIN_ID = "com.googlecode.goclipse.debug";

	// The shared instance
	private static GoDebugPlugin plugin;

	private IDebugEventSetListener debuggerEventListener = new IDebugEventSetListener() {
		@Override
		public void handleDebugEvents(DebugEvent[] events) {
			for (DebugEvent event : events) {
				System.out.println("[" + event + "]");
			}
		}
	};
	
	/**
	 * The constructor
	 */
	public GoDebugPlugin() {

	}

	public void start(BundleContext context) throws Exception {
		super.start(context);

		plugin = this;
		
		DebugPlugin.getDefault().addDebugEventListener(debuggerEventListener);
	}

	public void stop(BundleContext context) throws Exception {
		DebugPlugin.getDefault().removeDebugEventListener(debuggerEventListener);
		
		plugin = null;

		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static GoDebugPlugin getPlugin() {
		return plugin;
	}

	/**
	 * Get an image given a path relative to this plugin.
	 * 
	 * @param path
	 * @return an image
	 */
	public static Image getImage(String path) {
		Image image = getPlugin().getImageRegistry().get(path);
		
		if (image != null) {
			return image;
		}

		ImageDescriptor descriptor = getImageDescriptor(path);

		if (descriptor != null) {
			getPlugin().getImageRegistry().put(path, descriptor);

			return getPlugin().getImageRegistry().get(path);
		}

		return null;
	}
	
	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static void logError(Throwable exception) {
		getPlugin().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, exception.getMessage(), exception));
	}
	
}
