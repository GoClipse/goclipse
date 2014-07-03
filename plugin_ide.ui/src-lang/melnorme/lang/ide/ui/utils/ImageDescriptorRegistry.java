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

import java.util.HashMap;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * An image cache, indexed by ImageDescriptor
 */
public class ImageDescriptorRegistry {
	
	protected static final HashMap<ImageDescriptor, Image> imageMap = new HashMap<>(16);
	
	public Image getImage(ImageDescriptor imageDescriptor) {
		Image image = imageMap.get(imageDescriptor);
		
		if (image == null) {
			image = imageDescriptor.createImage();
			imageMap.put(imageDescriptor, image);
		}
		
		return image;
	}
	
	public void dispose() {
		for (Image image : imageMap.values()) {
			image.dispose();
		}
		imageMap.clear();
	}
	
}