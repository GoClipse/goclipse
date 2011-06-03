package com.googlecode.goclipse;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

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

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		// Force construction of singleton
		Environment.INSTANCE.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
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
		Image image = getDefault().getImageRegistry().get(path);
		
		if (image != null) {
			return image;
		}

		ImageDescriptor descriptor = getImageDescriptor(path);

		if (descriptor != null) {
			getDefault().getImageRegistry().put(path, descriptor);

			return getDefault().getImageRegistry().get(path);
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

}
