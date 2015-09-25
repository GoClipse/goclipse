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

import melnorme.lang.ide.core.utils.prefs.IPrefChangeListener;
import melnorme.utilbox.ownership.OwnedObjects;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.part.IPageBookViewPage;

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
		owned.add(ToolsConsolePrefs.ACTIVATE_ON_ERROR_MESSAGES.addPrefChangeListener(true, 
			new IPrefChangeListener() {
				@Override
				public void handleChange() {
					stdErr.setActivateOnWrite(ToolsConsolePrefs.ACTIVATE_ON_ERROR_MESSAGES.get());
				}
			}
		));
	}
	
	@Override
	protected void ui_initStreamColors() {
		super.ui_initStreamColors();
		
		owned.add(ToolsConsolePrefs.INFO_COLOR.addPrefChangeListener(true, new IPrefChangeListener() {
			@Override
			public void handleChange() {
				infoOut.setColor(ToolsConsolePrefs.INFO_COLOR.getManagedColor());
			}
		}));
		owned.add(ToolsConsolePrefs.STDERR_COLOR.addPrefChangeListener(true, new IPrefChangeListener() {
			@Override
			public void handleChange() {
				stdErr.setColor(ToolsConsolePrefs.STDERR_COLOR.getManagedColor());
			}
		}));
		owned.add(ToolsConsolePrefs.STDOUT_COLOR.addPrefChangeListener(true, new IPrefChangeListener() {
			@Override
			public void handleChange() {
				stdOut.setColor(ToolsConsolePrefs.STDOUT_COLOR.getManagedColor());
			}
		}));
		owned.add(ToolsConsolePrefs.BACKGROUND_COLOR.addPrefChangeListener(true, new IPrefChangeListener() {
			@Override
			public void handleChange() {
				setBackground(ToolsConsolePrefs.BACKGROUND_COLOR.getManagedColor());
			}
		}));
		
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