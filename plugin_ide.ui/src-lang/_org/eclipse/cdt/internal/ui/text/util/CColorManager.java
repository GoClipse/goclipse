/*******************************************************************************
 *  Copyright (c) 2000, 2009 IBM Corporation and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 *  Contributors:
 *     IBM Corporation - initial API and implementation
 *     QNX Software System
 *     Anton Leherbauer (Wind River Systems)
 *******************************************************************************/
package _org.eclipse.cdt.internal.ui.text.util;
 
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import _org.eclipse.cdt.ui.text.IColorManager;


/**
 * CDT color manager.
 */
public class CColorManager implements IColorManager {
	
	protected Map<String, RGB> fKeyTable= new HashMap<String, RGB>(10);
	protected Map<Display, Map<RGB, Color>> fDisplayTable= new HashMap<Display, Map<RGB, Color>>(2);
	
	/**
	 * Flag which tells if the colors are automatically disposed when
	 * the current display gets disposed.
	 */
	private boolean fAutoDisposeOnDisplayDispose;

	/**
	 * Creates a new CDT color manager which automatically
	 * disposes the allocated colors when the current display
	 * gets disposed.
	 */
	public CColorManager() {
		this(true);
	}

	/**
	 * Creates a new CDT color manager.
	 *
	 * @param autoDisposeOnDisplayDispose 	if <code>true</code>  the color manager
	 * automatically disposes all managed colors when the current display gets disposed
	 * and all calls to {@link org.eclipse.jface.text.source.ISharedTextColors#dispose()} are ignored.
	 *
	 * @since 4.0
	 */
	public CColorManager(boolean autoDisposeOnDisplayDispose) {
		fAutoDisposeOnDisplayDispose= autoDisposeOnDisplayDispose;
	}

	protected void dispose(Display display) {		
		Map<RGB, Color> colorTable= fDisplayTable.get(display);
		if (colorTable != null) {
			Iterator<Color> e= colorTable.values().iterator();
			while (e.hasNext())
				(e.next()).dispose();
		}
	}
	
	/*
	 * @see IColorManager#getColor(RGB)
	 */
	@Override
	public Color getColor(RGB rgb) {
		
		if (rgb == null)
			return null;
		
		final Display display= Display.getCurrent();
		Map<RGB, Color> colorTable= fDisplayTable.get(display);
		if (colorTable == null) {
			colorTable= new HashMap<RGB, Color>(10);
			fDisplayTable.put(display, colorTable);
			if (fAutoDisposeOnDisplayDispose) {
				display.disposeExec(new Runnable() {
					@Override
					public void run() {
						dispose(display);
					}
				});
			}
		}
		
		Color color= colorTable.get(rgb);
		if (color == null) {
			color= new Color(Display.getCurrent(), rgb);
			colorTable.put(rgb, color);
		}
		
		return color;
	}
	
	/*
	 * @see IColorManager#dispose
	 */
	@Override
	public void dispose() {
		if (!fAutoDisposeOnDisplayDispose)
			dispose(Display.getCurrent());
	}

	/*
	 * @see IColorManager#getColor(String)
	 */
	@Override
	public Color getColor(String key) {
		
		if (key == null)
			return null;
			
		RGB rgb= fKeyTable.get(key);
		return getColor(rgb);
	}
	
	/*
	 * @see IColorManager#bindColor(String, RGB)
	 */
	@Override
	public void bindColor(String key, RGB rgb) {
		Object value= fKeyTable.get(key);
		if (value != null)
			throw new UnsupportedOperationException();
		
		fKeyTable.put(key, rgb);
	}

	/*
	 * @see IColorManager#unbindColor(String)
	 */
	@Override
	public void unbindColor(String key) {
		fKeyTable.remove(key);
	}
}


