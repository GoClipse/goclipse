package com.googlecode.goclipse;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.Util;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.googlecode.goclipse.builder.GoBuilder;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @author steel
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "goclipse";

	public static final String CONTENT_ASSIST_EXTENSION_ID = "com.googlecode.goclipse.contentassistprocessor";
	
	// The shared instance
	private static Activator plugin;
	
	private static Map<ImageDescriptor, Image> imageCache = new HashMap<ImageDescriptor, Image>();
	
	/**
	 * The constructor
	 */
	public Activator() {
		
	}

	@Override
  public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		// Force construction of singleton
		Environment.INSTANCE.toString();
		
		GoBuilder.checkForCompilerUpdates(true);
	}

	@Override
  public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
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
	
	/**
	 * Get an image given a path relative to this plugin.
	 * 
	 * @param path
	 * @return an image
	 */
	public static Image getImage(String path) {
		try {
			Image image = getDefault().getImageRegistry().get(path);
			
			if (image != null) {
				return image;
			}
	
			ImageDescriptor descriptor = getImageDescriptor(path);
	
			if (descriptor != null) {
				getDefault().getImageRegistry().put(path, descriptor);
	
				return getDefault().getImageRegistry().get(path);
			}
		} catch(Exception ex){
			Activator.logError(ex);
		}

		return null;
	}

	/**
	 * Create or return the cached image for the given image descriptor.
	 * 
	 * @param imageDescriptor
	 * @return the image for the given image descriptor
	 */
	public static Image getImage(ImageDescriptor imageDescriptor) {
		Image image = imageCache.get(imageDescriptor);
		
		if (image == null) {
			image = imageDescriptor.createImage();
			
			imageCache.put(imageDescriptor, image);
		}
		
		return image;
	}
	
	/**
	 * Log the given info message to the Eclipse log.
	 */
	public static void logInfo(String message) {
		if (Environment.DEBUG) {
			getDefault().getLog().log(new Status(IStatus.INFO, PLUGIN_ID, message));
		}
	}
	
	/**
	 * Log the given info message to the Eclipse log.
	 */
	public static void logInfo(Throwable t) {
		if (Environment.DEBUG) {
			getDefault().getLog().log(new Status(IStatus.INFO, PLUGIN_ID, t.getMessage(), t));
		}
	}
	
	/**
	 * Log the given warning message to the Eclipse log.
	 */
	public static void logWarning(String message) {
		getDefault().getLog().log(new Status(IStatus.WARNING, PLUGIN_ID, message));
	}
	
	/**
	 * Log the given error message to the Eclipse log.
	 */
	public static void logError(String message) {
		getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, message));
	}
	
	/**
	 * Log the given exception to the Eclipse log.
	 * 
	 * @param t the exception to log
	 */
	public static void logError(Throwable t) {
	  if (getDefault() != null) {
	    getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, t.getMessage(), t));
	  }
	}
	
	public static boolean isMac() {
	  return Util.isMac();
	}

	public static boolean isWindows() {
	  return Util.isWindows();
	}
	
}
