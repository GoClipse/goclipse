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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Miscelleanous SWT utilities.
 */
public class SWTUtil {
	
	/** @return true if a control can be used: is neither null, nor disposed. */
	public static boolean isOkToUse(Control control) {
		return control != null && !control.isDisposed();
	}
	
	/**
	 * Returns the standard display to be used. The method first checks, if
	 * the thread calling this method has an associated disaply. If so, this
	 * display is returned. Otherwise the method returns the default display.
	 */
	public static Display getStandardDisplay() {
		Display display = Display.getCurrent();
		if (display == null)
			display = Display.getDefault();
		return display;		
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

	/** Show an info (OK button) MsgBox with given title and message. */
	public static int showInfoMsgBox(Shell shell, String title, String message) {
		MessageBox messageBox = new MessageBox(shell, SWT.OK);
		messageBox.setText(title);
		messageBox.setMessage(message);
		return messageBox.open(); 
	}
	
	
	/**
	 * Post {@link Control#setFocus()} on the UI thread, if current thread not UI thread.
	 */
	public static void post_setFocus(final Text control) {
		if(!isOkToUse(control)) {
			return;
		}
		
		Display display = control.getDisplay();
		if(display == Display.getCurrent()) {
			control.setFocus();
		} else {
			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					if(!control.isDisposed()) {
						control.setFocus();
					}
				}
			});
		}
	}
	
}