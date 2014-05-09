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
package com.googlecode.goclipse.gocode;

import static melnorme.utilbox.core.CoreUtil.array;
import melnorme.lang.ide.ui.utils.ConsoleUtils;
import melnorme.lang.ide.ui.utils.ProcessMessageConsole;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IOConsoleOutputStream;

class GocodeMessageConsole extends ProcessMessageConsole {
	
	public final IOConsoleOutputStream clientResponse;
	public final IOConsoleOutputStream clientResponseErr;
		
	public GocodeMessageConsole(String name, ImageDescriptor imageDescriptor) {
		super(name, imageDescriptor);

		clientResponse = newOutputStream();
		clientResponseErr = newOutputStream();
		stdErr.setActivateOnWrite(false);
	}
	
	@Override
	protected void initOuputStreamColors() {
		stdErr.setColor(getColorManager().getColor(new RGB(200, 0, 0)));
		clientResponse.setColor(getColorManager().getColor(new RGB(100, 0, 150)));
		clientResponseErr.setColor(getColorManager().getColor(new RGB(200, 0, 0)));
	}
	
	public static GocodeMessageConsole getConsole() {
		GocodeMessageConsole console = ConsoleUtils.findConsole("gocode log", GocodeMessageConsole.class);
		if(console != null) {
			return console;
		}
		// no console, so create a new one
		GocodeMessageConsole msgConsole = new GocodeMessageConsole("gocode log", null);
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(array(msgConsole));
		return msgConsole;
	}
	
}