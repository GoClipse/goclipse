/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.utils;

import static melnorme.utilbox.core.CoreUtil.areEqual;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

public class ImageImageDescriptor extends ImageDescriptor {
	
	protected final Image image;
	
	public ImageImageDescriptor(Image image) {
		super();
		this.image= image;
	}
	
	@Override
	public ImageData getImageData() {
		return image.getImageData();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof ImageImageDescriptor)) return false;
		
		ImageImageDescriptor other = (ImageImageDescriptor) obj;
		
		return areEqual(image, other.image);
	}
	
	@Override
	public int hashCode() {
		return image.hashCode();
	}
	
}