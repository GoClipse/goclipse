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
package melnorme.util.swt.jface.text;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import melnorme.utilbox.ownership.IDisposable;

public class ColorManager2 implements ISharedTextColors, IDisposable {
	
	protected ColorRegistryExt _colorRegistry;
	
	public ColorManager2() {
	}
	
	public ColorRegistryExt getColorRegistry() {
		if(_colorRegistry == null) {
			_colorRegistry = new ColorRegistryExt();
		}
		return _colorRegistry;
	}
	
//	@Override
	public Color getColor(String key) {
		return getColorRegistry().get(key);
	}

	public void putColor(String key, RGB rgb) {
		getColorRegistry().put(key, rgb);
	}
	
	public Color putAndGetColor(String key, RGB rgb) {
		putColor(key, rgb);
		return getColor(key);
	}
	
	@Override
	public Color getColor(RGB rgb) {
		String rgbSymbolicName = rgb.toString();
		return putAndGetColor(rgbSymbolicName, rgb);
	}
	
	@Override
	public void dispose() {
		getColorRegistry().clearCaches();
	}
	
	/* -----------------  ----------------- */
	
	public static class ColorRegistryExt extends ColorRegistry {
		@Override
		public void clearCaches() {
			super.clearCaches();
		}
	}

}