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
package melnorme.lang.ide.ui.views;

import melnorme.util.swt.jface.text.ColorManager;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;

public interface StylerHelpers {
	
	/* ----------------- styler helpers ----------------- */
	
	/**
	 * Note: this doesn't dispose of created Colors, so to avoid leaking, 
	 * it should be used with constant RGBs only 
	 */
	public static class ForegroundColorStyler extends Styler {
		
		protected final RGB fgColor;
		protected final ColorManager registry = ColorManager.getDefault();
		
		public ForegroundColorStyler(RGB fgColor) {
			this.fgColor = fgColor;
		}
		
		@Override
		public void applyStyles(TextStyle textStyle) {
			if(fgColor != null) {
				textStyle.foreground = registry.getColor(fgColor);
			}
		}
		
	}
	
	public static ForegroundColorStyler fgColor(RGB rgb) {
		return new ForegroundColorStyler(rgb);
	}
	
	public static abstract class FontStyler extends Styler {
		
		protected Styler parentStyler;
		
		public FontStyler(Styler parentStyler) {
			this.parentStyler = parentStyler;
		}
		
		@Override
		public void applyStyles(TextStyle textStyle) {
			if(parentStyler != null) {
				parentStyler.applyStyles(textStyle);
			}
			
			Font font = textStyle.font;
			if(font == null) {
				font = JFaceResources.getDefaultFont(); 
			}
			FontDescriptor fontDescriptor = FontDescriptor.createFrom(font);
			fontDescriptor = getModifiedFontDescriptor(fontDescriptor);
			textStyle.font = fontDescriptor.createFont(Display.getCurrent());
		}
		
		protected abstract FontDescriptor getModifiedFontDescriptor(FontDescriptor fontDescriptor);
	}
	
	public static class ItalicStyler extends FontStyler {
		
		public ItalicStyler(Styler parentStyler) {
			super(parentStyler);
		}

		@Override
		protected FontDescriptor getModifiedFontDescriptor(FontDescriptor fontDescriptor) {
			return fontDescriptor.setStyle(SWT.ITALIC);
		}
	}
	
	public static class BoldStyler extends FontStyler {
		
		public BoldStyler(Styler parentStyler) {
			super(parentStyler);
		}
		
		@Override
		protected FontDescriptor getModifiedFontDescriptor(FontDescriptor fontDescriptor) {
			return fontDescriptor.setStyle(SWT.BOLD);
		}
	}
	
}