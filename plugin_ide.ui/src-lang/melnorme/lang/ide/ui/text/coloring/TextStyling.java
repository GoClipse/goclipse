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
package melnorme.lang.ide.ui.text.coloring;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import melnorme.util.swt.jface.text.ColorManager2;

public class TextStyling {
	
	public final boolean isEnabled;
	public final RGB rgb;
	public final boolean isBold;
	public final boolean isItalic;
	public final boolean isStrikethrough;
	public final boolean isUnderline;
	
	public TextStyling(RGB rgb, boolean bold, boolean italic) {
		this.isEnabled = true;
		this.rgb = assertNotNull(rgb);
		this.isBold = bold;
		this.isItalic = italic;
		this.isStrikethrough = false;
		this.isUnderline = false;
	}
	
	public TextStyling(boolean isEnabled, RGB rgb, boolean isBold, boolean isItalic, boolean isStrikethrough, 
			boolean isUnderline) {
		this.isEnabled = isEnabled;
		this.rgb = assertNotNull(rgb);
		this.isBold = isBold;
		this.isItalic = isItalic;
		this.isStrikethrough = isStrikethrough;
		this.isUnderline = isUnderline;
	}
	
	public TextStyling(TextStylingData data) {
		this(data.isEnabled, data.rgb, data.isBold, data.isItalic, data.isStrikethrough, data.isUnderline);
	}
	
	public static class TextStylingData {
		public boolean isEnabled;
		public RGB rgb;
		public boolean isBold;
		public boolean isItalic;
		public boolean isStrikethrough;
		public boolean isUnderline;
	}
	
	public TextStylingData getData() {
		TextStylingData data = new TextStylingData();
		data.isEnabled = isEnabled;
		data.rgb = rgb;
		data.isBold = isBold;
		data.isItalic = isItalic;
		data.isStrikethrough = isStrikethrough;
		data.isUnderline = isUnderline;
		return data;
	}
	
	public TextAttribute getTextAttribute(ColorManager2 colorManager) {
		Color color = colorManager.getColor(rgb);
		
		return createTextAttribute(color, isBold, isItalic, isStrikethrough, isUnderline);
	}
	
	public static TextAttribute createTextAttribute(Color color, boolean isBold, boolean isItalic, 
			boolean isStrikethrough, boolean isUnderline) {
		int style = SWT.NORMAL;
		
		style |= isBold ? SWT.BOLD : 0;
		style |= isItalic ? SWT.ITALIC : 0;
		style |= isStrikethrough ? TextAttribute.STRIKETHROUGH : 0;
		style |= isUnderline ? TextAttribute.UNDERLINE : 0;
		
		return new TextAttribute(color, null, style);
	}
	
}