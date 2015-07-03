/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.util.swt.jface.resources;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

import melnorme.utilbox.misc.HashcodeUtil;

public class DecoratedImageDescriptor extends CompositeImageDescriptorExt {
	
	protected final ImageDescriptor overlayImage; 
	protected final Corner overlayCorner;
	
	public DecoratedImageDescriptor(ImageDescriptor baseImage, 
			ImageDescriptor overlay, Corner overlayCorner) {
		super(baseImage);
		
		this.overlayImage = assertNotNull(overlay);
		this.overlayCorner = assertNotNull(overlayCorner);
	}
	
	@Override
	protected Point getSize() {
		ImageData imageData = baseImage.getImageData();
		return new Point(imageData.width, imageData.height);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null || !getClass().equals(obj.getClass())) return false;
		
		DecoratedImageDescriptor other = (DecoratedImageDescriptor) obj;
		
		return 
				areEqual(baseImage, other.baseImage) &&
				areEqual(overlayImage, other.overlayImage) &&
				areEqual(overlayCorner, other.overlayCorner);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(baseImage, overlayImage, overlayCorner);
	}
	
	@Override
	protected void drawCompositeImage(int width, int height) {
		ImageData bg = getImageData(baseImage);
		drawImage(bg, 0, 0);
		
		drawOverlayAtCorner(overlayImage, overlayCorner);
	}
	
}