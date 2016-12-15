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
package melnorme.lang.ide.ui.tools.console;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.io.IOException;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.part.IPageBookViewPage;

import melnorme.lang.ide.ui.text.coloring.TextStyling;
import melnorme.util.swt.jface.text.ColorManager;
import melnorme.utilbox.core.DevelopmentCodeMarkers;
import melnorme.utilbox.ownership.OwnedObjects;

public class ToolsConsole extends AbstractProcessMessageConsole {
	
	public final IOConsoleOutputStreamExt infoOut;
	public final IOConsoleOutputStreamExt stdErr_silent; // An alternative to stdErr that never activate on write
	
	protected final OwnedObjects owned = new OwnedObjects();
	
	public ToolsConsole(String name, ImageDescriptor imageDescriptor) {
		this(name, imageDescriptor, true);
	}
	
	protected ToolsConsole(String name, ImageDescriptor imageDescriptor, boolean initializeColors) {
		super(name, imageDescriptor);
		
		infoOut = new IOConsoleOutputStreamExt(newOutputStream());
		stdErr_silent = new IOConsoleOutputStreamExt(newOutputStream());
		
		if(initializeColors) {
			postToUI_initOutputStreamColors();
		}
	}
	
	@Override
	protected void ui_initStreamColors() {
		super.ui_initStreamColors();
		
		ui_bindActivateOnErrorsListeners();
		
		ToolsConsolePrefs.INFO_COLOR.asField().bindOwnedListener(owned, true, 
			(newValue) -> infoOut.console().setColor(getManagedColor(newValue)));
		ToolsConsolePrefs.STDERR_COLOR.asField().bindOwnedListener(owned, true, 
			(newValue) -> {
				stdErr.console().setColor(getManagedColor(newValue));
				stdErr_silent.console().setColor(getManagedColor(newValue));
			});
		ToolsConsolePrefs.STDOUT_COLOR.asField().bindOwnedListener(owned, true, 
			(newValue) -> stdOut.console().setColor(getManagedColor(newValue)));
		ToolsConsolePrefs.BACKGROUND_COLOR.asField().bindOwnedListener(owned, true, 
			(newValue) -> setBackground(getManagedColor(newValue)));
	}
	
	protected void ui_bindActivateOnErrorsListeners() {
		ToolsConsolePrefs.ACTIVATE_ON_ERROR_MESSAGES.asField().bindOwnedListener(owned, true,
			(newValue) -> stdErr.console().setActivateOnWrite(newValue)
		);
	}
	
	protected static Color getManagedColor(TextStyling textStyling) {
		return ColorManager.getDefault().getColor(textStyling.rgb);
	}
	
	@Override
	protected void disposeDo() {
		assertTrue(Display.getCurrent() != null);
		owned.dispose(); // owned is modified from UI thread (in initialization), so we dispose it in UI thread too.
		
		super.disposeDo();
	}
	
	@Override
	public IPageBookViewPage createPage(IConsoleView view) {
		return new ToolsConsolePage(this, view);
	}
	
	public void writeOperationInfo(String string) {
		infoOut.write(string);
	}
	
	public static class IOConsoleOutputStreamExt {
		
		protected IOConsoleOutputStream console;

		public IOConsoleOutputStreamExt(IOConsoleOutputStream console) {
			this.console = assertNotNull(console);
		}
		
		public IOConsoleOutputStream console() {
			return console;
		}
		
		public boolean isIgnorinCommands() {
			return !PlatformUI.isWorkbenchRunning() && DevelopmentCodeMarkers.TESTS_MODE;
		}
		
		public void write(byte[] b, int off, int len) {
			if(isIgnorinCommands()) {
				return;
			}

			try {
				console.write(b, off, len);
			} catch(IOException e) {
				// Ignore
			}
		}

		public void write(String string) {
			if(isIgnorinCommands()) {
				return;
			}
			
			try {
				console.write(string);
			} catch (IOException e) {
				// Ignore
			}
		}

		public void flush() {
			if(isIgnorinCommands()) {
				return;
			}
			
			try {
				console.flush();
			} catch (IOException e) {
				// Ignore
			}
		}
		
	}
	
}