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
package melnorme.util.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * SWT helper methos specifically for the creation of Controls
 */
public class SWTFactoryUtil_Old extends SWTUtil {
	
	/** Controls the enablement of composite color helpers. 
	 * (random backgroung color)*/
	public static boolean enableDebugColorHelpers = System.getProperty("SWTUTIL_DEBUG") != null;
	
	/** Sets the background color of the given Control to the system color
	 * of the given id, if color helpers are enabled. */
	public static void setDebugColor(Control control, int id) {
		if(enableDebugColorHelpers) {
			control.setBackground(Display.getDefault().getSystemColor(id));
		}
	}

	/** Sets the background color of the given Control to the given color,
	 * if color helpers are enabled. */
	public static void setDebugColor(Control control, Color color) {
		if(enableDebugColorHelpers) {
			control.setBackground(color);
		}
	}
	
	/** Creates a ScrolledComposite with a properly configured interior Composite.
	 * If dynamicInteriorResize automatically resizes interior Composite. */
	public static ScrolledComposite createScrollComposite(final Composite parent,
			boolean dynamicInteriorWidthReflow) {
		final ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.BORDER);
		final Composite scrollContent = new Composite(sc, SWT.NONE);
		sc.setContent(scrollContent);
		setDebugColor(sc, SWT.COLOR_MAGENTA);
		setDebugColor(scrollContent, SWT.COLOR_DARK_CYAN);
		
		
		if(dynamicInteriorWidthReflow) {
			sc.setExpandHorizontal(true);
			sc.setExpandVertical(true);
			sc.addControlListener(new ControlAdapter() {
				@Override
				public void controlResized(ControlEvent e) {
					reflowScrollContentWidth(sc, scrollContent);
				}
			});
		}
		
		RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.wrap = false;
		scrollContent.setLayout(layout);
		return sc;
	}
	
	protected static void reflowScrollContentWidth(final ScrolledComposite sc, final Composite scrollContent) {
		Rectangle rect = sc.getClientArea();
		sc.setMinSize(scrollContent.computeSize(rect.width, SWT.DEFAULT));
	}
	
}