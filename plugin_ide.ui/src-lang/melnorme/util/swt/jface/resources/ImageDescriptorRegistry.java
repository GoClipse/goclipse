/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Patrick Chuong (Texas Instruments) - Bug 292411
 *******************************************************************************/
package melnorme.util.swt.jface.resources;

 
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import melnorme.lang.ide.ui.LangUIPlugin;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * A registry that maps <code>ImageDescriptors</code> to <code>Image</code>.
 */
public class ImageDescriptorRegistry {

	private Map<ImageDescriptor, Image> fRegistry = Collections.synchronizedMap(new HashMap<ImageDescriptor, Image>(10));
	private Display fDisplay;
	
	/**
	 * Creates a new image descriptor registry for the current or default display,
	 * respectively.
	 */
	public ImageDescriptorRegistry() {
		this(LangUIPlugin.getStandardDisplay());
	}
	
	/**
	 * Creates a new image descriptor registry for the given display. All images
	 * managed by this registry will be disposed when the display gets disposed.
	 * 
	 * @param display the display the images managed by this registry are allocated for 
	 */
	public ImageDescriptorRegistry(Display display) {
		fDisplay= display;
		Assert.isNotNull(fDisplay);
		hookDisplay();
	}
	
	/**
	 * Returns the image associated with the given image descriptor.
	 * 
	 * @param descriptor the image descriptor for which the registry manages an image
	 * @return the image associated with the image descriptor or <code>null</code>
	 *  if the image descriptor can't create the requested image.
	 */
	public Image get(ImageDescriptor descriptor) {
		if (descriptor == null) {
			descriptor= ImageDescriptor.getMissingImageDescriptor();
		}
			
		Image result= fRegistry.get(descriptor);
		if (result != null) {
			return result;
		}
	
		Assert.isTrue(fDisplay == LangUIPlugin.getStandardDisplay(), "Allocating image for wrong display"); 
		result= descriptor.createImage();
		if (result != null) {
			fRegistry.put(descriptor, result);
		}
		return result;
	}

	/**
	 * Disposes all images managed by this registry.
	 */	
	public void dispose() {
		for (Image image : fRegistry.values()) {
			image.dispose();
		}
		fRegistry.clear();
	}
	
	private void hookDisplay() {
		fDisplay.asyncExec(new Runnable() {
			@Override
			public void run() {
				fDisplay.disposeExec(new Runnable() {

					@Override
					public void run() {
						dispose();
					}
				});
			}
		});
	}
}

