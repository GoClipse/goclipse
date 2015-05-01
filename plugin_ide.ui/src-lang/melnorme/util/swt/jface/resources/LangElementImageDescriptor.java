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
import static melnorme.utilbox.core.CoreUtil.areEqual;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.LangImages;
import melnorme.lang.tooling.EAttributeFlag;
import melnorme.lang.tooling.EProtection;
import melnorme.lang.tooling.ElementAttributes;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.misc.HashcodeUtil;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

public class LangElementImageDescriptor extends CompositeImageDescriptor {
	
	public static final Point DEFAULT_SIZE = new Point(22, 16);
	
	protected final Point size;
	protected final ImageDescriptor baseImage;
	protected final ElementAttributes elementAttributes;
	
	
	public LangElementImageDescriptor(ImageDescriptor baseImage, ElementAttributes elementData) {
		this(DEFAULT_SIZE, baseImage, elementData);
	}
	
	public LangElementImageDescriptor(Point size, ImageDescriptor baseImage, ElementAttributes elementAttributes) {
		super();
		this.baseImage = assertNotNull(baseImage);
		this.size = assertNotNull(size);
		this.elementAttributes = assertNotNull(elementAttributes);
	}
	
	@Override
	protected Point getSize() {
		return size;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof LangElementImageDescriptor)) return false;
		
		LangElementImageDescriptor other = (LangElementImageDescriptor) obj;
		
		return 
				areEqual(baseImage, other.baseImage) &&
				areEqual(size, other.size) &&
				areEqual(elementAttributes, other.elementAttributes);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(baseImage, size, elementAttributes);
	}
	
	/* -----------------  ----------------- */
	
	protected int xOffset;
	protected int yOffset;
	
	@Override
	protected void drawCompositeImage(int width, int height) {
		ImageData bg = getImageData(baseImage);
		drawImage(bg, 0, 0);
		
		
		xOffset = 0;
		yOffset = 0;
		drawTopLeftDecorations();
		
		xOffset = getSize().x;
		yOffset = 0;
		drawTopRightDecorations();
		
		xOffset = 0;
		yOffset = getSize().y;
		drawBottomLeftDecorations();
		
		xOffset = getSize().x;
		yOffset = getSize().y;
		drawBottomRightDecorations();
	}
	
	protected ImageData getImageData(ImageDescriptor desc) {
		ImageData imageData = desc.getImageData();
		if(imageData == null) {
			LangCore.logError("Could not get ImageData.");
			return new ImageData(1, 1, 32, DEFAULT_IMAGE_DATA.palette);
		}
		return imageData;
	}
	
	protected void drawDecorationImage(ImageDescriptor desc, Direction direction, boolean drawBelow) {
		ImageData imageData = getImageData(desc);
		if(direction == Direction.LEFT) {
			xOffset -= imageData.width;
		}
		int ypos = drawBelow ? yOffset : yOffset - imageData.height;
		drawImage(imageData, xOffset, ypos);
		
		if(direction == Direction.RIGHT) {
			xOffset += imageData.width;
		}
	}
	
	protected static enum Direction {
		LEFT,
		RIGHT,
	}
	
	protected void drawImageSequence(Indexable<ImageDescriptor> images, Direction direction, boolean drawBelow, 
			int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		
		for(ImageDescriptor desc : images) {
			drawDecorationImage(desc, direction, drawBelow);
		}
	}
	
	/* -----------------  ----------------- */
	
	protected void drawTopLeftDecorations() {
		
		if(elementAttributes.hasFlag(EAttributeFlag.TEMPLATED)) {
			drawDecorationImage(LangImages.DESC_OVR_TEMPLATED, Direction.RIGHT, true);
		}
	}
	
	protected void drawTopRightDecorations() {
		
		if(elementAttributes.hasFlag(EAttributeFlag.CONST)) {
			drawDecorationImage(LangImages.DESC_OVR_CONST, Direction.LEFT, true);
		}
		if(elementAttributes.hasFlag(EAttributeFlag.IMMUTABLE)) {
			drawDecorationImage(LangImages.DESC_OVR_IMMUTABLE, Direction.LEFT, true);
		}
		
		if(elementAttributes.hasFlag(EAttributeFlag.FINAL)) {
			drawDecorationImage(LangImages.DESC_OVR_FINAL, Direction.LEFT, true);
		}
		if(elementAttributes.hasFlag(EAttributeFlag.ABSTRACT)) {
			drawDecorationImage(LangImages.DESC_OVR_ABSTRACT, Direction.LEFT, true);
		}
		if(elementAttributes.hasFlag(EAttributeFlag.STATIC)) {
			drawDecorationImage(LangImages.DESC_OVR_STATIC, Direction.LEFT, true);
		}
	}
	
	protected void drawBottomLeftDecorations() {
		
		if(elementAttributes.hasFlag(EAttributeFlag.ALIAS)) {
			drawDecorationImage(LangImages.DESC_OVR_ALIAS, Direction.RIGHT, false);
		}
	}
	
	protected void drawBottomRightDecorations() {
		
		ImageDescriptor protectionDecoration = getProtectionDecoration();
		if(protectionDecoration != null) {
			drawDecorationImage(protectionDecoration, Direction.LEFT, false);
		}
	}
	
	protected ImageDescriptor getProtectionDecoration() {
		return getProtectionDecoration(elementAttributes.getProtection());
	}
	
	public static ImageDescriptor getProtectionDecoration(EProtection protection) {
		if(protection == null)
			return null;
		
		switch (protection) {
		
		case PRIVATE: return LangImages.DESC_OVR_PRIVATE;
		case PROTECTED: return LangImages.DESC_OVR_PROTECTED;
		case PACKAGE: return LangImages.DESC_OVR_DEFAULT;
		case PUBLIC:
			default: return null;
		}
	}
	
}