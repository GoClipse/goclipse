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
package melnorme.lang.ide.ui.engine.console;

import java.io.IOException;

import melnorme.lang.ide.ui.utils.AbstractProcessMessageConsole;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.part.IPageBookViewPage;

public class LangEngineConsole extends AbstractProcessMessageConsole {
	
	public final IOConsoleOutputStream infoOut;
	
	public LangEngineConsole(String name, ImageDescriptor imageDescriptor) {
		super(name, imageDescriptor);
		
		infoOut = newOutputStream();
		postToUI_initOutputStreamColors();
	}
	
	@Override
	protected void ui_initStreamColors() {
		infoOut.setColor(EngineConsolePrefs.INFO_COLOR.getManagedColor());
		stdErr.setColor(EngineConsolePrefs.STDERR_COLOR.getManagedColor());
		stdOut.setColor(EngineConsolePrefs.STDOUT_COLOR.getManagedColor());
		setBackground(EngineConsolePrefs.BACKGROUND_COLOR.getManagedColor());
	}
	
	@Override
	public IPageBookViewPage createPage(IConsoleView view) {
		return new EngineConsolePage(this, view);
	}
	
	public void writeOperationInfo(String string) {
		try {
			infoOut.write(string);
		} catch (IOException e) {
			// Do nothing
		}
	}
	
}