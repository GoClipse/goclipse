/*******************************************************************************
 * Copyright (c) 2008, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/

package melnorme.util.swt;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertEquals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;


public class SWTTestUtils {
	
	/** Runs the event queue executing all events that were pending when this method was called. 
	 * Note BM: This works under the assumption that {@link Display#asyncExec(Runnable)} puts the given runnable 
	 * at the end of the queue */
	public static void runPendingUIEvents(Display display) {
		assertEquals(display, Display.getCurrent());
		final boolean[] result = new boolean[1];
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				result[0] = true;
			}
		});
		while (result[0] == false) {
			display.readAndDispatch();
		}
	}
	
	public static void ________________flushUIEventQueue________________() {
		runPendingUIEvents(Display.getCurrent());
	}
	
	/** Runs the event queue until there are no events in the queue. 
	 * Note that it is possible that this method might not return, if new events keep being added to the queue. */
	public static void runEventQueueUntilEmpty(Display display) {
		assertEquals(display, Display.getCurrent());
		while (display.readAndDispatch()) {
		}
	}
	
	public static void runSWTEventLoopUntilBreak() {
		Display display = Display.getCurrent();
		
		final boolean[] breakLoopFlag = new boolean[1];
		
		display.addFilter(SWT.KeyDown, new Listener() {
			@Override
			public void handleEvent(Event event) {
				System.out.println("" + event.button + "--" + event.character  + " :: " + event.keyCode);
				if(event.keyCode == SWT.PAUSE) {
					breakLoopFlag[0] = true;
				}
			}
		});
		
		while(breakLoopFlag[0] == false) {
			if(!display.readAndDispatch())
				display.sleep();
		}
	}
	
	public static void clearEventQueue() {
		runEventQueueUntilEmpty(PlatformUI.getWorkbench().getDisplay());
	}
	
	public static void ________________clearEventQueue________________() {
		clearEventQueue();
	}
	
}
