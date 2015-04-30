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
package melnorme.lang.ide.ui.views;


import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.navigator.IDescriptionProvider;

public abstract class AbstractLangLabelProvider extends LabelProvider 
		implements IStyledLabelProvider, IDescriptionProvider {
	
	public AbstractLangLabelProvider() {
		super();
	}
	
	@Override
	public String getText(Object element) {
		StyledString styledText = getStyledText(element);
		if(styledText != null) {
			return styledText.getString();
		}
		return null;
	}
	
	@Override
	public abstract StyledString getStyledText(Object element);
	
	/* ----------------- styler helpers ----------------- */
	
	protected static class ForegroundColorStyler extends Styler {
		protected final RGB fgColor;
		
		public ForegroundColorStyler(RGB fgColor) {
			this.fgColor = fgColor;
		}
		
		@Override
		public void applyStyles(TextStyle textStyle) {
			if(fgColor != null) {
				/*FIXME: BUG here Color leak*/
				textStyle.foreground = new Color(Display.getCurrent(), fgColor);
			}
		}
	}
	
	public static ForegroundColorStyler fgColor(RGB rgb) {
		return new ForegroundColorStyler(rgb);
	}
	
	protected static abstract class FontStyler extends Styler {
		
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
	
	/* ----------------- description ----------------- */
	
	// By default, use full string of styled text  
	@Override
	public String getDescription(Object anElement) {
		
		StyledString styledText = getStyledText(anElement);
		if(styledText != null) {
			return styledText.getString();
		}
		return null;
	}
	
}