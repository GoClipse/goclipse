package com.googlecode.goclipse.editors;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public interface IGoSourceColorConstants {
	// Potential: 468966 FFF0A5 FFB03B B64926 8E2800
	
	final Color DEFAULT    = new Color(Display.getCurrent(), 0x00, 0x00, 0x00);
	final Color NUMBER     = new Color(Display.getCurrent(), 0xC0, 0x00, 0x00);
	final Color NUMBER_OCT = new Color(Display.getCurrent(), 0x80, 0x00, 0x00);
	final Color NUMBER_HEX = new Color(Display.getCurrent(), 0xC0, 0x00, 0x00);
	final Color NUMBER_UNR = new Color(Display.getCurrent(), 0x80, 0x00, 0x00);
	final Color STRING     = new Color(Display.getCurrent(), 0x00, 0x80, 0x00);
	final Color KEYWORD    = new Color(Display.getCurrent(), 0x00, 0x00, 0x80);
	final Color TYPE       = new Color(Display.getCurrent(), 0x00, 0x40, 0x00);
	final Color COMMENT    = new Color(Display.getCurrent(), 0x80, 0x80, 0x80);
}
