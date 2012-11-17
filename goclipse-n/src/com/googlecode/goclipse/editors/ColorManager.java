package com.googlecode.goclipse.editors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * 
 * @author steel
 */
public class ColorManager {

	protected Map<RGB, Color> fColorTable = new HashMap<RGB, Color>(10);
	
	public static final ColorManager INSTANCE = new ColorManager();
	
	private ColorManager(){}
	
	public void dispose() {
		Iterator<Color> e = fColorTable.values().iterator();
		while (e.hasNext())
			e.next().dispose();
	}

	public Color getColor(RGB rgb) {
		Color color = fColorTable.get(rgb);
		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			fColorTable.put(rgb, color);
		}
		return color;
	}
}
