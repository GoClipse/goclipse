/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
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
import static melnorme.utilbox.core.Assert.AssertNamespace.assertUnreachable;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;

import melnorme.lang.ide.core.LangCore;
import melnorme.utilbox.collections.Indexable;

public abstract class CompositeImageDescriptorExt extends CompositeImageDescriptor {
	
	protected final ImageDescriptor baseImage;
	
	public CompositeImageDescriptorExt(ImageDescriptor baseImage) {
		super();
		this.baseImage = assertNotNull(baseImage);
	}
	
	protected ImageData getImageData(ImageDescriptor desc) {
		ImageData imageData = desc.getImageData();
		if(imageData == null) {
			LangCore.logError("Could not get ImageData.");
			return new ImageData(1, 1, 32, DEFAULT_IMAGE_DATA.palette);
		}
		return imageData;
	}
	
	/* -----------------  ----------------- */
	
	public static enum Corner {
		TOP_LEFT,
		TOP_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_RIGHT,
		
		;
		public boolean isTop() {
			return this == TOP_LEFT || this == TOP_RIGHT;
		}
		
		public boolean isBottom() {
			return !isTop();
		}
		
		public boolean isLeft() {
			return this == TOP_LEFT || this == BOTTOM_LEFT;
		}
		
		public boolean isRight() {
			return !isLeft();
		}

	}
	
	public static enum HorizontalDirection {
		LEFT,
		RIGHT,
	}
	
	public static enum VerticalDirection {
		TOP,
		BOTTOM,
	}
	
	protected int xOffset;
	protected int yOffset;
	
	public void resetDrawPosition(Corner corner) {
		switch(corner) {
		case TOP_LEFT:
			xOffset = 0;
			yOffset = 0;
			return;
		case TOP_RIGHT:
			xOffset = getSize().x;
			yOffset = 0;
			return;
		case BOTTOM_LEFT:
			xOffset = 0;
			yOffset = getSize().y;
			return;
		case BOTTOM_RIGHT:
			xOffset = getSize().x;
			yOffset = getSize().y;
			return;
		}
		throw assertUnreachable();
	}
	
	public void drawOverlayAtCorner(ImageDescriptor overlayImage, Corner corner) {
		resetDrawPosition(corner);
		drawOverlayAtCursor(overlayImage, corner);
	}

	protected void drawOverlayAtCursor(ImageDescriptor overlayImage, Corner corner) {
		ImageData imageData = getImageData(overlayImage);
		if(corner.isRight()) {
			xOffset -= imageData.width;
		}
		int ypos = corner.isTop() ? yOffset : yOffset - imageData.height;
		drawImage(imageData, xOffset, ypos);
		
		if(corner.isLeft()) {
			xOffset += imageData.width;
		}
	}

	
	protected void drawImageSequence(Indexable<ImageDescriptor> images, Corner corner, 
			int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		
		for(ImageDescriptor desc : images) {
			drawOverlayAtCursor(desc, corner);
		}
	}
	
}