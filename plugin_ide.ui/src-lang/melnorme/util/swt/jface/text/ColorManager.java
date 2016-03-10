/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.util.swt.jface.text;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class ColorManager implements ISharedTextColors {
	
	private static final ColorManager defaultInstance = new ColorManager();
	
	public static ColorManager getDefault() {
		return assertNotNull(defaultInstance);
	}
	
	protected final Map<RGB, Color> colors = new HashMap<>(16);
	
	public ColorManager() {
		// Note: no SWT code in constructor
	}
	
	@Override
	public Color getColor(RGB rgb) {
		Color color = colors.get(rgb);
		if(color == null) {
			color = new Color(Display.getCurrent(), rgb);
			colors.put(rgb, color);
		}
		return color;
	}
	
	@Override
	public void dispose() {
		for (Color color : colors.values()) {
			color.dispose();
		}
	}
	
}