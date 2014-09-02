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
package melnorme.lang.ide.ui.build;

import java.io.IOException;

import melnorme.lang.ide.ui.utils.AbstractProcessMessageConsole;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.console.IOConsoleOutputStream;

public class LangOperationConsole extends AbstractProcessMessageConsole {
	
	public final IOConsoleOutputStream metaOut;
	
	public LangOperationConsole(String name, ImageDescriptor imageDescriptor) {
		super(name, imageDescriptor);
		
		metaOut = newOutputStream();
		postToUI_initOutputStreamColors();
	}
	
	public void writeOperationInfo(String string) {
		try {
			metaOut.write(string);
		} catch (IOException e) {
			// Do nothing
		}
	}
	
}