package com.googlecode.goclipse.ui;

import melnorme.lang.ide.ui.LangUIPlugin;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;


// TODO: refactor this out to GoUIPlugin code
/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author devoncarew
 */
public class GoDebugPlugin {
	
	protected static AbstractUIPlugin getPlugin() {
		return GoUIPlugin.getInstance();
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
	public static ImageDescriptor getImageDescriptor(String imageFilePath) {
		return LangUIPlugin.imageDescriptorFromPlugin(LangUIPlugin.PLUGIN_ID, imageFilePath);
	}
	
	public static void logError(Throwable exception) {
		LangUIPlugin.log(exception);
	}
	
}