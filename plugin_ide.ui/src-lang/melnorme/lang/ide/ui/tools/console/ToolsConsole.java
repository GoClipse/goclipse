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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.io.IOException;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.part.IPageBookViewPage;

import melnorme.utilbox.ownership.OwnedObjects;

public class ToolsConsole extends AbstractProcessMessageConsole {
	
	public final IOConsoleOutputStream infoOut;
	protected final OwnedObjects owned = new OwnedObjects();
	
	public ToolsConsole(String name, ImageDescriptor imageDescriptor) {
		this(name, imageDescriptor, true);
	}
	
	protected ToolsConsole(String name, ImageDescriptor imageDescriptor, boolean initializeColors) {
		super(name, imageDescriptor);
		
		infoOut = newOutputStream();
		
		initialize_ActivateOnErrorMessages();
		
		if(initializeColors) {
			postToUI_initOutputStreamColors();
		}
	}
	
	protected void initialize_ActivateOnErrorMessages() {
		ToolsConsolePrefs.ACTIVATE_ON_ERROR_MESSAGES.getGlobalField().addOwnedListener(owned, true,
			() -> stdErr.setActivateOnWrite(ToolsConsolePrefs.ACTIVATE_ON_ERROR_MESSAGES.get())
		);
	}
	
	@Override
	protected void ui_initStreamColors() {
		super.ui_initStreamColors();
		
		ToolsConsolePrefs.INFO_COLOR.getGlobalField().addOwnedListener(owned, true, 
			() -> infoOut.setColor(ToolsConsolePrefs.INFO_COLOR.getManagedColor()));
		ToolsConsolePrefs.STDERR_COLOR.getGlobalField().addOwnedListener(owned, true, 
			() -> stdErr.setColor(ToolsConsolePrefs.STDERR_COLOR.getManagedColor()));
		ToolsConsolePrefs.STDOUT_COLOR.getGlobalField().addOwnedListener(owned, true, 
			() -> stdOut.setColor(ToolsConsolePrefs.STDOUT_COLOR.getManagedColor()));
		ToolsConsolePrefs.BACKGROUND_COLOR.getGlobalField().addOwnedListener(owned, true, 
			() -> setBackground(ToolsConsolePrefs.BACKGROUND_COLOR.getManagedColor()));
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
		try {
			infoOut.write(string);
		} catch (IOException e) {
			// Do nothing
		}
	}
	
}