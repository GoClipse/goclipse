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
package melnorme.lang.ide.ui.utils;

import static melnorme.utilbox.core.CoreUtil.array;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;

public class ConsoleUtils {
	
	/** Finds an existing {@link MessageConsole} with given name. 
	 * If it doesn't exist, create a new one. */
	public static MessageConsole findOrCreateMessageConsole(String name) {
		MessageConsole console = findConsole(name, MessageConsole.class);
		if(console != null) {
			return console;
		}
		// no console, so create a new one
		MessageConsole msgConsole = new MessageConsole(name, null);
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { msgConsole });
		return msgConsole;
	}
	
	public static <T extends IConsole> T findConsole(String name, Class<T> klass) {
		IConsoleManager consoleMgr = ConsolePlugin.getDefault().getConsoleManager();
		IConsole[] existing = consoleMgr.getConsoles();
		for (IConsole console : existing) {
			if (name.equals(console.getName()) && klass.isAssignableFrom(console.getClass())) {
				return klass.cast(console);
			}
		}
		return null;
	}
	
	/** Create a new {@link MessageConsole}. 
	 * If an existing console already exists, remove it before adding a new one.
	 */
	public static MessageConsole recreateMessageConsole(String name, ImageDescriptor imageDescriptor) {
		MessageConsole console = findConsole(name, MessageConsole.class);
		if(console != null) {
			ConsolePlugin.getDefault().getConsoleManager().removeConsoles(array(console));
		}
		// create a new one
		MessageConsole msgConsole = new MessageConsole(name, imageDescriptor);
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(array(msgConsole));
		return msgConsole;
	}
	
}