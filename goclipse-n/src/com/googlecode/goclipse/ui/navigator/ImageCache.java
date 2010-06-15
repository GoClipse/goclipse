package com.googlecode.goclipse.ui.navigator;

import java.util.Hashtable;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.googlecode.goclipse.Activator;

/**
 * 
 * @author Mihailo Vasiljevic
 * 
 */
public class ImageCache {

	public static final String PACKAGE_ICON_PATH = "icons/package.gif";
	public static final String SOURCE_FOLDER_ICON_PATH = "icons/source-folder.gif";

	private static ImageCache instance = null;

	public static ImageCache getInstance() {
		if (instance == null) {
			instance = new ImageCache();
		}
		return instance;
	}

	private final Hashtable<String, Image> table = new Hashtable<String, Image>();

	public Image getCachedImage(String path) {
		if (!table.contains(path)) {
			ImageDescriptor desc = Activator.getImageDescriptor(path);
			Image newImage = desc.createImage();
			table.put(path, newImage);
		}
		return table.get(path);
	}
}
