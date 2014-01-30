/*******************************************************************************
 * Copyright (c) 2007 DSource.org and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial implementation
 *******************************************************************************/
package melnorme.util.swt;

import java.util.Random;

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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * Miscelleanous class with some SWT utils.
 */
public class SWTUtil {
		
	/** Controls the enablement of composite color helpers. 
	 * (random backgroung color)*/
	public static boolean enableDebugColorHelpers = System.getProperty("SWTUTIL_DEBUG") != null;
	
	private static Random rnd = new Random(0);

	/** Sets the background color of the given Control to the system color
	 * of the given id, if color helpers are enabled. */
	public static void setDebugColor(Control control, int id) {
		if(enableDebugColorHelpers)
			control.setBackground(Display.getDefault().getSystemColor(id));
	}

	/** Sets the background color of the given Control to the given color,
	 * if color helpers are enabled. */
	public static void setDebugColor(Control control, Color color) {
		if(enableDebugColorHelpers)
			control.setBackground(color);
	}
	
	/** Sets the background color of the given Control to a random color,
	 * if color helpers are enabled. */
	public static void setRandomDebugColor(Control control) {
		if(enableDebugColorHelpers) {
			Color color = new Color(Display.getDefault(), 
					rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
			control.setBackground(color);
		}
	}

	/** Sets the enable state of the composite's children, recursively. */
	public static void recursiveSetEnabled(Composite composite, boolean enabled) {
		for(Control control : composite.getChildren() ) {
			if(control instanceof Composite) {
				recursiveSetEnabled((Composite) control, enabled);
			}
			control.setEnabled(enabled);
		}
	}


	/** Runs the runnable in the SWT thread. 
	 * (Simply runs the runnable if the current thread is the UI thread,
	 * otherwise calls the runnable in asyncexec.) */
	public static void runInSWTThread(Runnable runnable) {
		if(Display.getCurrent() == null) {
			Display.getDefault().asyncExec(runnable);
		} else {
			runnable.run();
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
	
	public static void reflowScrollContentWidth(final ScrolledComposite sc, final Composite scrollContent) {
		Rectangle rect = sc.getClientArea();
		sc.setMinSize(scrollContent.computeSize(rect.width, SWT.DEFAULT));
	}

	/** Show an info (OK button) MsgBox with given title and message. */
	public static int showInfoMsgBox(Shell shell, String title, String message) {
		MessageBox messageBox = new MessageBox(shell, SWT.OK);
		messageBox.setText(title);
		messageBox.setMessage(message);
		return messageBox.open(); 
	}

}