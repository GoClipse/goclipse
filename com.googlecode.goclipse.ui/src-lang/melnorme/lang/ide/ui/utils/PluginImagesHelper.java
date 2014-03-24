/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.utils;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.net.URL;

import melnorme.util.swt.SWTUtil;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

/**
 * Helper to manage the images of a plugin
 */
public class PluginImagesHelper {
	
	protected final Bundle bundle;
	protected final IPath imagesPath;
	protected final boolean failOnMissingImage;
	protected final ImageRegistry imageRegistry;
	
	public PluginImagesHelper(Bundle bundle, IPath imagesPath, boolean failOnMissingImage) {
		this.bundle = assertNotNull(bundle);
		this.imagesPath = imagesPath;
		this.failOnMissingImage = failOnMissingImage;
		this.imageRegistry = new ImageRegistry(SWTUtil.getStandardDisplay());
	}
	
	public ImageRegistry getImageRegistry() {
		return imageRegistry;
	}
	
	protected static ImageDescriptor getImageDescriptor(Bundle bundle, IPath path, 
			boolean useMissingImageDescriptor) {
		URL url = FileLocator.find(bundle, path, null);
		if (url != null) {
			return ImageDescriptor.createFromURL(url);
		}
		if (useMissingImageDescriptor) {
			return ImageDescriptor.getMissingImageDescriptor();
		}
		return null;
	}
	
	protected ImageDescriptor createImageDescriptor(String prefix, String imagePath, 
			boolean useMissingImageDescriptor) {
		IPath path = imagesPath.append(prefix).append(imagePath);
		ImageDescriptor imageDescriptor = getImageDescriptor(bundle, path, useMissingImageDescriptor);
		if(failOnMissingImage) {
			assertNotNull(imageDescriptor); 
		}
		return imageDescriptor;
	}
	
	public ImageDescriptor createUnmanaged(String prefix, String imagePath) {
		return createUnmanaged(prefix, imagePath, false);
	}
	
	public ImageDescriptor createUnmanaged(String prefix, String imagePath, boolean useMissingImageDescriptor) {
		return createImageDescriptor(prefix, imagePath, useMissingImageDescriptor);
	}
	
	public String getKey(String prefix, String imagePath) {
		return new Path(prefix).append(imagePath).toString();
	}
	
	public String createManaged(String prefix, String imagePath) {
		ImageDescriptor result = createImageDescriptor(prefix, imagePath, false);
		assertNotNull(result);
		String key = getKey(prefix, imagePath); 
		imageRegistry.put(key, result);
		return key;
	}
	
	/** 
	 * Gets the image associated with given key.
	 * Note: must be called from display thread. 
	 */
	public Image getImage(String key) {
		return imageRegistry.get(key);
	}
	
	/** 
	 * Gets the image descriptor associated with given key.
	 */
	public ImageDescriptor getImageDescriptor(String key) {
		return imageRegistry.getDescriptor(key);
	}
	
}