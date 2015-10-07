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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.part.IPageBookViewPage;

import melnorme.lang.ide.ui.text.coloring.ThemedColorPreference;
import melnorme.util.swt.jface.text.ColorManager;
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
		
		if(initializeColors) {
			postToUI_initOutputStreamColors();
		}
	}
	
	@Override
	protected void ui_initStreamColors() {
		super.ui_initStreamColors();
		
		ToolsConsolePrefs.ACTIVATE_ON_ERROR_MESSAGES.getGlobalField().addOwnedListener(owned, true,
			() -> stdErr.setActivateOnWrite(ToolsConsolePrefs.ACTIVATE_ON_ERROR_MESSAGES.get())
		);
		
		ToolsConsolePrefs.INFO_COLOR.getGlobalField().addOwnedListener(owned, true, 
			() -> infoOut.setColor(getManagedColor(ToolsConsolePrefs.INFO_COLOR)));
		ToolsConsolePrefs.STDERR_COLOR.getGlobalField().addOwnedListener(owned, true, 
			() -> stdErr.setColor(getManagedColor(ToolsConsolePrefs.STDERR_COLOR)));
		ToolsConsolePrefs.STDOUT_COLOR.getGlobalField().addOwnedListener(owned, true, 
			() -> stdOut.setColor(getManagedColor(ToolsConsolePrefs.STDOUT_COLOR)));
		ToolsConsolePrefs.BACKGROUND_COLOR.getGlobalField().addOwnedListener(owned, true, 
			() -> setBackground(getManagedColor(ToolsConsolePrefs.BACKGROUND_COLOR)));
	}
	
	protected static Color getManagedColor(ThemedColorPreference colorPref) {
		return ColorManager.getDefault().getColor(colorPref.getValue().rgb);
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