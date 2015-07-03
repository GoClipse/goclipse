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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

import melnorme.lang.ide.ui.LangImages;
import melnorme.lang.tooling.EAttributeFlag;
import melnorme.lang.tooling.EProtection;
import melnorme.lang.tooling.ElementAttributes;
import melnorme.utilbox.misc.HashcodeUtil;

public class LangElementImageDescriptor extends CompositeImageDescriptorExt {
	
	public static final Point DEFAULT_SIZE = new Point(22, 16);
	
	protected final Point size;
	protected final ElementAttributes elementAttributes;
	
	
	public LangElementImageDescriptor(ImageDescriptor baseImage, ElementAttributes elementData) {
		this(DEFAULT_SIZE, baseImage, elementData);
	}
	
	public LangElementImageDescriptor(Point size, ImageDescriptor baseImage, ElementAttributes elementAttributes) {
		super(baseImage);
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
		if(obj == null || !getClass().equals(obj.getClass())) return false;
		
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
	
	@Override
	protected void drawCompositeImage(int width, int height) {
		ImageData bg = getImageData(baseImage);
		drawImage(bg, 0, 0);
		
		
		resetDrawPosition(Corner.TOP_LEFT);
		drawTopLeftDecorations();
		
		resetDrawPosition(Corner.TOP_RIGHT);
		drawTopRightDecorations();
		
		resetDrawPosition(Corner.BOTTOM_LEFT);
		drawBottomLeftDecorations();
		
		resetDrawPosition(Corner.BOTTOM_RIGHT);
		drawBottomRightDecorations();
	}
	
	/* -----------------  ----------------- */
	
	protected void drawTopLeftDecorations() {
		
		if(elementAttributes.hasFlag(EAttributeFlag.TEMPLATED)) {
			drawOverlayAtCursor(LangImages.DESC_OVR_TEMPLATED, Corner.TOP_LEFT);
		}
	}
	
	protected void drawTopRightDecorations() {
		
		if(elementAttributes.hasFlag(EAttributeFlag.CONST)) {
			drawOverlayAtCursor(LangImages.DESC_OVR_CONST, Corner.TOP_RIGHT);
		}
		if(elementAttributes.hasFlag(EAttributeFlag.IMMUTABLE)) {
			drawOverlayAtCursor(LangImages.DESC_OVR_IMMUTABLE, Corner.TOP_RIGHT);
		}
		
		if(elementAttributes.hasFlag(EAttributeFlag.FINAL)) {
			drawOverlayAtCursor(LangImages.DESC_OVR_FINAL, Corner.TOP_RIGHT);
		}
		if(elementAttributes.hasFlag(EAttributeFlag.ABSTRACT)) {
			drawOverlayAtCursor(LangImages.DESC_OVR_ABSTRACT, Corner.TOP_RIGHT);
		}
		if(elementAttributes.hasFlag(EAttributeFlag.STATIC)) {
			drawOverlayAtCursor(LangImages.DESC_OVR_STATIC, Corner.TOP_RIGHT);
		}
	}
	
	protected void drawBottomLeftDecorations() {
		
		if(elementAttributes.hasFlag(EAttributeFlag.ALIASED)) {
			drawOverlayAtCursor(LangImages.DESC_OVR_ALIAS, Corner.BOTTOM_LEFT);
		}
	}
	
	protected void drawBottomRightDecorations() {
		
		ImageDescriptor protectionDecoration = getProtectionDecoration();
		if(protectionDecoration != null) {
			drawOverlayAtCursor(protectionDecoration, Corner.BOTTOM_RIGHT);
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
	
	public static ImageDescriptor getProtectionDecoration_Small(EProtection protection) {
		if(protection == null)
			return null;
		
		switch (protection) {
		
		case PRIVATE: return LangImages.DESC_OVR_PRIVATE_SMALL;
		case PROTECTED: return LangImages.DESC_OVR_PROTECTED_SMALL;
		case PACKAGE: return LangImages.DESC_OVR_DEFAULT_SMALL;
		case PUBLIC:
			default: return null;
		}
	}
	
}